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
    tableName = "grocery",
    indices = [
        Index(value = ["storeUid", "storeItemSku"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            StoreItems::class,
            ["storeUid", "sku"],
            ["storeUid", "storeItemSku"],
            ForeignKey.CASCADE,
            ForeignKey.CASCADE
        )
    ]
)
data class Grocery(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "storeUid") var storeUid: Int,
    @ColumnInfo(name = "storeItemSku") val storeItemSku: String,
    @ColumnInfo(name = "quantity", defaultValue = "1") val quantity: Int
)

data class GroceryItems(
    val sku: String,
    val storeUid: Int,
    val brand: String,
    val name: String,
    val price: Double,
    val quantity: Int,
)

@Dao
interface GroceryDao {
    @Query("SELECT si.sku, si.storeUid, si.brand, si.name, si.price, g.quantity FROM grocery as g join storeItems as si where si.storeUid = :storeUid")
    suspend fun getAll(storeUid: Int): List<GroceryItems>

    @Query("SELECT si.sku, si.storeUid, si.brand, si.name, si.price, g.quantity FROM grocery as g join storeItems as si where si.storeUid = :storeUid and sku = :itemSku")
    suspend fun getItem(storeUid: Int, itemSku: String): GroceryItems?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItems(vararg items: Grocery)

    @Query("DELETE FROM grocery where storeItemSku = :itemSku and storeUid = :storeUid")
    suspend fun deleteItem(itemSku: String, storeUid: Int)

    @Query("DELETE FROM grocery where storeUid = :storeUid")
    suspend fun deleteAll(storeUid: Int)
}
