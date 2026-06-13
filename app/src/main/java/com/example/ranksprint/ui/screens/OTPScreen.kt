package com.example.ranksprint.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.paint
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ranksprint.R
import com.example.ranksprint.common.Utils
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.model.ResendOTPResponse
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.navigation.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun OTPScreen(
    userId: String,
    email: String,
    onOtpVerified: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit
) {
    var otp1 by remember { mutableStateOf("") }
    var otp2 by remember { mutableStateOf("") }
    var otp3 by remember { mutableStateOf("") }
    var otp4 by remember { mutableStateOf("") }
    
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isAccepted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Illustration
        Image(
            painter = painterResource(R.drawable.otp_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Verify Your Email Id",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001D36),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We have just sent a code to $email",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OtpBox(
                value = otp1,
                onValueChange = {
                    if (it.length <= 1) {
                        otp1 = it
                        errorMessage = null
                        if (it.isNotEmpty()) focusRequester2.requestFocus()
                    }
                },
                focusRequester = focusRequester1
            )
            OtpBox(
                value = otp2,
                onValueChange = {
                    if (it.length <= 1) {
                        otp2 = it
                        errorMessage = null
                        if (it.isNotEmpty()) focusRequester3.requestFocus()
                    }
                },
                focusRequester = focusRequester2,
                onBackspace = { if (otp2.isEmpty()) focusRequester1.requestFocus() }
            )
            OtpBox(
                value = otp3,
                onValueChange = {
                    if (it.length <= 1) {
                        otp3 = it
                        errorMessage = null
                        if (it.isNotEmpty()) focusRequester4.requestFocus()
                    }
                },
                focusRequester = focusRequester3,
                onBackspace = { if (otp3.isEmpty()) focusRequester2.requestFocus() }
            )
            OtpBox(
                value = otp4,
                onValueChange = {
                    if (it.length <= 1) {
                        otp4 = it
                        errorMessage = null
                    }
                },
                focusRequester = focusRequester4,
                onBackspace = { if (otp4.isEmpty()) focusRequester3.requestFocus() }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        TextButton(
            onClick = {
                isLoading = true
                ServiceManager.getDataManager().resendOTP(object : Callback<ResendOTPResponse> {
                    override fun onResponse(call: Call<ResendOTPResponse>, response: Response<ResendOTPResponse>) {
                        isLoading = false
                        errorMessage = response.body()?.message ?: "OTP Resent"
                    }

                    override fun onFailure(call: Call<ResendOTPResponse>, t: Throwable) {
                        isLoading = false
                        errorMessage = t.message ?: "Network error"
                    }
                }, userId)
            },
            enabled = !isLoading
        ) {
            Text(
                text = "Resend OTP",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFFE5D10),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = isAccepted,
                onCheckedChange = { isAccepted = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFE5D10))
            )
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("By signing up you agree to ")
                }
                pushStringAnnotation(tag = "terms", annotation = "terms")
                withStyle(style = SpanStyle(color = Color(0xFFFE5D10), fontWeight = FontWeight.Bold)) {
                    append("Terms")
                }
                pop()
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append(" and ")
                }
                pushStringAnnotation(tag = "privacy", annotation = "privacy")
                withStyle(style = SpanStyle(color = Color(0xFFFE5D10), fontWeight = FontWeight.Bold)) {
                    append("Privacy Policy")
                }
                pop()
            }

            ClickableText(
                text = annotatedString,
                style = TextStyle(textAlign = TextAlign.Center, fontSize = 12.sp),
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset)
                        .firstOrNull()?.let { onNavigateToTerms() }
                    annotatedString.getStringAnnotations(tag = "privacy", start = offset, end = offset)
                        .firstOrNull()?.let { onNavigateToPrivacy() }
                }
            )
        }

        Button(
            onClick = {
                if (!isAccepted) {
                    errorMessage = "Please accept Terms and Privacy Policy"
                    return@Button
                }
                val otp = otp1 + otp2 + otp3 + otp4
                if (otp.length < 4) {
                    errorMessage = "Please enter 4-digit OTP"
                    return@Button
                }
                isLoading = true
                ServiceManager.getDataManager().verifyOTP(object : Callback<OTPVerifyResponse> {
                    override fun onResponse(call: Call<OTPVerifyResponse>, response: Response<OTPVerifyResponse>) {
                        isLoading = false
                        if (response.isSuccessful && response.body()?.status == 1) {
                            val body = response.body()!!
                            Utils.saveData(context, "user_id", body.userInfo?.userId)
                            Utils.saveData(context, "full_name", body.userInfo?.fullName)
                            Utils.saveData(context, "email", body.userInfo?.email)
                            Utils.saveData(context, "phone", body.userInfo?.phone)
                            Utils.saveData(context, "access_token", body.accessToken)
                            Utils.access_token = body.accessToken ?: ""
                            Utils.saveData(context, Utils.IS_REGISTERED, true)
                            onOtpVerified()
                        } else {
                            errorMessage = response.body()?.message ?: "Verification failed"
                        }
                    }

                    override fun onFailure(call: Call<OTPVerifyResponse>, t: Throwable) {
                        isLoading = false
                        errorMessage = t.message ?: "Network error"
                    }
                }, userId, otp)
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
                    text = "Verify",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun OtpBox(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onBackspace: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .paint(
                painter = painterResource(id = R.drawable.otp_field_bg),
                contentScale = ContentScale.FillBounds
            ),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .onKeyEvent {
                    if (it.key == Key.Backspace) {
                        onBackspace()
                        false
                    } else {
                        false
                    }
                },
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true
        )
    }
}
