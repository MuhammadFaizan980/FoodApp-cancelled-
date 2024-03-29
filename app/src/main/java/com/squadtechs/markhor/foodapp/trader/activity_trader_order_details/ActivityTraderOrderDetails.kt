package com.squadtechs.markhor.foodapp.trader.activity_trader_order_details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squadtechs.markhor.foodapp.R
import com.squadtechs.markhor.foodapp.SingletonQueue
import com.squadtechs.markhor.foodapp.main_utils.MainUtils

class ActivityTraderOrderDetails : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TraderOrderDetailsAdapter
    private lateinit var list: ArrayList<TraderOrderDetailsModel>
    private lateinit var txtPrice: TextView
    private lateinit var txtDeliveryPrice: TextView
    private lateinit var txtAllergyRequest: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtDeliverTo: TextView
    private lateinit var txtBeingPrepared: TextView
    private lateinit var txtOnTheWay: TextView
    private lateinit var txtReady: TextView
    private lateinit var linearUpdateStatus: LinearLayout
    private lateinit var linearUpdate: LinearLayout
    private var totalPrice: Int = 0
    private var totalDeliveryPrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader_order_details)
        initViews()
        populateRecyclerView()
        setTxtListeners()
    }

    private fun setTxtListeners() {
        txtBeingPrepared.setOnClickListener {
            txtBeingPrepared.setBackgroundResource(R.drawable.txt_edit_range_unselected)
            txtReady.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtOnTheWay.setBackgroundResource(R.drawable.txt_edit_range_selected)
            updateStatus("being prepared")
        }
        txtOnTheWay.setOnClickListener {
            txtBeingPrepared.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtReady.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtOnTheWay.setBackgroundResource(R.drawable.txt_edit_range_unselected)
            updateStatus("on the way")
        }
        txtReady.setOnClickListener {
            txtBeingPrepared.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtReady.setBackgroundResource(R.drawable.txt_edit_range_unselected)
            txtOnTheWay.setBackgroundResource(R.drawable.txt_edit_range_selected)
            updateStatus("ready")
        }
    }

    private fun updateStatus(status: String) {
        val pd = MainUtils.getLoadingDialog(this, "Updating", "Please wait", false)
        pd.show()
        val API = "http://squadtechsolution.com/android/v1/update_order_status.php"
        val map = HashMap<String, String>()
        map["company_id"] =
            getSharedPreferences("user_credentials", Context.MODE_PRIVATE).getString(
                "company_id",
                "none"
            )!!
        map["customer_id"] = intent!!.extras!!.getString("customer_id")!!
        map["status"] = status
        val requestQueue = SingletonQueue.getRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.POST,
            API,
            Response.Listener { response ->
                pd.cancel()
                Log.i("update_response", response)
            },
            Response.ErrorListener { error ->
                pd.cancel()
                Log.i("update_response", error.toString())
            }) {
            override fun getParams(): MutableMap<String, String> = map
        }
        requestQueue.add(stringRequest)
    }

    private fun populateRecyclerView() {
        val map = HashMap<String, String>()
        map["company_id"] =
            getSharedPreferences("user_credentials", Context.MODE_PRIVATE).getString(
                "company_id",
                "none"
            )!!
        map["customer_id"] = intent!!.extras!!.getString("customer_id")!!
        val API = "http://squadtechsolution.com/android/v1/get_all_order_for_trader.php"
        when (intent!!.extras!!.getInt("position")) {
            0 -> {
                map["is_complete"] = ""
            }
            1 -> {
                map["is_complete"] = "yes"
                linearUpdate.visibility = View.GONE
                linearUpdateStatus.visibility = View.GONE
                txtDeliverTo.visibility = View.GONE
                txtAddress.visibility = View.GONE
                txtAllergyRequest.visibility = View.GONE
            }
        }
        val pd = MainUtils.getLoadingDialog(this, "Loading", "Please wait", false)
        pd.cancel()
        val requestQueue = SingletonQueue.getRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.POST,
            API,
            Response.Listener { response ->
                pd.cancel()
                Log.i("customer_order_response", response)
                try {
                    val type = object : TypeToken<ArrayList<TraderOrderDetailsModel>>() {}.type
                    list = Gson().fromJson(response, type)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    adapter = TraderOrderDetailsAdapter(list, this)
                    recyclerView.adapter = adapter
                    calculateTotalPrice()
                } catch (exc: Exception) {
                    Log.i("customer_exception", exc.toString())
                }
            },
            Response.ErrorListener { error ->
                pd.cancel()
                Log.i("customer_order_response", error.toString())
            }) {

            override fun getParams(): MutableMap<String, String> = map
        }
        requestQueue.add(stringRequest)
    }

    private fun calculateTotalPrice() {
        for (i in list) {
            totalPrice += (i.itemPrice.toInt() * i.itemPrice.toInt())
        }
        txtPrice.text = "AED $totalPrice"
        calculateDeliveryPrice()
    }

    private fun calculateDeliveryPrice() {
        var count = 0
        for (i in list) {
            if (!i.deliveryPrice.equals("") && i.deliveryPrice != null) {
                totalDeliveryPrice += i.deliveryPrice.toInt()
                count++
            } else {
                continue
            }
        }
        if (count != 0)
            txtDeliveryPrice.text = "AED ${totalDeliveryPrice / count}"
        populateAddressAndAllergy()
    }

    private fun populateAddressAndAllergy() {
        txtAddress.text = list[0].address
        txtAllergyRequest.text = "Allergy request: ${list[0].request}"
        if (list[0].request.equals("")) {
            txtAllergyRequest.visibility = View.GONE
        }
        setTxtStatus()
    }

    private fun setTxtStatus() {
        if (list[0].status.equals("pending")) {
            txtBeingPrepared.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtOnTheWay.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtReady.setBackgroundResource(R.drawable.txt_edit_range_selected)
        } else if (list[0].status.equals("being prepared")) {
            txtBeingPrepared.setBackgroundResource(R.drawable.txt_edit_range_unselected)
            txtOnTheWay.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtReady.setBackgroundResource(R.drawable.txt_edit_range_selected)
        } else if (list[0].status.equals("on the way")) {
            txtBeingPrepared.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtOnTheWay.setBackgroundResource(R.drawable.txt_edit_range_unselected)
            txtReady.setBackgroundResource(R.drawable.txt_edit_range_selected)
        } else {
            txtBeingPrepared.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtOnTheWay.setBackgroundResource(R.drawable.txt_edit_range_selected)
            txtReady.setBackgroundResource(R.drawable.txt_edit_range_unselected)
        }
    }

    private fun initViews() {
        txtDeliverTo = findViewById(R.id.txt_deliver_to)
        linearUpdateStatus = findViewById(R.id.linear_update_status)
        linearUpdate = findViewById(R.id.linear_status)
        txtBeingPrepared = findViewById(R.id.txt_being_prepared)
        txtOnTheWay = findViewById(R.id.txt_on_the_way)
        txtReady = findViewById(R.id.txt_ready)
        txtBeingPrepared = findViewById(R.id.txt_being_prepared)
        txtAddress = findViewById(R.id.txt_address)
        txtAllergyRequest = findViewById(R.id.txt_allergy)
        recyclerView = findViewById(R.id.order_list)
        txtPrice = findViewById(R.id.txt_total)
        txtDeliveryPrice = findViewById(R.id.txt_delivery_total)
    }

}
