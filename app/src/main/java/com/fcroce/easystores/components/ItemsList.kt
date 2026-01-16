package com.fcroce.easystores.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun ItemsList(items: Array<String>) {
    Column(modifier = Modifier.padding(all = 12.dp)) {
        items.forEachIndexed { index, item ->
            Item(item, index == items.size - 1)
        }
    }
}

@Preview
@Composable
fun PreviewItemsList() {
    EasyStoresTheme {
        Surface {
            ItemsList(arrayOf("Item 1", "Item 2"))
        }
    }
}