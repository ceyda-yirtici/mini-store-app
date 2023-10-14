package com.example.ministore.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ministore.R
import com.example.ministore.databinding.FragmentProductsBinding
import com.example.ministore.databinding.ProductsToolbarCustomLayoutBinding
import com.example.ministore.model.Product
import com.example.ministore.ui.CartManager
import com.example.ministore.ui.ProductRecyclerAdapter
import com.example.ministore.ui.cart.CartFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProductsFragment: Fragment(R.layout.fragment_products)  {
    private lateinit var binding: FragmentProductsBinding
    private lateinit var cartManager: CartManager
    private val viewModel: ProductsViewModel by viewModels(ownerProducer = { this })
    private val productRecyclerAdapter = ProductRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        cartManager = CartManager.getInstance(viewModel.getProductDao())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        listenViewModel()
    }


    private fun initView(view: View) {
        view.apply {

            val layoutManager: LinearLayoutManager? = GridLayoutManager(requireContext(), 3)
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = productRecyclerAdapter

        }
    }

    override fun onStart(){
        super.onStart()
        viewModel.displayGroup()
        listenViewModel()
    }

    private fun listenViewModel() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                cartManager.updateToolbarCartView(binding.toolbar)
            }
        }
        viewModel.apply {
            liveDataProductList.observe(viewLifecycleOwner) {
                productRecyclerAdapter.updateList(it)
            }
            liveDataDaoList.observe(viewLifecycleOwner){
                productRecyclerAdapter.updateCartList(it)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                binding.recycler.visibility = if (it) View.GONE else View.VISIBLE
            }
            binding.toolbar.findViewById<ImageButton>(R.id.cart).setOnClickListener {
                findNavController().navigate(R.id.action_cart)
            }
            productRecyclerAdapter.setOnClickListener(object : ProductRecyclerAdapter.OnClickListener{
                override fun onAddButtonClick(
                    product: Product
                ) {

                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            cartManager.addProductToDB(product, context)
                            viewModel.updateDaoList()
                            cartManager.updateToolbarCartView(binding.toolbar)
                        }
                    }
                }
                override fun onReduceButtonClick(
                    product: Product
                ) {

                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            cartManager.reduceProductFromDB(product)
                            viewModel.updateDaoList()
                            cartManager.updateToolbarCartView(binding.toolbar)
                        }
                    }
                }
            }
            )
        }
    }



}