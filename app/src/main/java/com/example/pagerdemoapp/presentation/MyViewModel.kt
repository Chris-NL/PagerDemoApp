package com.example.pagerdemoapp.presentation

import android.util.Log
import android.view.KeyEvent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import kotlinx.coroutines.flow.MutableStateFlow

class MyViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    var activePage by mutableIntStateOf(savedStateHandle["activePage"] ?: 0)
    val greetingScreenScalingLazyListState by mutableStateOf(savedStateHandle["greetingScreenScalingLazyListState"] ?: ScalingLazyListState(initialCenterItemIndex = 1))
    val listScreenScalingLazyListState by mutableStateOf(savedStateHandle["listScreenScalingLazyListState"] ?: ScalingLazyListState(initialCenterItemIndex = 2))
    val cardScreenScalingLazyListState by mutableStateOf(savedStateHandle["cardScreenScalingLazyListState"] ?: ScalingLazyListState(initialCenterItemIndex = 2))
    val chipScreenScalingLazyListState by mutableStateOf(savedStateHandle["chipScreenScalingLazyListState"] ?: ScalingLazyListState(initialCenterItemIndex = 2))
    val textScreenScalingLazyListState by mutableStateOf(savedStateHandle["textScreenScalingLazyListState"] ?: ScalingLazyListState(initialCenterItemIndex = 2))
    val settingsScreenScalingLazyListState by mutableStateOf(savedStateHandle["settingsScreenScalingLazyListState"] ?: ScalingLazyListState(initialCenterItemIndex = 2))

    val greetingFocusRequester by mutableStateOf(savedStateHandle["greetingFocusRequester"] ?: FocusRequester())
    val cardFocusRequester by mutableStateOf(savedStateHandle["cardFocusRequester"] ?: FocusRequester())
    val chipFocusRequester by mutableStateOf(savedStateHandle["chipFocusRequester"] ?: FocusRequester())
    val textFocusRequester by mutableStateOf(savedStateHandle["textFocusRequester"] ?: FocusRequester())
    val settingsFocusRequester by mutableStateOf(savedStateHandle["settingsFocusRequester"] ?: FocusRequester())

    val buttonPressShortDetected = MutableStateFlow<KeyEvent?>(null)
    val buttonPressLongDetected = MutableStateFlow<KeyEvent?>(null)

    fun getScalingLazyListState(page: Int): ScalingLazyListState {
        Log.d("myViewModel", "getScalingLazyListState $page")

        val scalingLazyListState = when (page) {
            0 -> greetingScreenScalingLazyListState
            1 -> cardScreenScalingLazyListState
            2 -> chipScreenScalingLazyListState
            3 -> textScreenScalingLazyListState
            4 -> settingsScreenScalingLazyListState
            else -> {
                listScreenScalingLazyListState
            }
        }
        return scalingLazyListState
    }

    /*fun getScrollState(page: Int): ScrollState {
        Log.d("myViewModel", "getScrollState $page")

        val scrollState = when (page) {
            0 -> greetingScreenScrollState
            else -> {
                greetingScreenScrollState
            }
        }

        return scrollState
    }*/

    fun getFocusRequester(page: Int): FocusRequester {
        Log.d("myViewModel", "getFocusRequester $page")

        val focusRequester = when (page) {
            0 -> greetingFocusRequester
            1 -> cardFocusRequester
            2 -> chipFocusRequester
            3 -> textFocusRequester
            else -> {
                settingsFocusRequester
            }
        }
        return focusRequester
    }

}