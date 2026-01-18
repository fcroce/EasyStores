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
data class Grocery(
    @PrimaryKey var sku: String,
    @ColumnInfo(name = "storeUid") var storeUid: Int,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "quantity", defaultValue = "1") val quantity: Int
)

@Dao
interface GroceryDao {
    @Query("SELECT sku, storeUid, brand, name, price, quantity FROM grocery where storeUid = :storeUid")
    suspend fun getAll(storeUid: Int): List<Grocery>

    @Query("SELECT sku, storeUid, brand, name, price, quantity FROM grocery where storeUid = :storeUid and sku = :itemSku")
    suspend fun getItem(storeUid: Int, itemSku: String): Grocery?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItems(vararg items: Grocery)

    @Query("DELETE FROM grocery where sku = :itemSku and storeUid = :storeUid")
    suspend fun deleteItem(itemSku: String, storeUid: Int)

    @Query("DELETE FROM grocery where storeUid = :storeUid")
    suspend fun deleteAll(storeUid: Int)
}
