package com.ikea.shoppable.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ikea.shoppable.R
import com.ikea.shoppable.view.common.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: AppBarViewModel
    private var cartItemCount: Int = 0
    private var badgeText: TextView? = null
    private lateinit var navController: NavController
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        viewModel = ViewModelProvider(this, providerFactory)[AppBarViewModel::class.java]
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(viewModel.getCartSize().subscribe {
            if (badgeText != null) {
                setBadgeText(it)
            } else {
                cartItemCount = it
            }
        })
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val cartMenuItem = menu.findItem(R.id.menu_action_cart)
        badgeText = cartMenuItem.actionView.findViewById(R.id.tv_menu_action_cart_count)
        //onCreateOptionsMenu is called after onStart/onResume so we miss out on the observable firing the first time, thus we set the last known value here
        setBadgeText(cartItemCount)
        cartMenuItem.actionView.setOnClickListener {
            onOptionsItemSelected(cartMenuItem)
        }
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

    private fun setBadgeText(count: Int) {
        if (count > 0) {
            badgeText?.visibility = View.VISIBLE
            badgeText?.text = "$count"
        } else {
            badgeText?.visibility = View.GONE
        }
    }
}