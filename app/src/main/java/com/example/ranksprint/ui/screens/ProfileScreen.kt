package com.example.ranksprint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController
import com.example.ranksprint.ui.viewmodels.ProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ranksprint.R

import com.example.ranksprint.ui.viewmodels.SharedViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToContact: () -> Unit,
    onNavigateToFaq: () -> Unit,
    onNavigateToEnquiry: () -> Unit,
    onNavigateToSubscriptions: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val apiUserInfo by viewModel.userInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val sharedUserInfo by sharedViewModel.userInfo.collectAsState()
    val userInfo = apiUserInfo ?: sharedUserInfo

    LaunchedEffect(apiUserInfo) {
        apiUserInfo?.let {
            sharedViewModel.setUserInfo(it)
        }
    }

    LaunchedEffect(Unit) {
        val userId = Utils.getData(context, "user_id", "") as String
        viewModel.fetchProfile(userId)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with Blue Background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE1F5FE))
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        ) {
                            AsyncImage(
                                model = userInfo?.profileImage ?: "",
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.banner_1),
                                error = painterResource(R.drawable.banner_1)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userInfo?.fullName ?: "User",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                            Text(
                                text = userInfo?.email ?: "No Email",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF546E7A)
                            )
                            Text(
                                text = userInfo?.phone ?: "No Phone",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF546E7A)
                            )
                        }
                        IconButton(onClick = onNavigateToEditProfile) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color(0xFFFF6D00)
                            )
                        }
                    }
                }

                // General Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF5F5F5)
                ) {
                    Text(
                        text = "General",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF263238)
                    )
                }

                // Menu Items
                Column(modifier = Modifier.fillMaxWidth()) {
                    ProfileMenuItem(Icons.Outlined.Info, "About US", onClick = onNavigateToAboutUs)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    
                    ProfileMenuItem(Icons.Outlined.Lock, "Privacy Policy", onClick = onNavigateToPrivacy)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    
                    ProfileMenuItem(Icons.Outlined.Description, "Terms & Conditions", onClick = onNavigateToTerms)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    
                    ProfileMenuItem(Icons.Outlined.Call, "Contact US", onClick = onNavigateToContact)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    
                    ProfileMenuItem(Icons.Outlined.HelpOutline, "FAQ's", onClick = onNavigateToFaq)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    
                    ProfileMenuItem(Icons.Outlined.Email, "Enquiry Form", onClick = onNavigateToEnquiry)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)

                    ProfileMenuItem(Icons.Outlined.CreditCard, "Subscriptions", onClick = onNavigateToSubscriptions)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onLogout() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFFF6D00)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Logout",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFFF6D00),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFFF6D00)
                )
            }
            
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
