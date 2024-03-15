package ca.faltung.flightlog.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.faltung.flightlog.R
import ca.faltung.flightlog.ui.theme.AppTheme
import kotlinx.datetime.*

@Composable
fun TimePicker (
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    value: String,
    onValueChange: (String) -> Unit = {},
    is24HourView: Boolean = true,
){
    val now: Instant = Clock.System.now()
    val time: LocalTime = try { LocalTime.parse(value) } catch (e: Exception) { now.toLocalDateTime(
        TimeZone.currentSystemDefault()).time }

    val dialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, min -> onValueChange(LocalTime.parse("%02d:%02d:00".format(hour,min)).toString())},
        time.hour,
        time.minute,
        is24HourView
    )

    Box(
    ) {
        TextField(
            label = label,
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.align(Alignment.CenterStart),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Image(
            painter = painterResource(id = R.drawable.ic_time_selector),
            contentDescription = "Time selector",
            modifier = modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 8.dp)
                .clickable { dialog.show() },
        )
    }
}

@Preview
@Composable
fun PreviewTimePicker() {
    AppTheme {
        TimePicker(label = { Text("Test Time Picker") }, value = "12:34:56")
    }
}
