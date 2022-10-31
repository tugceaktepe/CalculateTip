package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatetip.R
import com.example.calculatetip.ui.theme.CalculateTipTheme
import java.text.NumberFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculateTipTheme {
                TipTimeScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipTimeScreen() {

    var amountInput by remember {
        mutableStateOf("")
    }
    //EditNumberField component ine girilen inputlari elvis operatorü ile kontrol ediyoruz.
    //Hesaplama islemleri ondalik sayilarla yapildigi icin toDoubleOrNull ile convert islemi yapiyoruz.
    val amount = amountInput.toDoubleOrNull() ?: 0.0

    var tipInput by remember {
        mutableStateOf("")
    }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    var roundUp by remember {
        mutableStateOf(false)
    }
    val tip = calculateTip(amount, tipPercent, roundUp)

    val focusManager = LocalFocusManager.current
    Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        //iki composable arasina bosluk koymak icin Space i kullaniyoruz.
        Spacer(modifier = Modifier.height(16.dp))

        //amountInput degiskenini state hoisting ile disariya cektik. Bunu onValueChanged lambda expression ile yaptik.
        EditNumberField(
            label = R.string.cost_of_service, value = amountInput, onValueChanged = {
                amountInput = it //recomposition olacagi icin tip sürekli hesaplanacak.
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,  //klavye numerik cikmasi icin kullandik.
                //klavyede isimiz bitince bir sonraki focus noktasini belirlemek icin kullandik.
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions( onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        //EditNumberField component ini label ve value degiskenlerini degistirerek reuse yaptik.
        EditNumberField(
            label = R.string.how_was_the_service, value = tipInput, onValueChanged = {
                tipInput = it
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ), keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            })
        )

        //roundUp degiskenini state hoisting ile disariya cektik. Bunu onRoundUpChanged lambda expression ile yaptik.
        RoundTheTipRow(
            label = R.string.round_up_tip,
            roundUp = roundUp,
            onRoundUpChanged = {
                roundUp = it
            })

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.tip_amount, tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

    }
}

//Input olarak sadece numerik deger kabul eden bir Textfield olusturduk.
// Bu componenti uygulamamizda istedigimiz yerde kullanabiliriz.
@Composable
fun EditNumberField(
    @StringRes label: Int,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier //modifier degiskenine default deger olarak Modifier objesi verdik.
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(
                text = stringResource(id = label), modifier = Modifier.fillMaxWidth()
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions, singleLine = true
    )
}

@Composable
fun RoundTheTipRow(
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        //verticalAlignment -> icinde bulunan elemanlari dikey eksende ortaliyor.
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(label))
        Switch(
            //checked degiskeni switch in state'i anlamına gelir. O anki state'i onRoundUp'a atadik.
            checked = roundUp,
            //onCheckedChange callback switch trigger edildigi zaman kullaniliyor.
            onCheckedChange = onRoundUpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
        )
    }
}

@VisibleForTesting // @VisibleForTesting metodun sadece test siniflari tarafindan gorulmesini sagliyor.
internal fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if(roundUp){
        tip = kotlin.math.ceil(tip)
    }
    //TODO: Currency instance ilgili cihazin local areasina bagli para birimi icin kullanilir.
    // Emulatorde US locale oldugu icin boyle verdik.
    return NumberFormat.getCurrencyInstance(Locale.US).format(tip)
}