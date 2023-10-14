package com.example.ministore.model

import com.google.gson.annotations.SerializedName

data class CheckoutResponse (

    @SerializedName("orderID") val orderID: String,
    @SerializedName("message")  val message: String,
)