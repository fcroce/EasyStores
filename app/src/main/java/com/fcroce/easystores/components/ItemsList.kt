package com.fcroce.easystores.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fcroce.easystores.R
import com.fcroce.easystores.data.AppDatabase
import com.fcroce.easystores.data.getDatabase
import com.fcroce.easystores.theme.EasyStoresTheme

data class ItemsListData(
    val sku: String,
    val brand: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

fun removeItemFromGroceryList(db: AppDatabase, storeId: Int, itemSku: String) {
    db.runInCoroutineExecutor {
        try {
            val groceryDao = db.groceryDao()

            groceryDao.deleteItem(itemSku, storeId)
        } catch (error: Error) {
            println(error)
        }
    }
}

@Composable
fun ItemsList(
    items: List<ItemsListData>,
    storeId: Int,
    db: AppDatabase,
    onRemoveItem: (itemSku: String) -> Unit,
    onItemClicked: (item: ItemsListData) -> Unit = {},
) {
    fun removeRemoveItem(itemSku: String) {
        removeItemFromGroceryList(db, storeId, itemSku)
        onRemoveItem(itemSku)
    }

    Column(modifier = Modifier.padding(all = 12.dp)) {
        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Item(item, index == items.size - 1, onClick = {
                    onItemClicked(item)
                })

                Button(
                    shape = CircleShape,
                    onClick = { removeRemoveItem(item.sku) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.add_new_store),
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewItemsList() {
    EasyStoresTheme {
        Surface {
            val db = getDatabase()

            ItemsList(
                items = listOf(
                    ItemsListData("1", "Brand 1", "Item 1", 0.0, 0),
                    ItemsListData("2", "Brand 1", "Item 2", 0.0, 0),
                ),
                storeId = 0,
                db = db,
                onRemoveItem = {}
            )
        }
    }
}