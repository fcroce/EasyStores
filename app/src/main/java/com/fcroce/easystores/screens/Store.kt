package com.fcroce.easystores.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcroce.easystores.R
import com.fcroce.easystores.components.AddFloatingButton
import com.fcroce.easystores.components.DropdownList
import com.fcroce.easystores.components.DropdownListData
import com.fcroce.easystores.components.ItemsList
import com.fcroce.easystores.components.ItemsListData
import com.fcroce.easystores.data.AppDatabase
import com.fcroce.easystores.data.Grocery
import com.fcroce.easystores.data.Store
import com.fcroce.easystores.data.getDatabase
import com.fcroce.easystores.theme.EasyStoresTheme
import java.util.Locale

fun getStoresFromDb(
    db: AppDatabase,
    onLoaded: (List<DropdownListData>) -> Unit,
) {
    db.runInCoroutineExecutor {
        val storeDao = db.storeDao()
        val stores: List<Store> = storeDao.getAll()
        val storesList = stores.map { store: Store ->
            DropdownListData(store.uid, store.name)
        }

        onLoaded(storesList)
    }
}

fun getStoreGroceryFromDb(
    db: AppDatabase,
    selectedStoreId: Int,
    onLoaded: (List<ItemsListData>) -> Unit,
) {
    db.runInCoroutineExecutor {
        val groceryDao = db.groceryDao()
        val grocery: List<Grocery> = groceryDao.getAll(selectedStoreId)
        val groceryList = grocery.map { item: Grocery ->
            ItemsListData(
                sku = item.sku,
                brand = item.brand,
                name = item.name,
                price = item.price,
                quantity = item.quantity
            )
        }

        onLoaded(groceryList)
    }
}

fun emptyGroceryList(db: AppDatabase, selectedStoreId: Int) {
    db.runInCoroutineExecutor {
        val groceryDao = db.groceryDao()

        groceryDao.deleteAll(selectedStoreId)
    }
}

@Composable
fun Store(
    openScanItemScreen: (itemSku: String) -> Unit,
    db: AppDatabase,
    setStoreId: (storeId: Int) -> Unit,
) {
    var selectedDropdownStoreId by remember { mutableStateOf(0) }
    var selectedStoreId by remember { mutableStateOf(0) }
    var storesList by remember { mutableStateOf<List<DropdownListData>>(emptyList()) }
    var groceryList by remember { mutableStateOf<MutableList<ItemsListData>>(mutableListOf()) }

    getStoresFromDb(db, onLoaded = { list ->
        storesList = list
        selectedStoreId = list.firstOrNull()?.id ?: 0

        setStoreId(selectedStoreId)
    })

    LaunchedEffect(storesList) {
        if (storesList.isNotEmpty()) {
            getStoreGroceryFromDb(db, selectedStoreId, onLoaded = { list ->
                groceryList = list.toMutableList()
            })
        }
    }

    fun onSelectStore(listPosition: Int, store: DropdownListData) {
        selectedDropdownStoreId = listPosition
        selectedStoreId = store.id

        getStoreGroceryFromDb(db, store.id, onLoaded = { list ->
            groceryList = list.toMutableList()
        })

        setStoreId(store.id)
    }

    fun onClearGroceryList() {
        emptyGroceryList(db, selectedStoreId)

        groceryList = mutableListOf()
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingButton(
                onClick = { openScanItemScreen("") },
                modifier = Modifier.padding(16.dp)
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(paddingValues = contentPadding)
                    .padding(all = 12.dp)
            ) {
                if (storesList.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.store_name),
                            fontSize = 30.sp
                        )

                        DropdownList(
                            fontSize = 30.sp,
                            selectedIndex = selectedDropdownStoreId,
                            itemList = storesList,
                            onItemClick = { listPosition, store ->
                                onSelectStore(listPosition, store)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(height = 8.dp))

                Button(onClick = { onClearGroceryList() }) {
                    Text(stringResource(R.string.scan_item_reset))
                }

                Spacer(modifier = Modifier.height(height = 8.dp))

                if (groceryList.isNotEmpty()) {
                    ItemsList(
                        groceryList,
                        selectedStoreId,
                        db,
                        onRemoveItem = { sku ->
                            groceryList = groceryList.filter { it.sku != sku }.toMutableList()
                        },
                        onItemClicked = { item ->
                            openScanItemScreen(item.sku)
                        },
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.store_total))

                        Text(
                            (String.format(
                                Locale.ENGLISH, "%.2f",
                                groceryList.sumOf { it.price * it.quantity }
                            )))
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(stringResource(R.string.scan_item_empty_list))
                    }
                }
            }
        })
}

@Preview
@Composable
fun PreviewStore() {
    EasyStoresTheme {
        Surface {
            val db = getDatabase()
            Store(
                openScanItemScreen = {},
                db = db,
                setStoreId = {},
            )
        }
    }
}