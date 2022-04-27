package com.erapps.newws.data.models

import com.google.gson.annotations.SerializedName

data class ErrorModel (
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("code"    ) var code    : String? = null,
    @SerializedName("message" ) var message : String? = null
)
