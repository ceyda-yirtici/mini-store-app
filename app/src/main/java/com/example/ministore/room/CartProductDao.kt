package com.example.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movieproject.room.CartProduct

@Dao
interface CartProductDao {

    @Query("SELECT * FROM cart_table")
    fun getAll(): List<CartProduct>



    @Query("SELECT * FROM cart_table WHERE product_id = :productId")
    fun get(productId:String): CartProduct?



    @Query("SELECT COUNT(*) FROM cart_table WHERE product_id = :productId")
    fun getCountById(productId: String): Int

    @Query("UPDATE cart_table SET amount = :newAmount WHERE product_id = :productId")
    fun updateAmount(productId: String, newAmount: Int)

    @Query("SELECT amount FROM cart_table WHERE product_id = :productId")
    fun getAmountById(productId: String): Int

    @Query("SELECT COUNT(*) FROM cart_table")
    fun getCount(): Int

    @Query("SELECT SUM(amount) FROM cart_table")
    fun getSumOfAmounts(): Int

    @Insert
    fun insert(movie: CartProduct)

    @Insert
    fun insertAll(products: List<CartProduct>)
    @Query("DELETE FROM cart_table WHERE product_id = :productId")
    fun deleteById(productId: String)

    @Query("DELETE FROM cart_table")
     fun deleteAllProducts()




}