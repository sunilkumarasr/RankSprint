package com.example.ranksprint.ui.screens

import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

import com.example.ranksprint.ui.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    onNavigateBack: () -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current
    
    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    
    var profileImageUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female")
    
    var isLoading by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(Unit) {
        val userId = Utils.getData(context, "user_id", "") as String
        if (userId.isNotEmpty()) {
            isLoading = true
            ServiceManager.getDataManager().getProfile(object : Callback<OTPVerifyResponse> {
                override fun onResponse(call: Call<OTPVerifyResponse>, response: Response<OTPVerifyResponse>) {
                    isLoading = false
                    if (response.isSuccessful && response.body()?.status == 1) {
                        val user = response.body()?.userInfo
                        fullName = user?.fullName ?: ""
                        email = user?.email ?: ""
                        phoneNumber = user?.phone ?: ""
                        city = user?.city ?: ""
                        pincode = user?.pincode ?: ""
                        address = user?.address ?: ""
                        profileImageUrl = user?.profileImage ?: ""
                        val genderVal = user?.gender ?: "m"
                        gender = if (genderVal == "f") "Female" else "Male"
                        
                        // Sync local state
                        Utils.saveData(context, "full_name", fullName)
                        Utils.saveData(context, "email", email)
                        Utils.saveData(context, "phone", phoneNumber)
                        Utils.saveData(context, "city", city)
                        Utils.saveData(context, "pincode", pincode)
                        Utils.saveData(context, "address", address)
                        Utils.saveData(context, "profile_image", profileImageUrl)
                        Utils.saveData(context, "gender", genderVal)
                    }
                }

                override fun onFailure(call: Call<OTPVerifyResponse>, t: Throwable) {
                    isLoading = false
                    // Fallback to local data
                    fullName = Utils.getData(context, "full_name", "") as String
                    email = Utils.getData(context, "email", "") as String
                    phoneNumber = Utils.getData(context, "phone", "") as String
                    city = Utils.getData(context, "city", "") as String
                    pincode = Utils.getData(context, "pincode", "") as String
                    address = Utils.getData(context, "address", "") as String
                    profileImageUrl = Utils.getData(context, "profile_image", "") as String
                    val genderVal = Utils.getData(context, "gender", "m") as String
                    gender = if (genderVal == "f") "Female" else "Male"
                }
            }, userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Back", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Personal Info",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004AAD)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Image Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") }
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (profileImageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUrl),
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FileUpload,
                        contentDescription = "Upload",
                        modifier = Modifier.size(40.dp).align(Alignment.Center),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditableField("Full Name", fullName) { fullName = it }
            Spacer(modifier = Modifier.height(16.dp))

           /* Text(text = "Gender", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = gender)
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFFFF6D00))
                    }
                }
               *//* DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        )
                    }
                } *//*
            }
*/
            Spacer(modifier = Modifier.height(16.dp))
            EditableField("Email ID", email) { email = it }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Phone Number", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Row(
                        modifier = Modifier.padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🇮🇳 +91", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        VerticalDivider(modifier = Modifier.height(24.dp), thickness = 1.dp)
                    }
                },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            EditableField("City", city) { city = it }

            Spacer(modifier = Modifier.height(16.dp))
            EditableField("Pincode", pincode) { pincode = it }

            Spacer(modifier = Modifier.height(16.dp))
            EditableField("Address", address) { address = it }

            /*Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Profile Image", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
*/

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    val userId = Utils.getData(context, "user_id", "") as String
                    if (userId.isEmpty()) {
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val genderVal = if (gender == "Female") "f" else "m"
                    isLoading = true
                    
                    // First update profile
                    ServiceManager.getDataManager().updateProfile(object : Callback<OTPVerifyResponse> {
                        override fun onResponse(call: Call<OTPVerifyResponse>, response: Response<OTPVerifyResponse>) {
                            if (response.isSuccessful && response.body()?.status == 1) {
                                Utils.saveData(context, "full_name", fullName)
                                Utils.saveData(context, "email", email)
                                Utils.saveData(context, "phone", phoneNumber)
                                Utils.saveData(context, "city", city)
                                Utils.saveData(context, "pincode", pincode)
                                Utils.saveData(context, "address", address)
                                Utils.saveData(context, "gender", genderVal)
                                
                                val updatedUser = response.body()?.userInfo
                                sharedViewModel.setUserInfo(updatedUser)

                                // If an image was selected, upload it
                                selectedImageUri?.let { uri ->
                                    val file = getFileFromUri(context, uri)
                                    if (file != null) {
                                        ServiceManager.getDataManager().uploadProfileImage(object : Callback<OTPVerifyResponse> {
                                            override fun onResponse(call: Call<OTPVerifyResponse>, imageResponse: Response<OTPVerifyResponse>) {
                                                isLoading = false
                                                if (imageResponse.isSuccessful) {
                                                    val updatedUserInfo = imageResponse.body()?.userInfo
                                                    updatedUserInfo?.profileImage?.let { newUrl ->
                                                        Utils.saveData(context, "profile_image", newUrl)
                                                    }
                                                    sharedViewModel.setUserInfo(updatedUserInfo)
                                                    Toast.makeText(context, "Profile and Image updated successfully", Toast.LENGTH_SHORT).show()
                                                    onSubmit()
                                                } else {
                                                    Toast.makeText(context, "Profile updated but Image upload failed", Toast.LENGTH_SHORT).show()
                                                    onSubmit()
                                                }
                                            }

                                            override fun onFailure(call: Call<OTPVerifyResponse>, t: Throwable) {
                                                isLoading = false
                                                Toast.makeText(context, "Profile updated but Image upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
                                                onSubmit()
                                            }
                                        }, userId, file)
                                    } else {
                                        isLoading = false
                                        Toast.makeText(context, "Profile updated successfully (Image error)", Toast.LENGTH_SHORT).show()
                                        onSubmit()
                                    }
                                } ?: run {
                                    isLoading = false
                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                    onSubmit()
                                }
                            } else {
                                isLoading = false
                                Toast.makeText(context, response.body()?.message ?: "Update failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<OTPVerifyResponse>, t: Throwable) {
                            isLoading = false
                            Toast.makeText(context, t.message ?: "Network error", Toast.LENGTH_SHORT).show()
                        }
                    }, userId, fullName, address, phoneNumber, genderVal, city, pincode)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE5D10)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Submit Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun getFileFromUri(context: android.content.Context, uri: Uri): File? {
    val contentResolver = context.contentResolver
    val fileName = "temp_profile_image_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, fileName)
    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file
    } catch (e: Exception) {
        null
    }
}

@Composable
fun EditableField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
    }
}
