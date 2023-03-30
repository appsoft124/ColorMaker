package com.example.colormix.viewmodel

import android.util.Log
import androidx.lifecycle.*

import com.example.colormix.ColorDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ColorViewModel(private val colorDataStore: ColorDataStore) : ViewModel() {

    private val _rgbValues = MutableLiveData(Triple(0f, 0f, 0f))
    val rgbValues: LiveData<Triple<Float, Float, Float>>
        get() = _rgbValues


    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val rgbValues = colorDataStore.rgbValues.first()
                Log.d("ColorViewModelLogs", "RGB values from database: $rgbValues")
                _rgbValues.postValue(rgbValues)



            }
        }


    }

    fun setRGBValues(red: Float, green: Float, blue: Float) {
        _rgbValues.postValue(Triple(red, green, blue))
        viewModelScope.launch {
            colorDataStore.saveRGBValues(red, green, blue)
        }
    }

}
