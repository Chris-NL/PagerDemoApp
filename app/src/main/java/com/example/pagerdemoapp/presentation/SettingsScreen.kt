package com.example.pagerdemoapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSmallRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSquare
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    scalingLazyListState: ScalingLazyListState,
    modifier: Modifier,
    //focusRequester: FocusRequester
) {
    var setting1 by remember { mutableStateOf(true) }
    var setting2 by remember { mutableStateOf(true) }
    var setting3 by remember { mutableStateOf(false) }
    var setting4 by remember { mutableStateOf(true) }
    var setting5 by remember { mutableStateOf(false) }
    var setting6 by remember { mutableStateOf(true) }
    var setting7 by remember { mutableStateOf(true) }

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
        modifier = scalingLazyColumnModifier,
    ) {
        item {
            Text(
                text = "Settings",
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
        item {
            ToggleChip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Setting 1",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                checked = setting1,
                colors = ToggleChipDefaults.toggleChipColors(
                    uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                ),
                toggleControl = {
                    Switch(
                        checked = setting1,
                        enabled = true,
                        modifier = Modifier.semantics {
                            contentDescription = if (setting1)
                                "On" else
                                "Off"
                        }
                    )
                },
                onCheckedChange = {
                    setting1 = it
                },
                enabled = true,
            )
        }
        item {
            ToggleChip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Setting 2",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                checked = setting2,
                colors = ToggleChipDefaults.toggleChipColors(
                    uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                ),
                toggleControl = {
                    Switch(
                        checked = setting2,
                        enabled = true,
                        modifier = Modifier.semantics {
                            contentDescription = if (setting2)
                                "On" else
                                "Off"
                        }
                    )
                },
                onCheckedChange = {
                    setting2 = it
                },
                enabled = true,
            )
        }
        item {
            ToggleChip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Setting 3",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                checked = setting3,
                colors = ToggleChipDefaults.toggleChipColors(
                    uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                ),
                toggleControl = {
                    Switch(
                        checked = setting3,
                        enabled = true,
                        modifier = Modifier.semantics {
                            contentDescription = if (setting3)
                                "On" else
                                "Off"
                        }
                    )
                },
                onCheckedChange = {
                    setting3 = it
                },
                enabled = true,
            )
        }
        item {
            ToggleChip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Setting 4",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                checked = setting4,
                colors = ToggleChipDefaults.toggleChipColors(
                    uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                ),
                toggleControl = {
                    Switch(
                        checked = setting4,
                        enabled = true,
                        modifier = Modifier.semantics {
                            contentDescription = if (setting4)
                                "On" else
                                "Off"
                        }
                    )
                },
                onCheckedChange = {
                    setting4 = it
                },
                enabled = true,
            )
        }
        item {
            ToggleChip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Setting 5",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                checked = setting5,
                colors = ToggleChipDefaults.toggleChipColors(
                    uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                ),
                toggleControl = {
                    Switch(
                        checked = setting5,
                        enabled = true,
                        modifier = Modifier.semantics {
                            contentDescription = if (setting5)
                                "On" else
                                "Off"
                        }
                    )
                },
                onCheckedChange = {
                    setting5 = it
                },
                enabled = true,
            )
        }
        item {
            ToggleChip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Setting 6",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                checked = setting6,
                colors = ToggleChipDefaults.toggleChipColors(
                    uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                ),
                toggleControl = {
                    Switch(
                        checked = setting6,
                        enabled = true,
                        modifier = Modifier.semantics {
                            contentDescription = if (setting6)
                                "On" else
                                "Off"
                        }
                    )
                },
                onCheckedChange = {
                    setting6 = it
                },
                enabled = true,
            )
        }
        item {
            ToggleChip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Setting 7",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                checked = setting7,
                colors = ToggleChipDefaults.toggleChipColors(
                    uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                ),
                toggleControl = {
                    Switch(
                        checked = setting7,
                        enabled = true,
                        modifier = Modifier.semantics {
                            contentDescription = if (setting7)
                                "On" else
                                "Off"
                        }
                    )
                },
                onCheckedChange = {
                    setting7 = it
                },
                enabled = true,
            )
        }
    }
}

@WearPreviewLargeRound
@WearPreviewSmallRound
@WearPreviewSquare
@Composable
fun SettingsPreview() {
    //SettingsScreen(ScalingLazyListState(),  Modifier, authViewModel, matchViewModel, PagerState(), 0)
}