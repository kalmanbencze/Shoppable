package com.ikea.shoppable.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.ui.robots.ProductRobot
import com.ikea.shoppable.view.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProductUITest {
    private lateinit var robot: ProductRobot

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun before() {
        robot = ProductRobot(InstrumentationRegistry.getInstrumentation().targetContext)
        robot.clearCart()
    }

    @Test
    fun testUIAppears() {
        robot.apply {
            openProductFromList(0)
            checkAddButtonIsVisible()
            checkProductDetailsAreVisible(
                "Henriksdal",
                Product.Companion.Type.CHAIR,
                "499 kr",
                " • white\n • wood with cover"
            )
        }
    }

    @Test
    fun testProductCanBeAddedToCart() {
        robot.apply {
            openProductFromList(0)
            pressAddToCart()
            checkCartHasItemCount(1)
            checkBadgeHasCount(1)
            pressAddToCart()
            checkCartHasItemCount(2)
            checkProductDetailsAreVisible(
                "Henriksdal",
                Product.Companion.Type.CHAIR,
                "499 kr",
                " • white\n • wood with cover"
            )
        }
    }
}