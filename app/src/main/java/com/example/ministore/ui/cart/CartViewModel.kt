package com.example.ministore.ui.cart

import android.app.Application
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
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val movieService: ProductService, application: Application
)
    : ViewModel() {


    private val productDao: CartProductDao


    val liveDataLoading = MutableLiveData<Boolean>()

    private val _liveDataDaoList = MutableLiveData<List<CartProduct>>()
    val liveDataDaoList : LiveData<List<CartProduct>> = _liveDataDaoList

    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        productDao = database.productDao()
        updateDaoList()
    }

    fun getProductDao(): CartProductDao {
        return productDao
    }

    fun updateDaoList(){

        viewModelScope.launch(Dispatchers.IO) {
            _liveDataDaoList.postValue(productDao.getAll())
        }
    }


}
