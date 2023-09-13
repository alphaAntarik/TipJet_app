package com.example.tipjet_app


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.tipjet_app.components.InputField
import com.example.tipjet_app.ui.theme.TipJet_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipJet_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomePage()
                }
            }
        }
    }
}

@Composable
fun HomePage() {
    val totalBillState = remember {
        mutableStateOf("0.00")
    }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidthInDp = (configuration.screenWidthDp * density).toInt()
    val screenHeightInDp = (configuration.screenHeightDp * density).toInt()
    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        Column() {
            Surface( modifier = Modifier.width(screenWidthInDp.dp), elevation = 6.dp) {
                Text(text = "Give a Tip", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.h4, modifier = Modifier.padding(10.dp).padding(start = 7.dp))
            }

            EachPersonWillPay(totalBillState.value.toDouble(), screenWidthInDp,screenHeightInDp)
            EnterDetailsScreen(screenWidthInDp,screenHeightInDp){
                totalBillState.value=it
            }

        }

    }
}

@Composable
fun EachPersonWillPay(amount: Double = 0.0, width: Int, height:Int) {

 val amounttotal = "%.2f".format(amount)
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 15.dp)
        .height(height * 0.08.dp)
        .padding(horizontal = width * 0.015.dp)
        .padding(top = height * 0.01.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(android.graphics.Color.parseColor("#CBC3E3"))
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Each person will pay",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "₹ $amounttotal",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 40.sp,

                    textAlign = TextAlign.Center,
                )

            }


        }



    }

}


@Composable
fun EnterDetailsScreen(width:Int = 0, height:Int=0,onValllChange: (String) -> Unit={}) {

    val totalBillState = remember {
        mutableStateOf("")
    }





    Card(modifier= Modifier
        .fillMaxWidth()
        .height(height * 0.15.dp)
        .padding(horizontal = width * 0.015.dp), shape = RoundedCornerShape(16.dp), border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {

            BillForm(){
                totalBillState.value=it
                onValllChange(totalBillState.value)
            }






    }

}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier=Modifier, onValChange: (String) -> Unit={}){
    val totalBillState = remember {
        mutableStateOf("")
    }


    val isValidState = remember (totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val count = remember { mutableStateOf(0) }
    val tip = remember { mutableStateOf("") }

    val isValidState1 = remember (tip.value){
        tip.value.isNotEmpty()
    }


    val textController = LocalSoftwareKeyboardController.current
    val textController1 = LocalSoftwareKeyboardController.current
    Column {
    InputField(valueState =totalBillState ,
        labelId = "Enter bill amount",
        enabled = true,
        isSingledLined = true,
        onAction = KeyboardActions{
            if(!isValidState)return@KeyboardActions




            textController?.hide()

        }

    )
        if(isValidState){


           Column() {
               Row( verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 10.dp)){
                   Text(text = "Number of friends", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold )
                   Box(modifier = Modifier.width(60.dp))
                   IconButton(onClick =
                   {
                       if(count.value>1)
                       // onValueChange(count.value--)
                           count.value--
                   }) {
                       Icon(imageVector = Icons.Rounded.Remove, contentDescription = "reduce button")
                   }
                   Text(text = count.value.toString(),style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
                   IconButton(onClick = {
                       // onValueChange( count.value++)
                       count.value++
                   }) {
                       Icon(imageVector = Icons.Rounded.Add, contentDescription = "increase button")
                   }
               }


               InputField(valueState = tip, labelId = "Enter tip", enabled = true , isSingledLined = true,   onAction = KeyboardActions{
                   if(!isValidState1)return@KeyboardActions

                   textController1?.hide()

               })
               Button(
                   onClick = {  onValChange(((totalBillState.value.toDouble() + tip.value.toDouble())/count.value).toString())
                   },
                   modifier = Modifier.align(Alignment.CenterHorizontally)
               ) {
                   Text("Check")
               }



           }

        }else{
            Box(){}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipJet_appTheme {
        HomePage()
    }
}