package com.example.ministore.ui.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ministore.R
import com.example.ministore.databinding.FragmentProductsBinding
import com.example.ministore.ui.ProductRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment: Fragment(R.layout.fragment_products)  {
    private lateinit var binding: FragmentProductsBinding
    private val viewModel: ProductsViewModel by viewModels(ownerProducer = { this })

    private val productRecyclerAdapter = ProductRecyclerAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
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
        viewModel.apply {
            liveDataProductList.observe(viewLifecycleOwner) {
                productRecyclerAdapter.updateList(it)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                binding.recycler.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
    }

}