package com.example.moviesapp.ui.models

import com.google.gson.annotations.SerializedName

data class NowPlayingResponse(

    @field:SerializedName("dates")
    var dates: Dates? = null,

    @field:SerializedName("page")
    var page: Int? = null,

    @field:SerializedName("total_pages")
    var totalPages: Int? = null,

    @field:SerializedName("results")
    var nowPlayingList: ArrayList<ItemDetails>? = null,

    @field:SerializedName("total_results")
    var totalResults: Int? = null

) : ItemDetails()

data class Dates(

    @field:SerializedName("maximum")
    var maximum: String? = null,

    @field:SerializedName("minimum")
    var minimum: String? = null
)