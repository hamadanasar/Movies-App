package com.example.moviesapp.ui.models

import com.google.gson.annotations.SerializedName

data class TopRatedResponse(

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,

    @field:SerializedName("results")
    val TopRatedList: ArrayList<ItemDetails>? = null,

    @field:SerializedName("total_results")
    val totalResults: Int? = null

) : ItemDetails()