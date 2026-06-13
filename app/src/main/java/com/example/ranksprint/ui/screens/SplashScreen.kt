package com.example.ranksprint.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.ranksprint.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize(),

        contentAlignment = Alignment.Center
    ) {
        // Background pattern
        Image(
            painter =  painterResource(R.drawable.splash_bg_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        // Logo
       /* Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "RankSprint Logo",
            modifier = Modifier
                .size(280.dp)
                .padding(16.dp)
        )*/
    }
}
