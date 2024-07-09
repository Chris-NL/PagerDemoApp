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

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import kotlinx.coroutines.launch

@Composable
fun CardsScreen(
    scalingLazyListState: ScalingLazyListState,
    modifier: Modifier,
    //focusRequester: FocusRequester
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var scalingLazyColumnModifier = modifier
        .fillMaxSize()

    if (LocalConfiguration.current.isScreenRound)
        scalingLazyColumnModifier = scalingLazyColumnModifier.then(Modifier.clip(CircleShape))

    scalingLazyColumnModifier = scalingLazyColumnModifier.then(
        Modifier
            .background(Color.Black)
            /*.onRotaryScrollEvent {
                coroutineScope.launch {
                    scalingLazyListState.scrollBy(it.verticalScrollPixels)
                }
                true
            }*/
            .focusRequester(focusRequester)
            .focusable()
    )

    ScalingLazyColumn(
        state = scalingLazyListState,
        modifier = scalingLazyColumnModifier
    ) {
        item {
            Text(text = "Cards list")
        }
        item {
            TitleCard(title = { Text("Card 1") }, onClick = { }) {
                Text("Example Content\nThis is a card\nJust to display something")
            }
        }
        item {
            TitleCard(title = { Text("Card 2") }, onClick = { }) {
                Text("Example Content\nThis is a card\nJust to display something")
            }
        }
        item {
            TitleCard(title = { Text("Card 3") }, onClick = { }) {
                Text("Example Content\nThis is a card\nJust to display something")
            }
        }
        item {
            TitleCard(title = { Text("Card 4") }, onClick = { }) {
                Text("Example Content\nThis is a card\nJust to display something")
            }
        }
    }
}