package com.laquysoft.atdemo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import java.io.IOException

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {

    private lateinit var led: Led
    private val ledHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        led = Led("GPIO2_IO02")
        ledHandler.post(ledRunnable())

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            led.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error on closing GPIO", e)
        }
    }

    private fun ledRunnable(): Runnable = Runnable {
        try {
            led.blink()
            ledHandler.postDelayed(ledRunnable(), DELAY)
        } catch (e: IOException) {
            Log.e(TAG, "Error on peripheral", e)
        }
    }

    companion object {
        const val TAG = "BlinkLedActivity"
        const val DELAY = 1000L
    }

}
