package com.example.ministore.model

import com.google.gson.annotations.SerializedName

data class Product (

    @SerializedName("currency") val currency: String,
    @SerializedName("id")  val id: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int,

        )