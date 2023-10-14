package com.example.ministore.ui.cart

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ministore.R
import com.example.ministore.databinding.FragmentCartBinding
import com.example.ministore.databinding.FragmentProductsBinding
import com.example.ministore.ui.CartManager
import com.example.ministore.ui.ProductRecyclerAdapter
import com.example.ministore.ui.products.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment: Fragment(R.layout.fragment_cart)  {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartManager: CartManager
    private val viewModel: CartViewModel by viewModels(ownerProducer = { this })
    private val cartRecyclerAdapter = ProductRecyclerAdapter()
}