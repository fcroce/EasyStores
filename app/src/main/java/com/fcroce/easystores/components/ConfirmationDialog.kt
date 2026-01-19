package com.fcroce.easystores.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fcroce.easystores.R
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun ConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.conf_dialog_title)) },
        text = { Text(stringResource(R.string.conf_dialog_description)) },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text(stringResource(R.string.conf_dialog_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.conf_dialog_cancel))
            }
        }
    )
}

@Preview
@Composable
fun PreviewConfirmationDialog() {
    EasyStoresTheme {
        Surface {
            ConfirmationDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}