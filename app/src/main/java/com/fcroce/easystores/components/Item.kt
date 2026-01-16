package com.fcroce.easystores.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun Item(item: String, isLast: Boolean) {
    Row(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 12.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Item screenshot",
            modifier = Modifier
                .size(size = 22.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        Text(text = item, color = MaterialTheme.colorScheme.primary)
    }
}

@Preview
@Composable
fun PreviewItem() {
    EasyStoresTheme {
        Surface {
            Item("Item 1", isLast = false)
        }
    }
}

@Preview
@Composable
fun PreviewItemLast() {
    EasyStoresTheme {
        Surface {
            Item("Item 1", isLast = true)
        }
    }
}