package com.squadtechs.markhor.foodapp.customer.activity_food_types

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.squadtechs.markhor.foodapp.R
import com.squadtechs.markhor.foodapp.customer.activity_customer_main.ActivityCustomerMain

class ActivityFoodType : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var firstTab: TextView
    private lateinit var secondTab: TextView
    private lateinit var thirdTab: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_type)
        initView()
        populateViewPager()
    }

    private fun populateViewPager() {
        val list = ArrayList<String>()
        list.add("close")
        list.add("cuisine")
        list.add("deliver")
        viewPager.adapter =
            FoodViewPagerAdapter(list, this, supportFragmentManager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                when (position) {
                    0 -> {
                        firstTab.setBackgroundResource(R.drawable.tab_back_selected)
                        secondTab.setBackgroundResource(R.drawable.tab_back_unselected)
                        thirdTab.setBackgroundResource(R.drawable.tab_back_unselected)
                    }
                    1 -> {
                        firstTab.setBackgroundResource(R.drawable.tab_back_unselected)
                        secondTab.setBackgroundResource(R.drawable.tab_back_selected)
                        thirdTab.setBackgroundResource(R.drawable.tab_back_unselected)
                    }
                    2 -> {
                        firstTab.setBackgroundResource(R.drawable.tab_back_unselected)
                        secondTab.setBackgroundResource(R.drawable.tab_back_unselected)
                        thirdTab.setBackgroundResource(R.drawable.tab_back_selected)
                    }
                }
            }

            override fun onPageSelected(position: Int) {}
        })
    }

    private fun initView() {
        viewPager = findViewById(R.id.food_view_pager)
        firstTab = findViewById(R.id.txt_first_tab)
        secondTab = findViewById(R.id.txt_second_tab)
        thirdTab = findViewById(R.id.txt_third_tab)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityFoodType, ActivityCustomerMain::class.java))
        finish()
    }

}