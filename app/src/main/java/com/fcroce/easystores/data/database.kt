package com.fcroce.easystores.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [Store::class, Grocery::class, StoreItems::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
    abstract fun groceryDao(): GroceryDao
    abstract fun storeItemsDao(): StoreItemsDao

    fun runInCoroutineExecutor(runFun: suspend () -> Unit) {
        val executor = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        val scope = CoroutineScope(executor)
        scope.launch(executor) {
            runFun()
        }
    }
}

@Composable
fun getDatabase(): AppDatabase {
    return Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java, "storesDB"
    )
        .fallbackToDestructiveMigration(dropAllTables = true) // @TODO - Temporary
        .build()
}