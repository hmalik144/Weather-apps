package com.appttude.h_mal.atlas_weather.mvvm.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class DateUtilsKtTest{

    @Test
    fun toDayString_testStandardData_outputCorrect() {
        val input = 1606183160

        val result = input.toDayString()

        assertEquals(result, "Nov 24")
    }

    @Test
    fun toDayName_testStandardData_outputCorrect() {
        val input = 1606183160

        val result = input.toDayName()

        assertEquals(result, "Tuesday")
    }
}