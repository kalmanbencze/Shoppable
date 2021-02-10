package com.ikea.shoppable.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ikea.shoppable.ui.robots.BrowseRobot
import com.ikea.shoppable.view.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class ProductListUITest {
    private lateinit var robot: BrowseRobot

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun before() {
        robot = BrowseRobot(InstrumentationRegistry.getInstrumentation().targetContext)
        robot.insertTestData()
    }

    @Test
    fun testUIAppears() {
        robot.checkProductListIsVisible()
    }

    @Test
    fun test13thElementIsVisible() {
        robot.apply {
            checkElementIsVisible(12)
            checkDataIsPresentInRow(12, "Janinge")
        }
    }


    @Test
    fun testAddingItemToCartDisplaysSuccessMessage() {
        robot.apply {
            listItemCountIsAtLeast(1)
            addItemToCart(0)
            checkAddSucceededMessageIsVisible()
        }

    }

    @Test
    fun testItemsGetPutInCart() {
        robot.apply {
            listItemCountIsAtLeast(5)
            addItemToCart(0)
            addItemToCart(1)
            addItemToCart(2)
            checkCartIconBadgeText("3")
            //we scroll down a bit to awoid the snackbar undo button
            scrollListTo(7)
            addItemToCart(3)
            addItemToCart(4)
            checkCartIconBadgeText("5")
        }
    }

    @Test
    fun testCartOpens() {
        robot.apply {
            listItemCountIsAtLeast(5)
            addItemToCart(0)
            addItemToCart(1)
            checkCartIconBadgeText("2")
            openCart()
            checkProductListIsNotVisible()
            checkCartIsVisible()
        }
    }

    @Test
    fun testProductOpens() {
        robot.apply {
            listItemCountIsAtLeast(1)
            clickOnItem(0)
            checkProductListIsNotVisible()
            checkProductPageIsVisible("Henriksdal")
        }
    }
}