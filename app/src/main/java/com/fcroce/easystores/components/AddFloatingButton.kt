package com.fcroce.easystores.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fcroce.easystores.R
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun AddFloatingButton(
    onClick: () -> Unit = {},
    tint: Color = Color.White,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_new_store),
            tint = tint,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview
@Composable
fun PreviewAddItem() {
    EasyStoresTheme {
        Surface {
            AddFloatingButton()
        }
    }
}