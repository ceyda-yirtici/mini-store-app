package com.example.ministore.ui.cart

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.ministore.service.ProductService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val movieService: ProductService, application: Application
)
    : ViewModel() {

}
