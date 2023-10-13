package com.example.ministore.service

import com.example.ministore.model.Product
import retrofit2.http.GET

interface ProductService {

    @GET("/list")
    suspend fun getProducts(): List<Product>

}

