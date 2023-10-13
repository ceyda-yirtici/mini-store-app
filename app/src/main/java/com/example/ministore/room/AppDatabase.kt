package com.example.ministore.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieproject.room.CartProduct
import com.example.myapplication.room.CartProductDao

@Database(entities = [CartProduct::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): CartProductDao
}