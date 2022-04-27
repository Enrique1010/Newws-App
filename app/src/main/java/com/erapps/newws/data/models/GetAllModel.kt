package com.erapps.newws.data.models

import com.google.gson.annotations.SerializedName

data class GetAllModel<T>(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val data: List<T>
)
