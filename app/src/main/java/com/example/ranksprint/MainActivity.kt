package com.example.ranksprint

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ranksprint.common.Utils
import com.example.ranksprint.navigation.RankSprintNavGraph
import com.example.ranksprint.ui.theme.RankSprintTheme
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class MainActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Preload Razorpay
        //Checkout.preload(applicationContext)

        // Initialize access_token from SharedPreferences on startup
        Utils.access_token = Utils.getData(this, "access_token", "") as String

        setContent {
            RankSprintTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    RankSprintNavGraph(navController = navController)
                }
            }
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

    }

    fun startPayment(amount: Double, name: String, description: String) {
        val checkout = Checkout()
        //checkout.setKeyID("rzp_test_Se2lUwHoEfGyGd")
        checkout.setKeyID("rzp_live_Se2TObQ6KgNsZt")
        try {
            val options = JSONObject()
            options.put("name", "RankSprint")
            options.put("description", description)
            options.put("image", "https://ranksprint.org/logo.png") // Optional logo
            options.put("theme.color", "#004AAD")
            options.put("currency", "INR")
            options.put("amount", (amount * 100).toLong())
            
            val prefill = JSONObject()
            // You can get these from Utils if stored
            // prefill.put("email", "support@ranksprint.org")
            // prefill.put("contact", "")
            options.put("prefill", prefill)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        PaymentStatus.onPaymentSuccess(razorpayPaymentId)
    }

    override fun onPaymentError(code: Int, response: String?) {
        PaymentStatus.onPaymentError(code, response)
    }
}

object PaymentStatus {
    var onPaymentSuccess: (String?) -> Unit = {}
    var onPaymentError: (Int, String?) -> Unit = { _, _ -> }
}
