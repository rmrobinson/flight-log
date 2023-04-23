package ca.faltung.flightlog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun ValidatedTextField(
    modifier: Modifier,
    input: StringInput,
    label: @Composable () -> Unit,
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    onImeKeyAction: (String) -> Unit = {},
    singleLine: Boolean = false,
) {
    Column(
        modifier = modifier
    ) {
        TextField(
            label = label,
            value = input.value,
            onValueChange = onValueChange,
            isError = input.errorResId != null,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
        )
        if (input.errorResId != null) {
            Text(
                modifier = modifier.padding(start = 16.dp),
                text = stringResource(id = input.errorResId),
                color = MaterialTheme.colors.error,
            )
        }
    }
}