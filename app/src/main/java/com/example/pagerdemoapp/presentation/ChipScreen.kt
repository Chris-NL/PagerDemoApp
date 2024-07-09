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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch

@Composable
fun ChipScreen(
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
        .focusRequester(focusRequester)
        .focusable()

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
            Text(text = "Chip list")
        }
        item {
            Chip(label = { Text("Chip 1") }, icon = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Example Button",
                    modifier = Modifier
                        .size(ChipDefaults.LargeIconSize)
                        .wrapContentSize(align = Alignment.Center)
                )
                                                    }, onClick = { })
        }
        item {
            Chip(label = { Text("Chip 2") }, icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Example Button",
                    modifier = Modifier
                        .size(ChipDefaults.LargeIconSize)
                        .wrapContentSize(align = Alignment.Center)
                )
            }, onClick = { })
        }
        item {
            Chip(label = { Text("Chip 3") }, icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Example Button",
                    modifier = Modifier
                        .size(ChipDefaults.LargeIconSize)
                        .wrapContentSize(align = Alignment.Center)
                )
            }, onClick = { })
        }
        item {
            Chip(label = { Text("Chip 4") }, icon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Example Button",
                    modifier = Modifier
                        .size(ChipDefaults.LargeIconSize)
                        .wrapContentSize(align = Alignment.Center)
                )
            }, onClick = { })
        }
    }
}