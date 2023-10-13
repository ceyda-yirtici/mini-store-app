package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movieproject.room.CartProduct

@Dao
interface CartProductDao {

    @Query("SELECT * FROM cart_table")
    fun getAll(): List<CartProduct>


    @Query("SELECT product_id FROM cart_table")
    fun getAllByIds(): List<String>

    @Query("SELECT * FROM cart_table WHERE product_id = :productId")
    fun get(productId:Int): CartProduct


    @Query("SELECT * FROM cart_table WHERE product_id IN (:productIds)")
    suspend fun loadAllByIds(productIds: IntArray): List<CartProduct>

    @Query("SELECT COUNT(*) FROM cart_table WHERE product_id = :productId")
    fun getCountById(productId: Int): Int

    @Query("SELECT COUNT(*) FROM cart_table")
    fun getCount(): Int

    @Insert
    fun insert(movie: CartProduct)

    @Insert
    fun insertAll(products: List<CartProduct>)

    @Delete
    fun delete(product: CartProduct)

    @Query("DELETE FROM cart_table")
     fun deleteAllProducts()




}