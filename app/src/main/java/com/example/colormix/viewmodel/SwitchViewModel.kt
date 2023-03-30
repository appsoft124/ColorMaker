package com.example.colormix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colormix.ColorDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SwitchViewModel(private val colorDataStore: ColorDataStore) : ViewModel() {

    private val _redSwitchState = MutableStateFlow(false)
    val redSwitchState: StateFlow<Boolean>
        get() = _redSwitchState

    private val _greenSwitchState = MutableStateFlow(false)
    val greenSwitchState: StateFlow<Boolean>
        get() = _greenSwitchState

    private val _blueSwitchState = MutableStateFlow(false)
    val blueSwitchState: StateFlow<Boolean>
        get() = _blueSwitchState

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                colorDataStore.getRedSwitchState().collect {
                    _redSwitchState.emit(it)
                }
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                colorDataStore.getGreenSwitchState().collect {
                    _greenSwitchState.emit(it)
                }
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                colorDataStore.getBlueSwitchState().collect {
                    _blueSwitchState.emit(it)
                }
            }
        }
    }

    suspend fun saveRedSwitchState(state: Boolean) {
        _redSwitchState.emit(state)
        colorDataStore.saveRedSwitchState(state)
    }

    suspend fun saveGreenSwitchState(state: Boolean) {
        _greenSwitchState.emit(state)
        colorDataStore.saveGreenSwitchState(state)
    }

    suspend fun saveBlueSwitchState(state: Boolean) {
        _blueSwitchState.emit(state)
        colorDataStore.saveBlueSwitchState(state)
    }
}