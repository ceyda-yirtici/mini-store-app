package com.example.ministore.ui.cart

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ministore.model.CheckoutResponse
import com.example.ministore.model.Product
import com.example.ministore.model.RequestProduct
import com.example.ministore.model.RequestProductList
import com.example.ministore.service.ProductService
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.room.CartProduct
import com.example.myapplication.room.CartProductDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale
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


    private val _liveDataSum = MutableLiveData<String>()
    val liveDataSum: LiveData<String> = _liveDataSum

    private val _liveDataResponse = MutableLiveData<String>()
    val liveDataResponse: LiveData<String> = _liveDataResponse

    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        productDao = database.productDao()

        callProductRepos()
        updateDaoList()
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
            updateSumOfCart()
        }
    }

     @SuppressLint("SuspiciousIndentation")
     fun updateSumOfCart()  {
        var sum = 0.0
            viewModelScope.launch(Dispatchers.IO) {
            for (product in getProductDao().getAll()) {
                val price =
                    liveDataProductList.value?.find { it.id == product.product_id }?.price ?: 0.0
                sum += price*product.amount
            }
            _liveDataSum.postValue(String.format(Locale.US, "%.2f", sum))

        }
    }

    fun checkoutCart(){

        viewModelScope.launch(Dispatchers.IO) {
            val requestProducts = getProductDao().getAll().map { product ->
                RequestProduct(
                    id = product.product_id,
                    amount = product.amount
                )
            }
            val requestBody = RequestProductList(products = requestProducts)
            Log.e("check", getProductDao().getAll().toString())
            _liveDataResponse.postValue(productService.checkoutCart(requestBody).message)
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
                updateSumOfCart()
            } catch (exception: Exception) {
                _liveDataProductList.postValue(emptyList())
            } finally {
                liveDataLoading.postValue(false)
            }
        }
    }

}
