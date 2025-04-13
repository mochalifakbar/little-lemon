package com.embul.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Profile (navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("little_lemon", Context.MODE_PRIVATE)
    }
    val firstName = remember {
        sharedPreferences.getString("first_name", "")
    }
    val lastName = remember {
        sharedPreferences.getString("last_name", "")
    }
    val email = remember {
        sharedPreferences.getString("email", "")
    }
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

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Column (
                modifier = Modifier
                    .padding(vertical = 200.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ){
                Text("Profile Information",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.padding(vertical = 20.dp)
                )
                LabeledText(label = "First name", value = firstName.toString())
                LabeledText(label = "Last name", value = lastName.toString())
                LabeledText(label = "Email", value = email.toString())
            }

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        sharedPreferences.edit().clear().commit()
                        withContext(Dispatchers.Main){
                            navController.navigate(Onboarding.route)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(45.dp).border(width = 1.dp, color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(10.dp)
            ) { Text("Log out", color = MaterialTheme.colorScheme.surface, fontSize = 16.sp) }
        }
    }
}



@Composable
fun LabeledText(
    label: String,
    value: String,
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
            onValueChange = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(45.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.surface
            ),
            readOnly = true
        )
    }
}