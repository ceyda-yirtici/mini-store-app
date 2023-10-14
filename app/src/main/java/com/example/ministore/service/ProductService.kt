package com.example.ministore.service

import com.example.ministore.model.CheckoutResponse
import com.example.ministore.model.Product
import com.example.ministore.model.RequestProductList
import com.example.movieproject.room.CartProduct
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {

    @GET("/list")
    suspend fun getProducts(): List<Product>

    @POST("/checkout")
    suspend fun checkoutCart(@Body products: RequestProductList) : CheckoutResponse

}

