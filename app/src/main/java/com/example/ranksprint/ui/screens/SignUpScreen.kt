package com.example.ranksprint.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ranksprint.R
import com.example.quiztech.model.LoginResponse
import com.example.quiztech.model.RegistrationMainRes
import com.example.quiztech.services.ServiceManager
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignUpScreen(
    onSignUpSuccess: (userId: String, email: String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var device_id by remember { mutableStateOf("") }


    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                device_id = token
                Log.e("FCM_TOKEN", device_id)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Illustration
        Image(
            painter = painterResource(R.drawable.signup_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to Rank Sprint",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001D36),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create your account and get started in seconds",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        SignUpTextField(value = fullName, onValueChange = { fullName = it; errorMessage = null }, label = "Full Name")
        Spacer(modifier = Modifier.height(16.dp))
        SignUpTextField(value = phoneNumber, onValueChange = { phoneNumber = it; errorMessage = null }, label = "+91 999999 9999")
        Spacer(modifier = Modifier.height(16.dp))
        SignUpTextField(value = email, onValueChange = { email = it; errorMessage = null }, label = "Email Id")
        Spacer(modifier = Modifier.height(16.dp))
        SignUpTextField(value = city, onValueChange = { city = it; errorMessage = null }, label = "City")
        Spacer(modifier = Modifier.height(16.dp))
        SignUpTextField(value = pinCode, onValueChange = { pinCode = it; errorMessage = null }, label = "Pin Code")
        Spacer(modifier = Modifier.height(16.dp))
        SignUpTextField(
            value = address,
            onValueChange = { address = it; errorMessage = null },
            label = "Address",
            modifier = Modifier.height(120.dp),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isBlank() || fullName.isBlank() || phoneNumber.isBlank() || city.isBlank() || pinCode.isBlank() || address.isBlank()) {
                    errorMessage = "Please fill all fields"
                    return@Button
                }
                isLoading = true
                ServiceManager.getDataManager().registerUser(object : Callback<RegistrationMainRes> {
                    override fun onResponse(call: Call<RegistrationMainRes>, response: Response<RegistrationMainRes>) {
                        isLoading = false
                        if (response.isSuccessful && response.body()?.status == 1) {
                            val body = response.body()!!
                            Toast.makeText(context, body.message ?: "User registered successfully", Toast.LENGTH_LONG).show()
                            onSignUpSuccess(body.data!!.userId ?: "", body.data!!.email ?: email)
                        } else {
                            errorMessage = response.body()?.message ?: "Registration failed"
                        }
                    }

                    override fun onFailure(call: Call<RegistrationMainRes>, t: Throwable) {
                        isLoading = false
                        errorMessage = t.message ?: "Network error"
                    }
                }, fullName, email, phoneNumber, "123456", city, pinCode, address, device_id)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE5D10)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.Gray) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5),
            disabledContainerColor = Color(0xFFF5F5F5),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}
