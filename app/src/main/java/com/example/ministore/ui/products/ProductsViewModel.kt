package com.example.ministore.ui.products

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ministore.model.Product
import com.example.ministore.service.ProductService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel  @Inject constructor(
    private val movieService: ProductService, application: Application
)
    : ViewModel() {

    private val _liveDataProductList = MutableLiveData<List<Product>>()
    val liveDataProductList: LiveData<List<Product>> = _liveDataProductList

    val liveDataLoading = MutableLiveData<Boolean>()


    init {
        callProductRepos()
    }




    private fun callProductRepos() {
        liveDataLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {

            val productList = try {
                movieService.getProducts()
            } catch (exception: Exception) {
                emptyList()
            }

            _liveDataProductList.postValue(productList)
            liveDataLoading.postValue(false)
        }
    }


    fun displayGroup() {
        callProductRepos()
    }

}
