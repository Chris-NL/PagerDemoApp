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

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard

@Composable
fun ListScreen(
    scalingLazyListState: ScalingLazyListState,
) {
    //val focusRequester = remember { FocusRequester() }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        //focusRequester.requestFocus()
    }

    ScalingLazyColumn(
        state = scalingLazyListState,
        modifier = Modifier
            .fillMaxSize()
            //.focusRequester(focusRequester)
            //.focusable()
    ) {
        item {
            Text(text = "Header")
        }
        item {
            TitleCard(title = { Text("Example Title") }, onClick = { }) {
                Text("Example Content\nMore Lines\nAnd More")
            }
        }
        item {
            Chip(label = { Text("Example Chip") }, onClick = { })
        }
        item {
            Button(
                onClick = { showDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Example Button",
                    modifier = Modifier
                        .size(ChipDefaults.LargeIconSize)
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }
    }

    SampleDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onCancel = {},
        onOk = {}
    )
}