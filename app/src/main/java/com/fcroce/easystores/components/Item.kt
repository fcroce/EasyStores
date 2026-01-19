package com.fcroce.easystores.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fcroce.easystores.R
import com.fcroce.easystores.data.GroceryItems
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun Item(item: GroceryItems, isLast: Boolean, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(bottom = if (isLast) 0.dp else 12.dp)
            .clickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Item screenshot",
            modifier = Modifier
                .size(size = 22.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        Text(text = item.name, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.width(width = 8.dp))

        Text(text = item.quantity.toString(), color = MaterialTheme.colorScheme.secondary)

        Spacer(modifier = Modifier.width(width = 8.dp))

        Text(text = item.price.toString(), color = MaterialTheme.colorScheme.tertiary)
    }
}

@Preview
@Composable
fun PreviewItem() {
    EasyStoresTheme {
        Surface {
            Item(
                GroceryItems(
                    sku = "1",
                    storeUid = 1,
                    brand = "Brand 1",
                    name = "Item 1",
                    price = 0.0,
                    quantity = 0
                ),
                isLast = false
            )
        }
    }
}

@Preview
@Composable
fun PreviewItemLast() {
    EasyStoresTheme {
        Surface {
            Item(
                GroceryItems(
                    sku = "1",
                    storeUid = 1,
                    brand = "Brand 1",
                    name = "Item 1",
                    price = 0.0,
                    quantity = 0
                ),isLast = true
            )
        }
    }
}