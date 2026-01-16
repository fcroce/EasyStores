package com.fcroce.easystores.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Store::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
}

@Composable
fun getDatabase(): AppDatabase {
    return Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java, "storesDB"
    ).build()
}