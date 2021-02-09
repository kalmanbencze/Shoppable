package com.ikea.shoppable

import com.ikea.shoppable.model.Info
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun infoFormatting() {
        var info = Info("iron", "black", null)
        assertEquals(" • black\n • iron", info.toString())
        info = Info(null, "brown", 3)
        assertEquals(" • brown\n • 3 people", info.toString())
        info = Info("leather", "light brown", 1)
        assertEquals(" • light brown\n • leather\n • 1 person", info.toString())
        info = Info(null, "", null)
        assertEquals("", info.toString())
        info = Info(null, "", 0)
        assertEquals("", info.toString())
    }
}