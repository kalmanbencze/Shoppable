package com.ikea.shoppable.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ikea.shoppable.ui.robots.CartRobot
import com.ikea.shoppable.view.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class CartTest {
    private lateinit var robot: CartRobot

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun before() {
        robot = CartRobot(InstrumentationRegistry.getInstrumentation().targetContext)
        robot.insertItems(4)
    }

    @Test
    fun testUIAppears() {
        robot.checkCartIsVisible()
    }

    @Test
    fun test4ElementsAdded() {
        robot.openCart()

        //we check the number of elements
        robot.checkNumberOfItemsInList(4)

        //we check the total amount after the 8 items have been added
        robot.checkTotalAmountIs("12,424 kr")

        //we check the badge on the cart menu action
        robot.checkCartIconBadgeText("4")
    }

    @Test
    fun testOrderCanBeSent() {
        robot.apply {
            openCart()
            checkout()
            checkSuccessMessageIsVisible()

            //we are back on the product list after the order has been sent
            checkCartIsClosed()

            //we check the total amount in the cart is 0 and the cart is empty
            openCart()
            checkNumberOfItemsInList(0)
            checkTotalAmountIs("0 kr")
        }
    }

    @Test
    fun testItemCanBeRemoved() {
        robot.apply {
            openCart()
            checkNumberOfItemsInList(4)
            //we remove one item and check the remaining items count
            removeFirstItemFromCart()
            checkNumberOfItemsInList(3)
            //we remove 2 more and check the number of items
            removeFirstItemFromCart()
            removeFirstItemFromCart()
            checkNumberOfItemsInList(1)
        }
    }

    @Test
    fun testItemCanBeRemovedBySwipingLeftOrRight() {
        robot.apply {
            openCart()
            checkNumberOfItemsInList(4)
            //we remove one item and check the remaining items count
            swipeLeftToDelete(1)
            //we check the snackbar is visible and the item is removed
            checkUndoMessageIsVisible()
            checkNumberOfItemsInList(3)

            swipeRightToDelete(1)
            checkUndoMessageIsVisible()
            checkNumberOfItemsInList(2)
        }
    }

    @Test
    fun testDeleteUndoWorks() {
        robot.apply {
            openCart()
            checkNumberOfItemsInList(4)
            //we remove one item and check the remaining items count
            swipeLeftToDelete(1)
            checkUndoMessageIsVisible()
            checkNumberOfItemsInList(3)
            pressUndoAction()
            Thread.sleep(500)
            checkNumberOfItemsInList(4)
        }
    }
}