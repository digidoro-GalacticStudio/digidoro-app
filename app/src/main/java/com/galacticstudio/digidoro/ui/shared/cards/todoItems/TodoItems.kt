package com.galacticstudio.digidoro.ui.shared.cards.todoItems

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.shadowWithBorder

//corner
val cornerRadius = 5.dp
data class TodoMessageData(
    val mainMessage: String,
    val messageBold: String,
    val messageNoBold: String
)

@Preview(showSystemUi = true)
@Composable
fun todoItemPreview(){
    DigidoroTheme() {
        TodoItem(TodoMessageData("Tarea a realizar mada faka", "Hoy", "8:00AM"))
    }
}

/**
 * A composable function representing Todo Item.
 *
 * @param message the object of strings messages the item will display.
 * @param done  state of the item.
 * @param onClick function todo item will execute when click happen
 */
@Composable
fun TodoItem(
    message: TodoMessageData,
    done: Boolean = false,
    colorTheme: Color = MaterialTheme.colorScheme.secondary,
    onClick: ()-> Unit = fun(){}){
    var isChecked by remember { mutableStateOf(value = done) }

    Box(modifier = Modifier.clickable {
        onClick
    }){

        TodoInformation(
            message = message,
            done = isChecked,
            colorTheme = colorTheme,
            stateHandler = { isChecked = !isChecked },
        )

    }
}

/**
 * A composable function representing Data which will be display inside each item.
 *
 * @param message the object of strings messages the item will display.
 * @param done  state of the item.
 * @param stateHandler handler of item state
 */
@Composable
fun TodoInformation(
    message: TodoMessageData,
    done: Boolean,
    colorTheme: Color,
    modifier: Modifier = Modifier,
    stateHandler: ()-> Unit
){
    Box(
        modifier = Modifier
            .shadowWithBorder(
                cornerRadius = cornerRadius,
                shadowOffset = Offset(15.0f, 15.0f),
                shadowColor = if (done) MaterialTheme.colorScheme.secondary else colorTheme
            )
            .then(modifier)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    colorResource(id = R.color.secondary_color),
                    RoundedCornerShape(cornerRadius)
                )
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(cornerRadius)
                )

        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = done,
                    colors = CheckboxDefaults.colors(
                        checkedColor =  MaterialTheme.colorScheme.secondary,
                        uncheckedColor = MaterialTheme.colorScheme.secondary,
                        checkmarkColor = MaterialTheme.colorScheme.primary
                    ),
                    onCheckedChange = {
                        stateHandler()
                    })
                Text(
                    text = message.mainMessage,
                    color = if(done == true) MaterialTheme.colorScheme.secondary else colorResource(
                        R.color.black_text_color),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = message.messageBold + ", ",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = message.messageNoBold,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall,
                )
            }

        }
    }
}