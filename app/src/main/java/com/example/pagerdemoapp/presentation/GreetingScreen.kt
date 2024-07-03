/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.pagerdemoapp.presentation

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitVerticalDragOrCancellation
import androidx.compose.foundation.gestures.awaitVerticalTouchSlopOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.pagerdemoapp.R
import com.example.pagerdemoapp.presentation.theme.ActionDrawer
import kotlin.math.roundToInt

@Composable
fun GreetingScreen(
    scalingLazyListState: ScalingLazyListState,
    modifier: Modifier,
    greetingName: String,
    //focusRequester: FocusRequester,
    actionDrawerState: MutableState<Int>,
    greetingScreenVisible: MutableState<Boolean>,
    onScreenNavigated: (route: String?, navOptions: NavOptions?) -> Unit = { _: String?, _: NavOptions? -> }
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val screenHeight = (LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density)

    var bottomSwipe by remember { mutableStateOf(true) }
    var releaseThreshHold by remember { mutableFloatStateOf(0f) }
    val actionDrawerOffsetY = remember { mutableFloatStateOf(screenHeight) }
    val actionDrawerAnimatedOffsetY: Float by animateFloatAsState(actionDrawerOffsetY.floatValue, label = "actionDrawerAnimatedOffset")

    var boxModifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            awaitEachGesture {
                val down = awaitFirstDown()
                var change =
                    awaitVerticalTouchSlopOrCancellation(down.id) { change, over ->
                        if (actionDrawerState.value == 0) {
                            if (over < 0) {
                                change.consume()
                                // Bottom to top
                                bottomSwipe = true
                                val originalY = screenHeight
                                val newValue = (originalY + over).coerceIn(0f, screenHeight)
                                actionDrawerOffsetY.floatValue = newValue

                                releaseThreshHold = screenHeight - (screenHeight / 3)
                                Log.d("GreetingScreen", "actionDrawer awaitVerticalTouchSlopOrCancellation $originalY $newValue $over $releaseThreshHold")
                            }
                        } else if (actionDrawerState.value == 2) {
                            change.consume()

                            val originalY = actionDrawerOffsetY.floatValue
                            val newValue = (originalY + over).coerceIn(0f, screenHeight)
                            actionDrawerOffsetY.floatValue = newValue

                            releaseThreshHold = screenHeight / 4
                            Log.d("GreetingScreen", "actionDrawer awaitVerticalTouchSlopOrCancellation $originalY $newValue $over $releaseThreshHold")
                        }
                    }
                while (change != null && change.pressed) {
                    change = awaitVerticalDragOrCancellation(change.id)
                    if (change != null && change.pressed) {
                        if (bottomSwipe) {
                            actionDrawerState.value = 1
                            val originalY = actionDrawerOffsetY.floatValue
                            val newValue = (originalY + change.positionChange().y).coerceIn(0f, screenHeight)
                            change.consume()
                            actionDrawerOffsetY.floatValue = newValue
                            Log.d("GreetingScreen", "actionDrawer awaitVerticalDragOrCancellation $originalY $newValue")
                        }
                    }
                }
                if (change != null) {
                    if (bottomSwipe) {
                        if (actionDrawerOffsetY.floatValue < releaseThreshHold) {
                            actionDrawerState.value = 2
                            actionDrawerOffsetY.floatValue = 0f
                        } else {
                            actionDrawerState.value = 0
                            actionDrawerOffsetY.floatValue = screenHeight
                        }
                    }
                }
            }
        }

    if (LocalConfiguration.current.isScreenRound)
        boxModifier = boxModifier.then(Modifier.clip(CircleShape))

    boxModifier = boxModifier.then(
        Modifier
            .background(Color.Black)
    )

    LaunchedEffect(actionDrawerState) {
        greetingScreenVisible.value = (actionDrawerState.value != 2)
    }

    LaunchedEffect(actionDrawerOffsetY.floatValue) {
        if (actionDrawerOffsetY.floatValue == screenHeight) {
            greetingScreenVisible.value = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            greetingScreenVisible.value = false
        }
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Greeting(greetingName = greetingName)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Chip(label = { Text("Show List") }, onClick = { onScreenNavigated("list", null) })
            }
        }
        ActionDrawer(
            scalingLazyListState = scalingLazyListState,
            greetingScreenVisible = greetingScreenVisible,
            focusRequester = focusRequester,
            actionDrawerOffsetY = actionDrawerOffsetY,
            intOffset = IntOffset(0, actionDrawerAnimatedOffsetY.roundToInt()),
            actionDrawerState = actionDrawerState,
            onSelectedAction = { _  ->
            }
        )
    }
}

@Composable
fun Greeting(greetingName: String) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            text = stringResource(R.string.hello_world, greetingName)
        )
}
