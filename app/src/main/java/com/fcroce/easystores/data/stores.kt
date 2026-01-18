package com.fcroce.easystores.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(
    tableName = "store", indices = [
        Index(value = ["uid", "name"], unique = true)
    ]
)
data class Store(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "name") val name: String
)

@Dao
interface StoreDao {
    @Query("SELECT count(uid) FROM store")
    suspend fun count(): Int

    @Query("SELECT uid, name FROM store")
    suspend fun getAll(): List<Store>

    @Insert
    suspend fun addStores(vararg stores: Store)

    @Delete
    suspend fun deleteStore(store: Store)
}
