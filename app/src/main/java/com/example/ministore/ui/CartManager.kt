package com.example.ministore.ui

import android.util.Log
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import com.example.ministore.R
import com.example.ministore.databinding.ProductsToolbarCustomLayoutBinding
import com.example.ministore.model.Product
import com.example.ministore.util.Constants
import com.example.movieproject.room.CartProduct
import com.example.myapplication.room.CartProductDao
import javax.inject.Singleton

@Singleton
class CartManager private constructor(private val productDao: CartProductDao) {
    private lateinit var toast: Toast
    companion object {
        @Volatile
        private var instance: CartManager? = null

        fun getInstance(productDao: CartProductDao): CartManager {
            return instance ?: synchronized(this) {
                instance ?: CartManager(productDao).also { instance = it }
            }
        }// singleton cart manager
    }

    fun updateToolbarCartView(toolbar: ProductsToolbarCustomLayoutBinding) {
        val cartAmountIcon = toolbar.amountOfAllProducts
        val count = productDao.getSumOfAmounts()
        cartAmountIcon.text = count.toString()
    }
    fun addProductToDB(
        product: Product,
        context: Context?,
    ) {
        var currentAmount = productDao.getAmountById(product.id)
        if (product.stock > currentAmount) currentAmount += 1
        else{
            Handler(Looper.getMainLooper()).post {
                if (this::toast.isInitialized)
                {
                    toast.cancel()
                }
                toast = Toast.makeText(context, Constants.CUSTOM_TOAST_MESSAGE, Toast.LENGTH_SHORT)
                toast.show()
            }
            return
        }
        if(currentAmount == 1) {
            val cartProduct =
                CartProduct(product_id = product.id, amount = currentAmount)
            productDao.insert(cartProduct)
        }
        else{
            productDao.updateAmount(productId = product.id, newAmount = currentAmount)
        }

    }


    fun reduceProductFromDB(
        product :Product,
    ) {
        var currentAmount = productDao.getAmountById(product.id)
        currentAmount -= 1
        if (currentAmount == 0) productDao.deleteById(productId = product.id)
        else productDao.updateAmount(productId = product.id, newAmount = currentAmount)



    }

    fun deleteDao() {
        productDao.deleteAllProducts()
    }

}