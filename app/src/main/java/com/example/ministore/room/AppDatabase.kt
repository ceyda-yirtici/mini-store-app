package com.example.ministore.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.movieproject.room.CartProduct
import com.example.myapplication.room.CartProductDao

@Database(entities = [CartProduct::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): CartProductDao

}