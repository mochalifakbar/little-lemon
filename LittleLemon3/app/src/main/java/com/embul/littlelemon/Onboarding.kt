package com.embul.littlelemon

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Onboarding(navController: NavHostController) {
    var firstName by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var lastName by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var email by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val context = LocalContext.current

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxWidth(0.55f).height(100.dp),
            contentScale = ContentScale.Fit
        )

        Surface (
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth().height(125.dp)
        ) {
            Box (
                contentAlignment = Alignment.Center,
            ) {
                Text("Lets get to know you",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 32.sp,
                    )
            }
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Personal Information",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Column (
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(bottom = 40.dp)

            ) {
                LabeledTextField(label = "First name", value = firstName.text, onValueChange = {
                    firstName = TextFieldValue(it)
                })
                LabeledTextField(label = "Last name", value = lastName.text, onValueChange = {
                    lastName = TextFieldValue(it)
                })
                LabeledTextField(label = "Email", value = email.text, onValueChange = {
                    email = TextFieldValue(it)
                })
            }

            Button(
                onClick = {
                    if (firstName.text.isEmpty() || lastName.text.isEmpty() || email.text.isEmpty()){
                        Toast.makeText(
                            context,
                            "Registration unsuccessful. Please enter all data.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val sharedPreferences = context.getSharedPreferences("little_lemon", Context.MODE_PRIVATE)
                    CoroutineScope(Dispatchers.IO).launch {
                        sharedPreferences.edit().
                            putBoolean("user_registered", true).
                            putString("first_name", firstName.text).
                            putString("last_name", lastName.text).
                            putString("email", email.text).
                        commit()
                    }
                    Toast.makeText(
                        context,
                        "Registration successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate(Home.route)
                },
                modifier = Modifier.padding(top = 200.dp).fillMaxWidth().height(45.dp).border(width = 1.dp, color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(10.dp)
            ) { Text("Register", color = MaterialTheme.colorScheme.surface, fontSize = 16.sp) }
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(45.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.surface
            )
        )
    }
}