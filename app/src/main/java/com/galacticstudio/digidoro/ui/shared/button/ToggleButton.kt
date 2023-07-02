package com.galacticstudio.digidoro.ui.shared.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.ui.theme.AzureBlue10

enum class SelectionType {
    NONE,
    SINGLE,
    MULTIPLE,
}

data class ToggleButtonOption(
    val type: ToggleButtonOptionType,
    val text: String,
    val iconRes: Int?,
)

sealed class ToggleButtonOptionType {
    object Today : ToggleButtonOptionType()
    object Weekly : ToggleButtonOptionType()
    object Monthly : ToggleButtonOptionType()
    object Total : ToggleButtonOptionType()
}

val options = arrayOf(
    ToggleButtonOption(ToggleButtonOptionType.Today, "Today", null),
    ToggleButtonOption(ToggleButtonOptionType.Weekly, "Semanal", null),
    ToggleButtonOption(ToggleButtonOptionType.Monthly, "Mensual", null),
)

@Composable
fun ToggleButton(
    backgroundColor: Color = AzureBlue10,
    optionList: Array<ToggleButtonOption> = options,
    enabled: Boolean = true,
    onButtonClick: (selectedOption: ToggleButtonOption) -> Unit = {},
) {
    ContainerToggleButton(
        backgroundColor = backgroundColor,
        options = optionList,
        type = SelectionType.SINGLE,
        modifier = Modifier.padding(end = 4.dp),
        enabled = enabled,
        onButtonClick = onButtonClick
    )
}

@Composable
fun VerticalDivider(
    color: Color
) {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(2.dp),
        color = color.copy(0.7f)
    )
}

@Composable
fun ContainerToggleButton(
    backgroundColor: Color,
    options: Array<ToggleButtonOption>,
    modifier: Modifier = Modifier,
    type: SelectionType = SelectionType.SINGLE,
    enabled: Boolean,
    onButtonClick: (selectedOption: ToggleButtonOption) -> Unit = {},
) {
    val state = remember { mutableStateMapOf<String, ToggleButtonOption>() }
    state[options.first().text] = options.first() // Select the first option by default

    OutlinedButton(
        onClick = { },
        border = BorderStroke(1.dp, backgroundColor.copy(0.7f)),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Red,
            containerColor = backgroundColor
        ),
        contentPadding = PaddingValues(0.dp, 0.dp),
        modifier = modifier
            .padding(3.dp)
            .height(35.dp),
    ) {
        if (options.isEmpty()) {
            return@OutlinedButton
        }

        val onItemClick: (option: ToggleButtonOption) -> Unit = { option ->
            if (enabled) {
                if (type == SelectionType.SINGLE) {
                    options.forEach {
                        val key = it.text
                        if (key == option.text) {
                            state[key] = option
                        } else {
                            state.remove(key)
                        }
                    }
                } else {
                    val key = option.text
                    if (!state.contains(key)) {
                        state[key] = option
                    } else {
                        state.remove(key)
                    }
                }
                onButtonClick(option)
            }
        }

        if (options.size == 1) {
            val option = options.first()
            SelectionPill(
                option = option,
                selected = state.contains(option.text),
                onClick = onItemClick,
            )
            return@OutlinedButton
        }

        val first = options.first()
        val last = options.last()
        val middle = options.slice(1..options.size - 2)

        SelectionPill(
            option = first,
            selected = state.contains(first.text),
            onClick = onItemClick,
        )

        VerticalDivider(backgroundColor)
        middle.map { option ->
            SelectionPill(
                option = option,
                selected = state.contains(option.text),
                onClick = onItemClick,
            )
            VerticalDivider(backgroundColor)
        }

        SelectionPill(
            option = last,
            selected = state.contains(last.text),
            onClick = onItemClick,
        )
    }
}

@Composable
fun SelectionPill(
    option: ToggleButtonOption,
    selected: Boolean,
    onClick: (option: ToggleButtonOption) -> Unit = {}
) {
    val textDecoration = if (selected) {
        TextDecoration.Underline
    } else {
        null
    }

    Button(
        onClick = { onClick(option) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(0),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.padding(14.dp, 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(0.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = option.text,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(textDecoration = textDecoration),
                fontWeight = if (selected) FontWeight.W800 else FontWeight.W500,
                modifier = Modifier.padding(0.dp),
            )
        }
    }
}