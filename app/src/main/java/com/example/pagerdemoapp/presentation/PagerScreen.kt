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

import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.wear.compose.foundation.AnchorType
import androidx.wear.compose.foundation.CurvedDirection
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.HierarchicalFocusCoordinator
import androidx.wear.compose.foundation.curvedBox
import androidx.wear.compose.foundation.curvedComposable
import androidx.wear.compose.foundation.curvedRow
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSmallRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSquare
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun PagerScreen(
    myViewModel: MyViewModel,
    swipeDismissableNavController: NavHostController,
    greetingScreenVisible: MutableState<Boolean>,
) {
    val scope = rememberCoroutineScope()

    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    var allowPaging by remember { mutableStateOf(true) }

    val pagesCount = 5
    val pagerState = rememberPagerState { pagesCount }
    var showPagerIndicator by remember { mutableStateOf(false) }

    val actionDrawerState = remember { mutableIntStateOf(0) } // 0 = Closed, 1 = opening / closing, 2 opened

    val isDraggable = pagerState.interactionSource.collectIsDraggedAsState()

    val buttonPressShort by myViewModel.buttonPressShortDetected.collectAsStateWithLifecycle()
    val buttonPressLong by myViewModel.buttonPressLongDetected.collectAsStateWithLifecycle()

    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            Log.d("Page change", "Page changed to $page ${isDraggable.value}")
        }
    }

    LaunchedEffect(pagerState.interactionSource.collectIsDraggedAsState().value) {
        Log.d("Page change", "Dragging is true $isDraggable")

        if (isDraggable.value) {
            showPagerIndicator = isDraggable.value
        } else if (pagerState.currentPage == pagerState.settledPage) {
            //Dragging was apparently cancelled
            /*when (pagerState.currentPage) {
                0 -> myViewModel.greetingFocusRequester.requestFocus()
                1 -> myViewModel.cardFocusRequester.requestFocus()
                2 -> myViewModel.chipFocusRequester.requestFocus()
                3 -> myViewModel.textFocusRequester.requestFocus()
                4 -> myViewModel.settingsFocusRequester.requestFocus()
            }*/
            myViewModel.activePage = pagerState.currentPage
            coroutineScope {
                delay(750)
                showPagerIndicator = false
            }
        }
    }

    LaunchedEffect(Unit) {
        Log.d("Page change", "On start no dragging")
        myViewModel.activePage = pagerState.currentPage

        showPagerIndicator = false
    }

    LaunchedEffect(buttonPressShort) {
        if (buttonPressShort != null) {
            val keyEvent = buttonPressShort
            myViewModel.buttonPressShortDetected.value = null
            CoroutineScope(Dispatchers.Main).launch {
                when (keyEvent!!.keyCode) {
                    KeyEvent.KEYCODE_STEM_1 -> {
                        Log.d("PagerScreen", "Button press")
                        // Check to make sure that this observer receives a trigger for a button-press in another screen
                        if (SystemClock.uptimeMillis() - keyEvent.eventTime < 250) {
                            if ((pagerState.currentPage == pagerState.settledPage) && !isDraggable.value && pagerState.currentPage == 0) {
                                //performMatchAction(true)
                            } else if ((pagerState.currentPage == pagerState.settledPage) && !isDraggable.value) {
                                Log.d("PagerScreen", "Scroll to page 0")
                                pagerState.scrollToPage(0)
                            }
                        }
                    }
                    KeyEvent.KEYCODE_STEM_2 -> {
                    }
                    KeyEvent.KEYCODE_STEM_3 -> {
                    }
                }
            }
        }
    }

    LaunchedEffect(buttonPressLong) {
        if (buttonPressLong != null) {
            val keyEvent = buttonPressLong
            myViewModel.buttonPressLongDetected.value = null
            CoroutineScope(Dispatchers.Main).launch {
                when (keyEvent!!.keyCode) {
                    KeyEvent.KEYCODE_STEM_1 -> {
                        Log.d("PagerScreen", "Button long press")
                        // Check to make sure that this observer receives a trigger for a button-press in another screen
                        if (SystemClock.uptimeMillis() - keyEvent.eventTime < 250) {
                            scope.launch {
                                pagerState.animateScrollToPage(page = 0, animationSpec = tween(150, 0))
                            }
                        }
                    }
                    KeyEvent.KEYCODE_STEM_2 -> {
                    }
                    KeyEvent.KEYCODE_STEM_3 -> {
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212124))
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(screenWidth) {
                    coroutineScope {
                        awaitEachGesture {
                            allowPaging = true
                            val firstDown =
                                awaitFirstDown(false, PointerEventPass.Initial)
                            val xPosition = firstDown.position.x
                            // Define edge zone of 15%, only when on the first page
                            allowPaging =
                                if (pagerState.settledPage == 0) ((actionDrawerState.intValue == 0) && (xPosition > screenWidth * 0.15f)) else if (actionDrawerState.intValue == 0) true else false
                        }
                    }
                }
                .semantics {
                    horizontalScrollAxisRange = if (allowPaging) {
                        ScrollAxisRange(value = { pagerState.currentPage.toFloat() },
                            maxValue = { 3f })
                    } else {
                        // signals system swipe to dismiss that they can take over
                        ScrollAxisRange(value = { 0f },
                            maxValue = { 0f })
                    }
                }
            ,
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(1),
                snapAnimationSpec = tween(150, 0),
            ),
            userScrollEnabled = allowPaging
        ) { page ->
            HierarchicalFocusCoordinator(requiresFocus = { pagerState.currentPage == page }) {
                when (page) {
                    0 -> {
                        GreetingScreen(
                            modifier = Modifier,
                            scalingLazyListState = myViewModel.greetingScreenScalingLazyListState,
                            //focusRequester = myViewModel.greetingFocusRequester,
                            greetingName = "Android",
                            actionDrawerState = actionDrawerState,
                            greetingScreenVisible = greetingScreenVisible,
                            onScreenNavigated = { route: String?, navOptions: NavOptions? ->
                                swipeDismissableNavController.navigate(
                                    route!!,
                                    navOptions = navOptions
                                )
                            }
                        )
                    }

                    1 -> {
                        CardsScreen(
                            modifier = Modifier,
                            scalingLazyListState = myViewModel.cardScreenScalingLazyListState,
                            //focusRequester = myViewModel.cardFocusRequester
                        )
                    }

                    2 -> {
                        ChipScreen(
                            modifier = Modifier,
                            scalingLazyListState = myViewModel.chipScreenScalingLazyListState,
                            //focusRequester = myViewModel.chipFocusRequester
                        )
                    }

                    3 -> {
                        TextScreen(
                            modifier = Modifier,
                            scalingLazyListState = myViewModel.textScreenScalingLazyListState,
                            //focusRequester = myViewModel.textFocusRequester
                        )
                    }

                    4 -> {
                        SettingsScreen(
                            modifier = Modifier,
                            scalingLazyListState = myViewModel.settingsScreenScalingLazyListState,
                            //focusRequester = myViewModel.settingsFocusRequester
                        )
                    }
                }
            }
        }

        if (showPagerIndicator) {
            if (LocalConfiguration.current.isScreenRound) {
                CurvedLayout(
                    anchor = 90F,
                    anchorType = AnchorType.Center,
                    angularDirection = CurvedDirection.Angular.CounterClockwise,
                    modifier = Modifier.fillMaxSize()
                ) {
                    curvedRow {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.White else Color.Gray
                            curvedBox {
                                curvedComposable {
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .size(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) Color.White else Color.Gray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
            }
        }
    }

}

@WearPreviewLargeRound
@WearPreviewSmallRound
@WearPreviewSquare
@Composable
fun PagerScreenPreview() {
    val context = LocalContext.current
    val myViewModel: MyViewModel = viewModel()

    PagerScreen(
        myViewModel = myViewModel,
        swipeDismissableNavController = NavHostController(context),
        greetingScreenVisible = remember { mutableStateOf(false) }
    )
}