package com.example.ranksprint.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ranksprint.R
import com.example.ranksprint.ui.components.BottomNavigationBar
import com.example.ranksprint.ui.viewmodels.ContactUsViewModel
import com.example.ranksprint.ui.viewmodels.PolicyViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyInfoScreen(
    navController: NavController,
    title: String,
    painetr: Painter,
    pageName: String,
    onNavigateBack: () -> Unit,
    viewModel: PolicyViewModel = viewModel()
) {
    val pageDataMap by viewModel.pageData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val pageData = pageDataMap[pageName]

    LaunchedEffect(pageName) {
        viewModel.fetchPage(pageName)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading && pageData == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null && pageData == null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painetr,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = pageData?.information_title ?: title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004AAD),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                setBackgroundColor(0) // Transparent
                            }
                        },
                        update = { webView ->
                            val htmlContent = """
                                <html>
                                <head>
                                <style>
                                    body {
                                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                                        font-size: 16px;
                                        line-height: 1.5;
                                        color: #616161;
                                        margin: 0;
                                        padding: 0;
                                    }
                                </style>
                                </head>
                                <body>
                                    ${pageData?.description ?: ""}
                                </body>
                                </html>
                            """.trimIndent()
                            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// Specific Policy Screens
@Composable
fun AboutUsScreen(navController: NavController, onNavigateBack: () -> Unit) {
    PolicyInfoScreen(
        navController = navController,
        title = "About Us",
        painetr = painterResource(R.drawable.about_logo),
        pageName = "about-us",
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(
    navController: NavController,
    onNavigateBack: () -> Unit,
    viewModel: ContactUsViewModel = viewModel()
) {
    val localContext = LocalContext.current
    val contactInfo by viewModel.contactInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchContactDetails()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading && contactInfo == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null && contactInfo == null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Image(
                        painter = painterResource(id = R.drawable.contact_us_logo),
                        contentDescription = "Contact Illustration",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "In any case of query please feel free to\ncontact us",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    if(contactInfo?.email?.isNotEmpty() == true)
                    Spacer(modifier = Modifier.height(32.dp))

                    if(contactInfo?.email?.isNotEmpty() == true)
                    ContactItem(
                        icon = Icons.Default.Email,
                        text = contactInfo?.email ?: "",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${contactInfo?.email ?: "support@ranksprint.org"}")
                            }
                            localContext.startActivity(intent)
                        }
                    )
                    if(contactInfo?.phone?.isNotEmpty() == true)
                    Spacer(modifier = Modifier.height(16.dp))
                if(contactInfo?.phone?.isNotEmpty() == true)
                    ContactItem(
                        icon = Icons.Default.LocalPhone,
                        text = contactInfo?.phone ?: "",
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${contactInfo?.phone ?: "+91 1234567892"}")
                            }
                            localContext.startActivity(intent)
                        }
                    )
                    if(contactInfo?.phone2?.isNotEmpty() == true)
                    Spacer(modifier = Modifier.height(16.dp))
            if(contactInfo?.phone2?.isNotEmpty() == true)
                    ContactItem(
                        icon = Icons.Default.ChatBubbleOutline,
                        text = contactInfo?.phone2 ?: "",
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${contactInfo?.phone2 ?: "+91 9052 803 84"}")
                            }
                            localContext.startActivity(intent)
                        }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                                append(contactInfo?.address ?: "")
                            }
                           /* append("\n\n")
                            withStyle(style = SpanStyle(color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold)) {
                                append(contactInfo?.email ?: "")
                            }*/
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ContactItem(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFE8F5E9),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(10.dp),
                tint = Color(0xFF4CAF50)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
    }
}

@Composable
fun PrivacyPolicyScreen(navController: NavController, onNavigateBack: () -> Unit) {
    PolicyInfoScreen(
        navController = navController,
        title = "Privacy Policy",
        painetr = painterResource(R.drawable.privacy_logo),
        pageName = "privacy-policy",
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun TermsConditionsScreen(navController: NavController, onNavigateBack: () -> Unit) {
    PolicyInfoScreen(
        navController = navController,
        title = "Terms & Conditions",
        painetr = painterResource(R.drawable.terms_logo),
        pageName = "terms-and-conditions",
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun RefundPolicyScreen(navController: NavController, onNavigateBack: () -> Unit) {
    PolicyInfoScreen(
        navController = navController,
        title = "Refund Policy",
        painetr = painterResource(R.drawable.refund_logo),
        pageName = "refund-policy",
        onNavigateBack = onNavigateBack
    )
}
