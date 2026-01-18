package com.fcroce.easystores.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcroce.easystores.R
import com.fcroce.easystores.components.ItemsListData
import com.fcroce.easystores.components.camera.BarcodeScannerAnalysis
import com.fcroce.easystores.data.AppDatabase
import com.fcroce.easystores.data.Grocery
import com.fcroce.easystores.data.StoreItems
import com.fcroce.easystores.data.getDatabase
import com.fcroce.easystores.theme.EasyStoresTheme

fun addItemToGroceryList(db: AppDatabase, storeId: Int, item: ItemsListData) {
    db.runInCoroutineExecutor {
        try {
            val storeItemsDao = db.storeItemsDao()
            val groceryDao = db.groceryDao()

            storeItemsDao.addItems(
                StoreItems(
                    sku = item.sku,
                    storeUid = storeId,
                    brand = item.brand,
                    name = item.name,
                    price = item.price
                )
            )

            groceryDao.addItems(
                Grocery(
                    sku = item.sku,
                    storeUid = storeId,
                    brand = item.brand,
                    name = item.name,
                    price = item.price,
                    quantity = item.quantity
                )
            )
        } catch (error: Error) {
            println(error)
        }
    }
}

fun getItemFromGroceryList(
    db: AppDatabase,
    storeId: Int,
    itemSku: String,
    onLoaded: (item: ItemsListData) -> Unit,
) {
    db.runInCoroutineExecutor {
        try {
            val groceryDao = db.groceryDao()
            val item = groceryDao.getItem(storeId, itemSku)

            if (item?.sku?.isNotEmpty() == true) {
                onLoaded(
                    ItemsListData(
                        item.sku,
                        item.brand,
                        item.name,
                        item.price,
                        item.quantity,
                    )
                )
            }
        } catch (error: Error) {
            println(error)
        }
    }
}

fun getItemFromStoreItems(
    db: AppDatabase,
    storeId: Int,
    itemSku: String,
    onLoaded: (item: ItemsListData) -> Unit,
) {
    db.runInCoroutineExecutor {
        try {
            val storeItemsDao = db.storeItemsDao()
            val item = storeItemsDao.getItem(storeId, itemSku)

            if (item?.sku?.isNotEmpty() == true) {
                onLoaded(
                    ItemsListData(
                        item.sku,
                        item.brand,
                        item.name,
                        item.price,
                        0,
                    )
                )
            }
        } catch (error: Error) {
            println(error)
        }
    }
}

@Composable
fun ScanItem(
    modifier: Modifier = Modifier,
    returnToPreviousScreen: () -> Unit,
    db: AppDatabase,
    storeId: Int,
    itemSku: String = "",
) {
    var barcodeValue: String by remember { mutableStateOf(itemSku) }
    var productBrand: String by remember { mutableStateOf("") }
    var productName: String by remember { mutableStateOf("") }
    var productQuantity: Int by remember { mutableStateOf(1) }
    var productPrice: Double by remember { mutableStateOf(0.0) }

    var textFieldBrand: String by remember { mutableStateOf("") }
    var textFieldName: String by remember { mutableStateOf("") }
    var textFieldQuantity: String by remember { mutableStateOf("1") }
    var textFieldPrice: String by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun addItem(item: ItemsListData) {
        addItemToGroceryList(db, storeId, item)
        returnToPreviousScreen()
    }

    if (itemSku.isNotEmpty()) {
        getItemFromGroceryList(db, storeId, barcodeValue, onLoaded = { item ->
            textFieldQuantity = item.quantity.toString()
            productQuantity = item.quantity
        })
    }

    LaunchedEffect(barcodeValue) {
        getItemFromStoreItems(db, storeId, barcodeValue, onLoaded = { item ->
            textFieldBrand = item.brand
            productBrand = item.brand
            textFieldName = item.name
            productName = item.name
            textFieldPrice = item.price.toString()
            productPrice = item.price
        })
    }

    Scaffold(
        modifier.fillMaxSize(),
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = contentPadding)
                    .padding(all = 12.dp)
            ) {
                Column {
                    if (barcodeValue.isEmpty()) {
                        BarcodeScannerAnalysis(
                            modifier = modifier
                                .aspectRatio(16f / 9f)
                                .fillMaxWidth(),
                            callback = { barcodes ->
                                barcodes.forEach { barcode ->
                                    if (barcode.rawValue?.isNotEmpty() == true) {
                                        barcodeValue = barcode.rawValue ?: ""
                                    }
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(height = 16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.scan_item_barcode),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = barcodeValue,
                            fontSize = 22.sp,
                        )
                    }

                    if (barcodeValue.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(height = 16.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            TextField(
                                value = textFieldBrand,
                                onValueChange = {
                                    textFieldBrand = it
                                    productName = textFieldBrand
                                },
                                label = { Text(stringResource(R.string.scan_item_enter_brand)) },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                            )

                            Spacer(modifier = Modifier.height(height = 8.dp))

                            TextField(
                                value = textFieldName,
                                onValueChange = {
                                    textFieldName = it
                                    productName = textFieldName
                                },
                                label = { Text(stringResource(R.string.scan_item_enter_name)) },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                            )

                            Spacer(modifier = Modifier.height(height = 8.dp))

                            TextField(
                                value = textFieldQuantity,
                                onValueChange = {
                                    textFieldQuantity = it
                                    productQuantity = textFieldQuantity.toIntOrNull() ?: 1
                                },
                                label = { Text(stringResource(R.string.scan_item_enter_quantity)) },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Number
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                            )

                            Spacer(modifier = Modifier.height(height = 8.dp))

                            TextField(
                                value = textFieldPrice,
                                onValueChange = {
                                    textFieldPrice = it
                                    productPrice = textFieldPrice.toDoubleOrNull() ?: 1.0
                                },
                                label = { Text(stringResource(R.string.scan_item_enter_price)) },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Decimal
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                            )

                            Spacer(modifier = Modifier.height(height = 8.dp))
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = { returnToPreviousScreen() }) {
                        Text(stringResource(R.string.cancel_button))
                    }

                    if (barcodeValue.isNotEmpty()) {
                        Button(onClick = {
                            addItem(
                                ItemsListData(
                                    sku = barcodeValue,
                                    brand = productBrand,
                                    name = productName,
                                    price = productPrice,
                                    quantity = productQuantity,
                                )
                            )
                        }) {
                            Text(stringResource(R.string.scan_item_add))
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewScanItem() {
    EasyStoresTheme {
        Surface {
            val db = getDatabase()

            ScanItem(
                returnToPreviousScreen = {},
                db = db,
                storeId = 0,
            )
        }
    }
}