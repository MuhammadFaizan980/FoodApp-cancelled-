package com.squadtechs.markhor.foodapp.customer.Fragments.customer_fragment_home


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squadtechs.markhor.foodapp.R
import com.squadtechs.markhor.foodapp.customer.activity_customer_main.CustomerFoodFragmetnCallback

class CustomerFragmentHome : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: CustomerFragmentHomeAdapter
    private lateinit var touchView: View
    private lateinit var mCallBack: CustomerFoodFragmetnCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.customer_fragment_home, container, false)
        initViews(view)
        populateRecyclerView(view)
        setListener()
        return view
    }

    private fun setListener() {
        touchView.setOnClickListener {
            mCallBack.onFoodSelected()
        }
    }

    private fun populateRecyclerView(view: View) {
        recyclerView.layoutManager =
            LinearLayoutManager(
                activity!!.applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        recyclerView.adapter = adapter
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recycler_customer_home)
        list = ArrayList()
        list.add("Clothes")
        list.add("Accessories")
        list.add("Skincare")
        list.add("Homeware")
        list.add("Toys")
        list.add("Shoes")
        list.add("Bags")
        list.add("Other")
        adapter =
            CustomerFragmentHomeAdapter(activity!!.applicationContext, list, activity!!)
        touchView = view.findViewById(R.id.touch_view)
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mCallBack = activity!! as CustomerFoodFragmetnCallback
    }

}
