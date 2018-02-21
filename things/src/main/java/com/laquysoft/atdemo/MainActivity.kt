package com.laquysoft.atdemo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.I2cDevice
import com.google.android.things.pio.PeripheralManagerService
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

    private lateinit var service: PeripheralManagerService
    private lateinit var i2cDevice: I2cDevice

    private val I2C_ADDRESS: String = "I2C1"
    private val TC74_TEMPERATURE_SENSOR_SLAVE = 0x4a

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = PeripheralManagerService()
        try {
            i2cDevice = service.openI2cDevice(I2C_ADDRESS, TC74_TEMPERATURE_SENSOR_SLAVE)
        } catch (e: IOException) {
            throw IllegalStateException("$I2C_ADDRESS bus slave $TC74_TEMPERATURE_SENSOR_SLAVE connection cannot be opened.", e)
        }

        while (true) {
            val array = ByteArray(1)
            i2cDevice.write(array, 1)

            val input = ByteArray(1)
            i2cDevice.read(input, 1)

            val temperature: Int = input[0].toInt() and 0xff
            Log.e("Sensor", "$temperature")

            Thread.sleep(3000)
        }

    }


    companion object {
        const val TAG = "BlinkLedActivity"
        const val DELAY = 1000L
    }

}
