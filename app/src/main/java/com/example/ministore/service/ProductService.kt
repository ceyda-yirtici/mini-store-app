package com.example.ministore.service

import com.example.ministore.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("")
    suspend fun getProduct(@Path("id") id: Int): Product

}

