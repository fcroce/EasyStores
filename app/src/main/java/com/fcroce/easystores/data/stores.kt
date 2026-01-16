package com.fcroce.easystores.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class Store(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?
)

@Dao
interface StoreDao {
    @Query("SELECT uid, name FROM store")
    fun getAll(): List<Store>

    @Insert
    fun addStores(vararg stores: Store)

    @Delete
    fun deleteStore(store: Store)
}