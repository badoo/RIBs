package com.badoo.ribs.samples.gallery.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ButtonList(
    buttons: List<Pair<String, () -> Unit>>
) {
    Box(
        modifier = Modifier.fillMaxSize(1f),
        contentAlignment = Alignment.Center
    ) {
        val buttonModifier = Modifier
            .padding(8.dp)
            .wrapContentSize()

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            buttons.forEach { (text, onClick) ->
                Button(
                    onClick = onClick,
                    modifier = buttonModifier
                ) {
                    Text(softWrap = false, text = text)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ButtonListPreview(
) {
    ButtonList(
        buttons = listOf(
            "Button 1" to {},
            "Button 2" to {},
            "Some long text" to {},
        )
    )
}
