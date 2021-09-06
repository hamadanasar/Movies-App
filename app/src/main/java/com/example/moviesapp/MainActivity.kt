package com.example.moviesapp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.moviesapp.progress.ProgressHandle
import com.example.moviesapp.services.ConnectivityReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ProgressHandle,
    NavController.OnDestinationChangedListener, ConnectivityReceiver.ConnectivityReceiverListner {

    lateinit var navController: NavController
    lateinit var bottomNav: BottomNavigationView

    var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bottom Navigation + Navigation Component
        setBottomNavWithNavComponent()

        // initialize Snack bar
        initSnackBar()

        // initialize Broadcast Receiver
        initBroadcastReceiver()
    }

    private fun initSnackBar() {
        snackbar = Snackbar.make(
            findViewById(R.id.root),
            getString(R.string.checkConnection), Snackbar.LENGTH_INDEFINITE
        )
    }

    private fun initBroadcastReceiver() {
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        ConnectivityReceiver.connectivityReceiverListner = this
    }

    private fun setBottomNavWithNavComponent() {
        bottomNav = findViewById(R.id.bottomNav)

        bottomNav.inflateMenu(R.menu.user_menu)

        val navHostFrag =
            supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment
        navController = navHostFrag.navController
        navController.setGraph(R.navigation.main_nav, intent.extras)

        //setupActionBarWithNavController(navController)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.nowPlayingFragment -> {
                showBottomNav()
            }
            R.id.topRatedFragment -> {
                showBottomNav()
            }
            R.id.favouriteFragment -> {
                showBottomNav()
            }
            R.id.movieDetailsFragment -> {
                hideBottomNav()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomNav.visibility = View.GONE
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackbar!!.show()
        } else if (isConnected) {
            if (snackbar!!.isShown) {
                snackbar!!.dismiss()
            }
        }
    }
}