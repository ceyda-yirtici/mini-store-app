package com.example.ministore.ui.products

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.ministore.service.ProductService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel  @Inject constructor(
    private val movieService: ProductService, application: Application
)
    : ViewModel() {
}
