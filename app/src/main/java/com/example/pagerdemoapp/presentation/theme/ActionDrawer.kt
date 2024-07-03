package com.example.pagerdemoapp.presentation.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSmallRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSquare
import com.example.pagerdemoapp.presentation.onPointerEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ActionDrawer(
    scalingLazyListState: ScalingLazyListState,
    greetingScreenVisible: MutableState<Boolean>,
    focusRequester: FocusRequester,
    actionDrawerOffsetY: MutableState<Float>,
    intOffset: IntOffset,
    actionDrawerState: MutableState<Int>,
    onSelectedAction: (action: String) -> Unit = { }
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    var countDownTimer by remember { mutableIntStateOf(5) }
    val scrollState = scalingLazyListState

    val coroutineScope = rememberCoroutineScope()

    var userScrollEnabled by remember { mutableStateOf(true) }
    var actionDrawerWasFullyOpened by remember { mutableStateOf(false) }
    var actionDrawerDirection by remember { mutableIntStateOf(0) } // 0 = Idle, 1 = opening, 2 = opened, 3 = closing, 4 = closed
    var actionDrawerState2 by remember { mutableIntStateOf(actionDrawerState.value) } // 0 = Closed, 1 = opening / closing, 2 opened

    LaunchedEffect(key1 = actionDrawerDirection) {
        Log.d("ActionDrawer", "LaunchedEffect actionDrawerDirection: $actionDrawerDirection")
        if (actionDrawerDirection == 2) {
            focusRequester.requestFocus()
            Log.d("ActionDrawer", "Starting countdown")
            countDownTimer = 5
            while (countDownTimer > 0) {
                delay(1000)
                countDownTimer -= 1
                if (scrollState.isScrollInProgress) {
                    countDownTimer = 5
                }
                Log.d("ActionDrawer", "Counting down $countDownTimer")
            }
            Log.d("ActionDrawer", "Forcing close")
            greetingScreenVisible.value = true
            actionDrawerState.value = 0
            userScrollEnabled = false
            actionDrawerOffsetY.value = screenHeight
        }
    }

    ScalingLazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
            .offset {
                if (!actionDrawerWasFullyOpened) {
                    if (actionDrawerState.value != 2) {
                        actionDrawerDirection = 1
                    } else {
                        if (intOffset == IntOffset(0, 0)) {
                            actionDrawerDirection = 2
                            actionDrawerState2 = 2
                            actionDrawerWasFullyOpened = true
                            userScrollEnabled = true
                        } else {
                            actionDrawerDirection = 1
                        }
                    }
                } else {
                    if (intOffset == IntOffset(0, screenHeight.toInt())) {
                        actionDrawerState2 = 0
                        actionDrawerDirection = 4
                        actionDrawerWasFullyOpened = false
                        coroutineScope.launch {
                            scrollState.scrollToItem(1)
                        }
                    } else if (intOffset != IntOffset(0, 0)) {
                        actionDrawerDirection = 3
                    }
                }
                intOffset
            }
            .onPointerEvent {
                val event = it.changes[0]

                if (actionDrawerState2 == 2 && event.pressed && event.position.y < (screenHeight / 10)) {
                    event.consume()
                    actionDrawerState2 = 1
                    val delta = event.position.y - event.previousPosition.y
                    actionDrawerOffsetY.value = delta
                } else if (actionDrawerState2 == 1 && event.pressed) {
                    event.consume()
                    val delta = event.position.y - event.previousPosition.y

                    val originalY = actionDrawerOffsetY.value
                    val newValue = (originalY + delta).coerceIn(0f, screenHeight)
                    actionDrawerOffsetY.value = newValue
                } else if (actionDrawerState2 == 1) {
                    event.consume()
                    if (actionDrawerOffsetY.value < (screenHeight / 4)) {
                        actionDrawerOffsetY.value = 0f
                        actionDrawerState2 = 2
                    } else {
                        actionDrawerOffsetY.value = screenHeight
                        actionDrawerState2 = 0
                        onSelectedAction("")
                    }
                }
            }
            .background(Color(0xFF1F1D1D))
            .focusRequester(focusRequester)
            .focusable(),
        userScrollEnabled = userScrollEnabled
    ) {
        item {
            Chip(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (LocalConfiguration.current.isScreenRound) 35.dp else 0.dp),
                label = {
                    Text(
                        text = "Account",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                colors = ChipDefaults.chipColors(
                    backgroundColor = Color(0xFF383838)
                ),
                onClick = { },
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account",
                            modifier = Modifier
                                .size(ChipDefaults.LargeIconSize)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
            )
        }
        item {
            Chip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Call",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                colors = ChipDefaults.chipColors(
                    backgroundColor = Color(0xFF383838)
                ),
                onClick = {

                },
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            modifier = Modifier
                                .size(ChipDefaults.LargeIconSize)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
            )
        }
        item {
            Chip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Lock",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                colors = ChipDefaults.chipColors(
                    backgroundColor = Color(0xFF383838)
                ),
                onClick = {

                },
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
                            modifier = Modifier
                                .size(ChipDefaults.LargeIconSize)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
            )
        }
        item {
            Chip(
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Play",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Clip
                    )
                },
                colors = ChipDefaults.chipColors(
                    backgroundColor = Color(0xFF383838)
                ),
                onClick = {

                },
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier
                                .size(ChipDefaults.LargeIconSize)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
            )
        }
    }
}

@WearPreviewLargeRound
@WearPreviewSmallRound
@WearPreviewSquare
@Composable
fun ActionDrawerPreview() {
    ActionDrawer(ScalingLazyListState(), greetingScreenVisible = remember { mutableStateOf(false) }, focusRequester = remember { FocusRequester() }, actionDrawerOffsetY = remember { mutableFloatStateOf(0f) }, intOffset = IntOffset(0, 0), actionDrawerState = remember { mutableIntStateOf(2) })
}