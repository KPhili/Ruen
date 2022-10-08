package com.example.ruen

import org.junit.Test

import org.junit.Assert.*
import java.util.Calendar

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() {
        val c = Calendar.getInstance()
        val f = Calendar.getInstance().apply {
            set(Calendar.HOUR, 14)
            set(Calendar.AM_PM, Calendar.AM)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val d = f.timeInMillis - c.timeInMillis
        println(c.timeInMillis)
        println(f.timeInMillis)
        println(d)
        val now = Calendar.getInstance()
        now.timeInMillis += d
        println(now)
    }
}