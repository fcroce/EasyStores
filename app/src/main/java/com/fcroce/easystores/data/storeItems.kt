package com.fcroce.easystores.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(
    tableName = "storeItems",
    indices = [
        Index(value = ["sku", "storeUid"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            Store::class,
            ["uid"],
            ["storeUid"],
            ForeignKey.CASCADE,
            ForeignKey.CASCADE
        )
    ]
)
data class StoreItems(
    @PrimaryKey var sku: String,
    @ColumnInfo(index = true, name = "storeUid") var storeUid: Int,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
)

@Dao
interface StoreItemsDao {
    @Query("SELECT sku, storeUid, brand, name, price FROM storeItems where storeUid = :storeUid")
    suspend fun getAll(storeUid: Int): List<StoreItems>

    @Query("SELECT sku, storeUid, brand, name, price FROM storeItems where storeUid = :storeUid and sku = :itemSku")
    suspend fun getItem(storeUid: Int, itemSku: String): StoreItems?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItems(vararg items: StoreItems)

    @Query("DELETE FROM storeItems where sku = :itemSku and storeUid = :storeUid")
    suspend fun deleteItem(itemSku: String, storeUid: Int)

    @Query("DELETE FROM storeItems where storeUid = :storeUid")
    suspend fun deleteAll(storeUid: Int)
}
