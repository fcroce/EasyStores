package com.fcroce.easystores.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.fcroce.easystores.theme.EasyStoresTheme

@Composable
fun DropdownList(
    itemList: List<String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    startOpen: Boolean = false,
    onItemClick: (Int) -> Unit = {},
    fontSize: TextUnit = 16.sp,
) {
    var showDropdown by rememberSaveable { mutableStateOf(startOpen) }
    var selectedIdx by rememberSaveable { mutableIntStateOf(selectedIndex) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = modifier.clickable { showDropdown = !showDropdown },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = itemList[selectedIdx],
                modifier = Modifier.padding(3.dp),
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Box {
            if (showDropdown) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(
                        excludeFromSystemGesture = true,
                    ),
                    onDismissRequest = { showDropdown = false }
                ) {

                    Column(
                        modifier = modifier
                            .heightIn(max = 300.dp)
                            .background(color = MaterialTheme.colorScheme.background)
                            .verticalScroll(state = scrollState)
                            .padding(all = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemList.forEachIndexed { index, item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 12.dp,
                                        bottom = 0.dp,
                                        start = 22.dp,
                                        end = 22.dp
                                    )
                                    .drawBehind {
                                        if (index < itemList.size - 1) {
                                            drawLine(
                                                color = Color.LightGray,
                                                start = Offset(0f, size.height + 12.dp.toPx()),
                                                end = Offset(
                                                    size.width,
                                                    size.height + 12.dp.toPx()
                                                ),
                                                strokeWidth = 1.dp.toPx()
                                            )
                                        }
                                    }
                                    .clickable {
                                        selectedIdx = index
                                        onItemClick(index)
                                        showDropdown = !showDropdown
                                    },
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Text(
                                    text = item,
                                    fontSize = fontSize,
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDropdownList() {
    EasyStoresTheme {
        Surface {
            DropdownList(
                itemList = listOf("One", "Two", "Three", "Four"),
                startOpen = true,
            )
        }
    }
}