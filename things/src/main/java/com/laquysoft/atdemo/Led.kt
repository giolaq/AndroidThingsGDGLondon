package com.laquysoft.atdemo

import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService

/**
 * Created by joaobiriba on 21/02/2018.
 */
class Led(val pinName: String) : AutoCloseable {

    private var gpio: Gpio? = null

    init {
        val service = PeripheralManagerService()
        gpio = service.openGpio(pinName)
        gpio?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
    }

    fun blink() {
        gpio?.value = !(gpio?.value ?: false)
    }

    override fun close() {
        gpio?.close().also {
            gpio = null
        }
    }
}