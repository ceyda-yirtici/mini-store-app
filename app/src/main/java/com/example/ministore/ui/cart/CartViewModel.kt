package com.example.ministore.ui.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ministore.model.Product
import com.example.ministore.service.ProductService
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.room.CartProduct
import com.example.myapplication.room.CartProductDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productService: ProductService, application: Application
)
    : ViewModel() {


    private val productDao: CartProductDao


    val liveDataLoading = MutableLiveData<Boolean>()

    private val _liveDataDaoList = MutableLiveData<List<CartProduct>>()
    val liveDataDaoList : LiveData<List<CartProduct>> = _liveDataDaoList


    private val _liveDataProductList = MutableLiveData<List<Product>>()
    val liveDataProductList: LiveData<List<Product>> = _liveDataProductList

    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        productDao = database.productDao()
        updateDaoList()
        callProductRepos()
    }

    fun getProductDao(): CartProductDao {
        return productDao
    }

    fun updateDaoList(){
        viewModelScope.launch(Dispatchers.IO) {

            val localProducts = productDao.getAll()
            _liveDataDaoList.postValue(localProducts)

            val productList = liveDataProductList.value?.filter { remoteProduct ->
                localProducts.any { localProduct ->
                    localProduct.product_id == remoteProduct.id
                }
            }
            _liveDataProductList.postValue(productList ?: emptyList())
        }

    }


    private fun callProductRepos() {
        liveDataLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val remoteProducts = productService.getProducts()
                val localProducts = productDao.getAll()

                val productList = remoteProducts.filter { remoteProduct ->
                    localProducts.any { localProduct ->
                        localProduct.product_id == remoteProduct.id
                    }
                }
                Log.d("call", productList.toString())
                _liveDataProductList.postValue(productList)
            } catch (exception: Exception) {
                _liveDataProductList.postValue(emptyList())
            } finally {
                liveDataLoading.postValue(false)
            }
        }
    }






}
