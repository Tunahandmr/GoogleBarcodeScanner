package com.tunahan.googlebarcodescanner

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.tunahan.googlebarcodescanner.ui.theme.GoogleBarcodeScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoogleBarcodeScannerTheme {
                GoogleScanner()
            }
        }
    }
}

@Composable
fun GoogleScanner() {
    val context = LocalContext.current
    var result by remember { mutableStateOf("Click Button!") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(result)

        Button(onClick = {
            val options = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE
                )
                .build()

            val scanner = GmsBarcodeScanning.getClient(context, options)

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    // Task completed successfully
                    val rawValue = barcode.rawValue

                    if (!rawValue.isNullOrEmpty()) {
                        result = rawValue
                    }
                }
                .addOnCanceledListener {
                    // Task canceled
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Scan QR")
        }
    }
}
