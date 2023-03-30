package com.example.colormix

import android.graphics.Color
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.colormix.ColorDataStore.PreferenceKeys.BLUE_VALUE_KEY
import com.example.colormix.ColorDataStore.PreferenceKeys.GREEN_VALUE_KEY
import com.example.colormix.ColorDataStore.PreferenceKeys.RED_VALUE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.colorDataStore: DataStore<Preferences> by preferencesDataStore(name = "color_prefs")

class ColorDataStore(context: Context) {
    private val dataStore = context.colorDataStore

    suspend fun saveRGBValues(red: Float, green: Float, blue: Float) {

        Log.d("ColorDataStore", "red: " + red + "green: " + green + "blue:" + blue)
        dataStore.edit { preferences ->
            preferences[RED_VALUE_KEY] = red
            preferences[GREEN_VALUE_KEY] = green
            preferences[BLUE_VALUE_KEY] = blue
        }
    }

    val rgbValues: Flow<Triple<Float, Float, Float>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            Log.d("ColorDataSaveStoreLogs", "red: ${preferences[RED_VALUE_KEY]}, green: ${preferences[GREEN_VALUE_KEY]}, blue: ${preferences[BLUE_VALUE_KEY]}")
            Triple(
                preferences[RED_VALUE_KEY] ?: 0f,
                preferences[GREEN_VALUE_KEY] ?: 0f,
                preferences[BLUE_VALUE_KEY] ?: 0f
            )
        }


    fun getRedSwitchState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.RED_SWITCH_STATE] ?: false
        }
    }

    fun getGreenSwitchState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.GREEN_SWITCH_STATE] ?: false
        }
    }

    fun getBlueSwitchState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.BLUE_SWITCH_STATE] ?: false
        }
    }

    suspend fun saveRedSwitchState(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.RED_SWITCH_STATE] = state
        }
    }

    suspend fun saveGreenSwitchState(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.GREEN_SWITCH_STATE] = state
        }
    }

    suspend fun saveBlueSwitchState(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.BLUE_SWITCH_STATE] = state
        }
    }

    private object PreferenceKeys {
        val RED_VALUE_KEY = floatPreferencesKey("red")
        val GREEN_VALUE_KEY = floatPreferencesKey("green")
        val BLUE_VALUE_KEY = floatPreferencesKey("blue")
        val RED_SWITCH_STATE = booleanPreferencesKey("red_switch_state")
        val GREEN_SWITCH_STATE = booleanPreferencesKey("green_switch_state")
        val BLUE_SWITCH_STATE = booleanPreferencesKey("blue_switch_state")
    }

}

