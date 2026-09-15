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

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.pagerdemoapp.presentation.theme.PagerDemoAppTheme

class MainActivity : ComponentActivity() {
    private val myViewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp(
                myViewModel = myViewModel,
                greetingName = "Android"
            )
        }
    }

    private var buttonShortPress = false
    private var buttonLongPress = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        event.startTracking()
        myViewModel.buttonPressShortDetected.value = null
        myViewModel.buttonPressLongDetected.value = null
        if (buttonLongPress) {
            buttonShortPress = false
        } else {
            buttonShortPress = true
            buttonLongPress = false
        }
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        event?.startTracking()
        if (buttonShortPress) {
            myViewModel.buttonPressShortDetected.value = event
        }
        buttonShortPress = true
        buttonLongPress = false

        return true
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        buttonShortPress = false
        buttonLongPress = true

        myViewModel.buttonPressLongDetected.value = event

        return true
    }
}

@Composable
fun WearApp(myViewModel: MyViewModel, greetingName: String) {
    val swipeDismissableNavController = rememberSwipeDismissableNavController()

    val greetingScreenVisible = remember { mutableStateOf(true) }

    PagerDemoAppTheme {
        Scaffold(
            positionIndicator = {
                // Only displays the position indicator for scrollable content.
                /*if (myViewModel.activePage == 0) {
                    PositionIndicator(
                        modifier = Modifier,
                        scrollState = myViewModel.getScrollState(0))
                } else {*/
                if (!greetingScreenVisible.value) {
                    PositionIndicator(
                        modifier = Modifier,
                        scalingLazyListState = myViewModel.getScalingLazyListState(myViewModel.activePage))
                }
                //}
            }
        ) {

            SwipeDismissableNavHost(
                navController = swipeDismissableNavController,
                startDestination = "pager",
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
            ) {

                composable("pager") {
                    PagerScreen(
                        swipeDismissableNavController = swipeDismissableNavController,
                        myViewModel = myViewModel,
                        greetingScreenVisible = greetingScreenVisible

                    )
                }

                composable("list") {
                    myViewModel.activePage = 8

                    ListScreen(
                        scalingLazyListState = myViewModel.listScreenScalingLazyListState
                    )
                }
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val myViewModel: MyViewModel = viewModel()

    WearApp(myViewModel, "Preview Android")
}