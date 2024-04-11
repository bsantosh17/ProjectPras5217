package com.example.cloudcatch.ui.fragments.homeFragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudcatch.R
import com.example.cloudcatch.ui.fragments.homeFragment.model.DashboardData

//Adapter for Dashboard
class AdapterDashboard(val context: Context, val dataList: ArrayList<DashboardData>) :
    RecyclerView.Adapter<AdapterDashboard.SpanDownViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpanDownViewHolder {
       val itemView =LayoutInflater.from(parent.context).inflate(R.layout.custom_item_dashboard_desk,parent,false)
        return SpanDownViewHolder(itemView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SpanDownViewHolder, position: Int) {
        val dashboardItem =dataList[position]

        holder.txtCount.text = dashboardItem.count
        holder.txtTitle.text = dashboardItem.title
        //holder.txtTitle.setTextColor(Color.parseColor(dashboardItem.background_color))
        holder.linearLayout.setBackgroundColor(Color.parseColor(dashboardItem.background_color))


    }



    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class SpanDownViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val txtCount:TextView = itemView.findViewById(R.id.txtCount)
        val txtTitle:TextView = itemView.findViewById(R.id.txtTitle)
        val linearLayout:LinearLayout = itemView.findViewById(R.id.linearLayout)

    }
}

