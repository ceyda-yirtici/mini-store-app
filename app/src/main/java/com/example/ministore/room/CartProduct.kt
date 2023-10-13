package com.example.movieproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartProduct(
    @PrimaryKey
    @ColumnInfo(name = "product_id") val product_id: Int,
    @ColumnInfo(name = "amount") val amount: Int,
)
