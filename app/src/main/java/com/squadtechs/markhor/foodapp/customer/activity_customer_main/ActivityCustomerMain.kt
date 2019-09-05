package com.squadtechs.markhor.foodapp.customer.activity_customer_main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squadtechs.markhor.foodapp.R
import com.squadtechs.markhor.foodapp.customer.customer_fragment_around_me.CustomerFragmentAroundMe
import com.squadtechs.markhor.foodapp.customer.customer_fragment_cart.CustomerFragmentCart
import com.squadtechs.markhor.foodapp.customer.customer_fragment_home.CustomerFragmentHome
import com.squadtechs.markhor.foodapp.customer.customer_fragment_orders.CustomerFragmentOrders
import com.squadtechs.markhor.foodapp.customer.customer_fragment_profile.CustomerFragmentProfile

class ActivityCustomerMain : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_main)
        initViews()
        setNavigationListener()
    }

    private fun setNavigationListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    private fun initViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation_view)
        changeFragment(CustomerFragmentHome())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_home -> {
                changeFragment(CustomerFragmentHome())
            }
            R.id.item_location -> {
                changeFragment(CustomerFragmentAroundMe())
            }
            R.id.item_cart -> {
                changeFragment(CustomerFragmentCart())
            }
            R.id.item_orders -> {
                changeFragment(CustomerFragmentOrders())
            }
            R.id.item_profile -> {
                changeFragment(CustomerFragmentProfile())
            }
        }
        return true
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.commit()
    }

}