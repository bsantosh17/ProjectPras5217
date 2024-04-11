package com.example.cloudcatch.ui.fragments.homeFragment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DashboardData(
    @SerializedName("background-color")
    @Expose
    val background_color: String,
    @SerializedName("count")
    @Expose
    val count: String,
    @SerializedName("title")
    @Expose
    val title: String

)