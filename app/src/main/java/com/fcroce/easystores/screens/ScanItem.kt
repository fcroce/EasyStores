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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcroce.easystores.R
import com.fcroce.easystores.components.camera.BarcodeScannerAnalysis
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun ScanItem(
    modifier: Modifier = Modifier,
    returnToPreviousScreen: () -> Unit,
) {
    val barcodeString = stringResource(R.string.scan_item_barcode)
    var barcodeValue: String by remember { mutableStateOf("") }

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
                                    barcodeValue = barcode.rawValue ?: ""
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
                            text = barcodeString,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = barcodeValue,
                            fontSize = 22.sp,
                        )
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
                        Text(stringResource(R.string.scan_item_cancel))
                    }

                    Button(onClick = { returnToPreviousScreen() }) {
                        Text(stringResource(R.string.scan_item_done))
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
            ScanItem(returnToPreviousScreen = {})
        }
    }
}