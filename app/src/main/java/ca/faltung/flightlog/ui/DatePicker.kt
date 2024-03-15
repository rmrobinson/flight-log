package ca.faltung.flightlog.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.faltung.flightlog.R
import ca.faltung.flightlog.ui.theme.AppTheme
import kotlinx.datetime.*

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    value: String,
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    var isDateValid by rememberSaveable { mutableStateOf(true) }

    fun datePickerChanged(year: Int, month: Int, day: Int) {
        onValueChange(LocalDate.parse("%d-%02d-%02d".format(year,month+1,day)).toString())
        isDateValid = true
    }

    val date: LocalDate = try { LocalDate.parse(value) } catch (e: Exception) { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    val dialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day -> datePickerChanged(year, month, day)},
        date.year,
        date.monthNumber,
        date.dayOfMonth
    )

    val focusManager = LocalFocusManager.current

    val keyOps = keyboardOptions.copy(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next,
    )
    var keyActions = KeyboardActions(
        onDone = keyboardActions.onDone,
        onGo = keyboardActions.onGo,
        onNext = {
            try {
                LocalDate.parse(value)
                isDateValid = true

                focusManager.moveFocus(FocusDirection.Down)
            } catch (e: Exception) {
                isDateValid = false
            }
        },
        onPrevious = keyboardActions.onPrevious,
        onSearch = keyboardActions.onSearch,
        onSend = keyboardActions.onSend,
    )

    Column() {
        Box(
        ) {
            TextField(
                label = label,
                value = value,
                onValueChange = onValueChange,
                modifier = modifier.align(Alignment.CenterStart),
                keyboardOptions = keyOps,
                keyboardActions = keyActions,
                isError = !isDateValid,
                singleLine = true,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_date_selector),
                contentDescription = "Date selector",
                modifier = modifier.align(Alignment.CenterEnd).padding(horizontal = 8.dp).clickable { dialog.show() },
            )
        }
        if (!isDateValid) {
            Text(
                modifier = modifier.padding(start = 16.dp),
                text = "Valid Date required (YYYY-MM-DD)",
                color = MaterialTheme.colors.error
            )
        }
    }
}

@Preview
@Composable
fun PreviewDatePicker() {
    AppTheme {
        DatePicker(label = { Text("Test Date Picker") }, value = "2023-01-01")
    }
}
