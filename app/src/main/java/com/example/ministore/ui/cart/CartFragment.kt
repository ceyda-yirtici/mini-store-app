package com.example.ministore.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ministore.R
import com.example.ministore.databinding.FragmentCartBinding
import com.example.ministore.model.Product
import com.example.ministore.ui.CartManager
import com.example.ministore.ui.ProductRecyclerAdapter
import com.example.ministore.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CartFragment: Fragment(R.layout.fragment_cart)  {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartManager: CartManager // dao add/remove operations manager
    private val viewModel: CartViewModel by viewModels(ownerProducer = { this })
    private val cartRecyclerAdapter = ProductRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
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
            val layoutManager = LinearLayoutManager(requireContext())
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = cartRecyclerAdapter
            // same adapter was used for different pages
            cartRecyclerAdapter.updatePageNo(Constants.CART_PAGE)
        }
    }
    override fun onStart(){
        super.onStart()
        viewModel.updateDaoList()
        listenViewModel()
    }
    private fun listenViewModel() {
        viewModel.apply {
            liveDataProductList.observe(viewLifecycleOwner){
                cartRecyclerAdapter.updateList(it) //List of features of dao's products

                // controlling visibilities depends on if dao is empty or not
                binding.placeholder.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
                binding.purchaseLayout.payButton.isEnabled = it.isNotEmpty()
                binding.toolbarLayout.delete.isEnabled = it.isNotEmpty()
            }
            liveDataDaoList.observe(viewLifecycleOwner){
                cartRecyclerAdapter.updateCartList(it)  //List of dao's products and amounts
            }
            liveDataSum.observe(viewLifecycleOwner){
                binding.purchaseLayout.price.text = it
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                binding.recycler.visibility = if (it) View.GONE else View.VISIBLE
            }
            binding.toolbarLayout.close.setOnClickListener {// cart page close button listener
                findNavController().popBackStack()
            }
            binding.toolbarLayout.delete.setOnClickListener{// cart page delete button listener
                showDeleteButtonDialog()
            }
            binding.purchaseLayout.payButton.setOnClickListener {// cart page payment button listener
                checkoutCart()
                paymentDialog()

            }
            cartRecyclerAdapter.setOnClickListener(object : ProductRecyclerAdapter.OnClickListener{
                override fun onAddButtonClick(
                    product: Product
                ) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            cartManager.addProductToDB(product, context)
                            updateDaoList()
                        }
                    }
                }
                override fun onReduceButtonClick(
                    product: Product
                ) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            cartManager.reduceProductFromDB(product)
                            updateDaoList()
                        }
                    }
                }
            }
            )
        }
    }


    // The user is asked again when deleting the cart.
    private fun showDeleteButtonDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val deleteDialog = inflater.inflate(R.layout.dialog_delete_button, null)

        builder.setView(deleteDialog)
            .setPositiveButton(Constants.DIALOG_YES) { dialog, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        cartManager.deleteDao()
                        viewModel.updateDaoList()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(Constants.DIALOG_CANCEL) { dialog, _ ->
                dialog.dismiss()
            }
        CoroutineScope(Dispatchers.Main).launch {
            builder.show()
        }


    }

    //The response message returned from the API is printed on the screen.
    private fun paymentDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val paymentDialog = inflater.inflate(R.layout.dialog_payment, null)
        val dialogText = paymentDialog.findViewById<TextView>(R.id.dialog)
        viewModel.liveDataResponse.observe(viewLifecycleOwner){
            dialogText.text = it
            if(it.contains(Constants.API_SUCCESS_CONDITION)) {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        cartManager.deleteDao()
                        viewModel.updateDaoList()
                    }
                }
            }
        }
        builder.setView(paymentDialog)
            .setPositiveButton(Constants.DIOLOG_OK) { dialog, _ ->
                dialog.dismiss()
            }
        CoroutineScope(Dispatchers.Main).launch {
            builder.show()
        }
    }


}