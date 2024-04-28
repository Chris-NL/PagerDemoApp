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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Dialog

@Composable
fun SampleDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onOk: () -> Unit
) {
    val state = rememberScalingLazyListState()

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismiss,
        scrollState = state
    ) {
        SampleDialogContent(onCancel, onDismiss, onOk)
    }
}

@Composable
fun SampleDialogContent(
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    onOk: () -> Unit
) {
    Alert(
        icon = {},
        title = { Text("Title") }
    ) {
        item {
            Text(text = "An unknown error occurred during the request.")
        }
        item {
            Row {
                Button(
                    modifier = Modifier
                        .padding(end = 4.dp),
                    onClick = {
                        onCancel()
                        onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel Button",
                        modifier = Modifier
                            .size(ChipDefaults.LargeIconSize)
                            .wrapContentSize(align = Alignment.Center)
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    onClick = {
                        onOk()
                        onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Ok Button",
                        modifier = Modifier
                            .size(ChipDefaults.LargeIconSize)
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
            }
        }
    }
}