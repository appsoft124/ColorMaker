package com.example.colormix

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.colormix.databinding.ActivityMainBinding
import com.example.colormix.viewmodel.ColorViewModel
import com.example.colormix.viewmodel.SwitchViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var colorViewModel: ColorViewModel
    private lateinit var switchViewModel: SwitchViewModel
    private lateinit var binding: ActivityMainBinding

    private var redValue = 0f
    private var greenValue = 0f
    private var blueValue = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colorDataStore = ColorDataStore(this)
        colorViewModel = ViewModelProvider(this, ViewModelFactory(ColorViewModel(colorDataStore))).get(ColorViewModel::class.java)

        val switchDataStore = ColorDataStore(this)
        switchViewModel = ViewModelProvider(this, ViewModelFactory(SwitchViewModel(switchDataStore))).get(SwitchViewModel::class.java)

        binding.redSeekBar.setOnSeekBarChangeListener(ColorSliderListener(binding.redValueEditText))
        binding.greenSeekBar.setOnSeekBarChangeListener(ColorSliderListener(binding.greenValueEditText))
        binding.blueSeekBar.setOnSeekBarChangeListener(ColorSliderListener(binding.blueValueEditText))



        binding.redSwitch.setOnCheckedChangeListener { _, isChecked ->
            handleRedSwitch(isChecked)

        }

        binding.greenSwitch.setOnCheckedChangeListener { _, isChecked ->
            handleGreenSwitch(isChecked)
        }

        binding.blueSwitch.setOnCheckedChangeListener { _, isChecked ->
            handleBlueSwitch(isChecked)
        }

        binding.resetButton.setOnClickListener {
            resetColors()
        }


        // Observe the switch states and update the switches accordingly
        lifecycleScope.launch {
            switchViewModel.redSwitchState.collect { isChecked ->
                binding.redSwitch.isChecked = isChecked
                updateControlsEnabled()

            }
        }
        lifecycleScope.launch {
            switchViewModel.greenSwitchState.collect { isChecked ->
                binding.greenSwitch.isChecked = isChecked
                updateControlsEnabled()

            }
        }
        lifecycleScope.launch {
            switchViewModel.blueSwitchState.collect { isChecked ->
                binding.blueSwitch.isChecked = isChecked
                updateControlsEnabled()

            }
        }

        colorViewModel.rgbValues.observe(this) { (red, green, blue) ->
            // Check if the new RGB values are different from the current ones
            setRGBValues(red, green, blue)
            Log.d("MainActivityLogs", "Red value: $red, Green value: $green, Blue value: $blue")

            redValue = red
            greenValue = green
            blueValue = blue
        }

    }


//    @SuppressLint("SetTextI18n")
//    private fun handleRedSwitch(isChecked: Boolean) {
//        lifecycleScope.launch{
//            switchViewModel.saveRedSwitchState(isChecked)
//        }
//
//
//        if (isChecked) {
//            // Enable red seek bar and edit text
//            binding.redSeekBar.isEnabled = true
//            binding.redValueEditText.isEnabled = true
//            binding.redSeekBar.progress = (redValue * 100).toInt()
//            binding.redValueEditText.setText(String.format("%.2f", redValue))
//
//        } else {
//            // Disable red seek bar and edit text
//            binding.redSeekBar.isEnabled = false
//            binding.redValueEditText.isEnabled = false
//            binding.redSeekBar.progress = 0
//            binding.redValueEditText.setText("0.00")
//
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun handleGreenSwitch(isChecked: Boolean) {
//        lifecycleScope.launch{
//            switchViewModel.saveGreenSwitchState(isChecked)
//        }
//
//
//        if (isChecked) {
//            // Enable green seek bar and edit text
//            binding.greenSeekBar.isEnabled = true
//            binding.greenValueEditText.isEnabled = true
//            binding.greenSeekBar.progress = (greenValue * 100).toInt()
//            binding.greenValueEditText.setText(String.format("%.2f", greenValue))
//
//        } else {
//            // Disable green seek bar and edit text
//            binding.greenSeekBar.isEnabled = false
//            binding.greenValueEditText.isEnabled = false
//            binding.greenSeekBar.progress = 0
//            binding.greenValueEditText.setText("0.00")
//
//
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//
//    private fun handleBlueSwitch(isChecked: Boolean) {
//        lifecycleScope.launch{
//            switchViewModel.saveBlueSwitchState(isChecked)
//        }
//
//        if (isChecked) {
//            // Enable blue seek bar and edit text
//            binding.blueSeekBar.isEnabled = true
//            binding.blueValueEditText.isEnabled = true
//            binding.blueSeekBar.progress = (blueValue * 100).toInt()
//            binding.blueValueEditText.setText(String.format("%.2f", blueValue))
//
//        } else {
//            // Disable blue seek bar and edit text
//            binding.blueSeekBar.isEnabled = false
//            binding.blueValueEditText.isEnabled = false
//            binding.blueSeekBar.progress = 0
//            binding.blueValueEditText.setText("0.00")
//
//
//        }
//    }

    private fun handleRedSwitch(isChecked: Boolean) {
        lifecycleScope.launch{
            switchViewModel.saveRedSwitchState(isChecked)
        }

        if (isChecked) {
            // Enable red seek bar and edit text
            binding.redSeekBar.isEnabled = true
            binding.redValueEditText.isEnabled = true
            binding.redSeekBar.progress = (redValue * 100).toInt()
            binding.redValueEditText.setText(String.format("%.2f", redValue))
        } else {
            // Disable red seek bar and edit text without resetting the values
            binding.redSeekBar.isEnabled = false
            binding.redValueEditText.isEnabled = false
        }
    }

    private fun handleGreenSwitch(isChecked: Boolean) {
        lifecycleScope.launch{
            switchViewModel.saveGreenSwitchState(isChecked)
        }

        if (isChecked) {
            // Enable green seek bar and edit text
            binding.greenSeekBar.isEnabled = true
            binding.greenValueEditText.isEnabled = true
            binding.greenSeekBar.progress = (greenValue * 100).toInt()
            binding.greenValueEditText.setText(String.format("%.2f", greenValue))
        } else {
            // Disable green seek bar and edit text without resetting the values
            binding.greenSeekBar.isEnabled = false
            binding.greenValueEditText.isEnabled = false
        }
    }

    private fun handleBlueSwitch(isChecked: Boolean) {
        lifecycleScope.launch{
            switchViewModel.saveBlueSwitchState(isChecked)
        }

        if (isChecked) {
            // Enable blue seek bar and edit text
            binding.blueSeekBar.isEnabled = true
            binding.blueValueEditText.isEnabled = true
            binding.blueSeekBar.progress = (blueValue * 100).toInt()
            binding.blueValueEditText.setText(String.format("%.2f", blueValue))
        } else {
            // Disable blue seek bar and edit text without resetting the values
            binding.blueSeekBar.isEnabled = false
            binding.blueValueEditText.isEnabled = false
        }
    }

    private fun resetColors() {
        setRGBValues(0f, 0f, 0f)
        binding.redSwitch.isChecked = false
        binding.greenSwitch.isChecked = false
        binding.blueSwitch.isChecked = false

        binding.redSeekBar.isEnabled = false
        binding.greenSeekBar.isEnabled = false
        binding.blueSeekBar.isEnabled = false

        binding.redValueEditText.isEnabled = false
        binding.greenValueEditText.isEnabled = false
        binding.blueValueEditText.isEnabled = false
    }

    inner class ColorSliderListener(private val editText: EditText) :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val value = progress.toFloat() / 100
            editText.setValue(value)
//            setRGBValues(
//                binding.redSeekBar.progress.toFloat() / 100,
//                binding.greenSeekBar.progress.toFloat() / 100,
//                binding.blueSeekBar.progress.toFloat() / 100
//            )

            val red = binding.redSeekBar.progress.toFloat() / 100
            val green = binding.greenSeekBar.progress.toFloat() / 100
            val blue = binding.blueSeekBar.progress.toFloat() / 100
            colorViewModel.setRGBValues(red, green, blue)


        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }


    fun EditText.setValue(value: Float) {
        this.setText(String.format("%.2f", value))
    }


    private fun setRGBValues(red: Float, green: Float, blue: Float) {
        Log.d("ColorViewModelLogs", "setRGBValues red: $red")
        Log.d("ColorViewModelLogs", "setRGBValues green: $green")
        Log.d("ColorViewModelLogs", "setRGBValues blue: $blue")

        runOnUiThread {
            binding.redSeekBar.progress = (red * 100).toInt()
            binding.greenSeekBar.progress = (green * 100).toInt()
            binding.blueSeekBar.progress = (blue * 100).toInt()

            binding.redValueEditText.setText(String.format("%.2f", red))
            binding.greenValueEditText.setText(String.format("%.2f", green))
            binding.blueValueEditText.setText(String.format("%.2f", blue))

            val color = Color.rgb(
                (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt()
            )

            binding.colorSwatch.setBackgroundColor(color)
        }


    }

//    companion object {
//        private const val RED_SWITCH_STATE = "red_switch_state"
//        private const val GREEN_SWITCH_STATE = "green_switch_state"
//        private const val BLUE_SWITCH_STATE = "blue_switch_state"
//    }
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putBoolean(RED_SWITCH_STATE, binding.redSwitch.isChecked)
//        outState.putBoolean(GREEN_SWITCH_STATE, binding.greenSwitch.isChecked)
//        outState.putBoolean(BLUE_SWITCH_STATE, binding.blueSwitch.isChecked)
//    }
//
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        binding.redSwitch.isChecked = savedInstanceState.getBoolean(RED_SWITCH_STATE)
//        binding.greenSwitch.isChecked = savedInstanceState.getBoolean(GREEN_SWITCH_STATE)
//        binding.blueSwitch.isChecked = savedInstanceState.getBoolean(BLUE_SWITCH_STATE)
//
//        updateControlsEnabled()
//    }


    private fun updateControlsEnabled() {

        val redSwitchEnabled = binding.redSwitch.isChecked
        val greenSwitchEnabled = binding.greenSwitch.isChecked
        val blueSwitchEnabled = binding.blueSwitch.isChecked

        Log.d("updateControlsEnabled", "redSwitchEnabled: $redSwitchEnabled")
        Log.d("updateControlsEnabled", "greenSwitchEnabled: $greenSwitchEnabled")
        Log.d("updateControlsEnabled", "blueSwitchEnabled: $blueSwitchEnabled")


        binding.redSeekBar.isEnabled = redSwitchEnabled
        binding.greenSeekBar.isEnabled = greenSwitchEnabled
        binding.blueSeekBar.isEnabled = blueSwitchEnabled

        binding.redValueEditText.isEnabled = redSwitchEnabled
        binding.greenValueEditText.isEnabled = redSwitchEnabled
        binding.blueValueEditText.isEnabled = redSwitchEnabled

    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putBoolean("redSwitchState", switchViewModel.redSwitchState.value ?: false)
//        outState.putBoolean("greenSwitchState", switchViewModel.greenSwitchState.value ?: false)
//        outState.putBoolean("blueSwitchState", switchViewModel.blueSwitchState.value ?: false)
//    }


}