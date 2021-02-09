package com.ikea.shoppable.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ikea.shoppable.R
import com.ikea.shoppable.persistence.CartRepository
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var navController: NavController
    private val TAG: String = javaClass.simpleName
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var repository: CartRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val cartItem = menu.findItem(R.id.menu_action_cart)
        cartItem.actionView.setOnClickListener { onOptionsItemSelected(cartItem) }
        compositeDisposable.add(
            repository.getSize()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val item = cartItem.actionView.findViewById<TextView>(R.id.tv_menu_action_cart_count)
                    if (it > 0) {
                        item.visibility = View.VISIBLE
                        item.text = "$it"
                    } else {
                        item.visibility = View.GONE
                    }
                }
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_action_cart -> {
                val current = navController.currentDestination
                if (current != null && current.id == R.id.CartFragment) {
                    navController.popBackStack()
                    return true
                }
                navController.navigate(R.id.action_open_cart)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}