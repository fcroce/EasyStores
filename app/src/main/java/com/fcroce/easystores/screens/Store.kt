package com.fcroce.easystores.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcroce.easystores.R
import com.fcroce.easystores.components.AddFloatingButton
import com.fcroce.easystores.components.DropdownList
import com.fcroce.easystores.components.ItemsList
import com.fcroce.easystores.data.AppDatabase
import com.fcroce.easystores.data.Store
import com.fcroce.easystores.data.getDatabase
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun Store(
    openScanItemScreen: () -> Unit,
    db: AppDatabase
) {
    val userDao = db.storeDao()
    val stores: List<Store> = userDao.getAll()
    val storesList = stores.map { store: Store ->
        store.name.toString()
    }
//    val stores = listOf("Dunnes", "Lidl", "Aldi", "IKEA")

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingButton(
                onClick = openScanItemScreen,
                modifier = Modifier.padding(16.dp)
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(paddingValues = contentPadding)
                    .padding(all = 12.dp)
            ) {
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
                        itemList = storesList,
                    )
                }

                Spacer(modifier = Modifier.height(height = 8.dp))

                ItemsList(arrayOf("Item 1", "Item 2"))
            }
        })
}

@Preview
@Composable
fun PreviewStore() {
    EasyStoresTheme {
        Surface {
            val db = getDatabase()
            Store(openScanItemScreen = {}, db)
        }
    }
}