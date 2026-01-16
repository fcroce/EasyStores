package com.fcroce.easystores.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun AddStore() {
    Scaffold(
        content = { contentPadding ->
            Column(modifier = Modifier
                .padding(paddingValues = contentPadding)
                .padding(all = 12.dp)
            ) {
                Text(
                    text = "TODO",
                    fontSize = 30.sp
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewAddStore() {
    EasyStoresTheme {
        Surface {
            AddStore()
        }
    }
}