package com.galacticstudio.digidoro.ui.shared.cards.todoItems

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.shared.roundShadowBox.RoundShadowBox

@Composable
fun TodoItem(todo: String, done: Boolean = false){
    var isChecked by remember { mutableStateOf(value = done) }

    Box(){

        RoundShadowBox(
            shadowColor = if(isChecked) colorResource(id = R.color.secondary_color)
            else colorResource(id = R.color.cherry_red_accent),
            borderRadius = 5.dp,
            XYOffset = 5.dp,
            modifier = Modifier
                .padding(10.dp)
        ) {
            TodoInformation(
                todo = todo,
                done = isChecked,
                stateHandler = { isChecked = !isChecked },

                )
        }

    }
}

@Composable
fun TodoInformation(
    todo: String,
    done: Boolean,
    stateHandler: ()-> Unit
){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(260.dp)
    ) {
        Checkbox(
            checked = done,
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(id = R.color.gray_text_color)
            ),
            onCheckedChange = {
                stateHandler()
            } )
        Text(
            text = todo,
            color = if(done == true) colorResource(id = R.color.gray_text_color) else colorResource(
                R.color.black_text_color),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}