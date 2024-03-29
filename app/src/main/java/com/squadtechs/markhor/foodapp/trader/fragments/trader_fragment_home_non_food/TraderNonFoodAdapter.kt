package com.squadtechs.markhor.foodapp.trader.fragments.trader_fragment_home_non_food

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squadtechs.markhor.foodapp.CONSTANTS
import com.squadtechs.markhor.foodapp.R
import com.squareup.picasso.Picasso

class TraderNonFoodAdapter(
    private val list: ArrayList<TraderNonFoodModel>,
    private val context: Context,
    private val mView: TraderNonFoodCallBack
) : RecyclerView.Adapter<TraderNonFoodAdapter.NonFoodViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NonFoodViewHolder =
        NonFoodViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.trader_non_food_home_row_design,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: NonFoodViewHolder, position: Int) {
        val obj = list[position]
        Picasso.get().load("${CONSTANTS.imgPre}${obj.image_path}")
            .into(holder.imgItem)
        adjustScreen(holder, position)

        holder.touchView.setOnClickListener {
            val pref = context.getSharedPreferences("add_item_preferences", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("is_edit", true)
            editor.putString("item_id", obj.id)
            editor.apply()
            mView.onEditTapped()
        }

    }

    private fun adjustScreen(holder: NonFoodViewHolder, position: Int) {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        holder.frame.layoutParams = FrameLayout.LayoutParams((width / 2), ((27 * height) / 100))
        if (position % 2 == 0) {
            holder.frame.setPadding(64, 32, 32, 32)
        } else {
            holder.frame.setPadding(32, 32, 64, 21)
        }
    }

    inner class NonFoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgItem: ImageView = view.findViewById(R.id.img_item)
        val frame: FrameLayout = view.findViewById(R.id.main_frame)
        val touchView: View = view.findViewById(R.id.touch_view)
    }
}