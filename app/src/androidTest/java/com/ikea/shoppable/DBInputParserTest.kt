package com.ikea.shoppable

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ikea.shoppable.persistence.db.DBInputParser

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DBInputParserTest {
    @Test
    fun parseInputFile() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val path = "test_products.json"
        val products = DBInputParser.readProducts(appContext, path)
        assert(products.size == 2)
    }
}