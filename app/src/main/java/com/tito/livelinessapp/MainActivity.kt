package com.tito.livelinessapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.core.content.ContextCompat
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.ui.liveness.ui.FaceLivenessDetector
import com.amplifyframework.ui.liveness.ui.LivenessColorScheme
import android.Manifest
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    // Define a unique request code for handling permission results
    private val CAMERA_PERMISSION_CODE = 100
    //https://ui.docs.amplify.aws/android/connected-components/liveness
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

        // Add these lines to include the Auth plugin.
        Amplify.addPlugin(AWSCognitoAuthPlugin())
        Amplify.configure(applicationContext)

        requestCameraPermission()

    }


    // Function to request camera permission
    private fun requestCameraPermission() {
        // Check if we already have camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed with your camera code
            openCamera()
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    // Handle permission results in your activity
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                openCamera()
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // This function will be called when permission is granted and you can use the camera
    private fun openCamera() {
        // Your camera code goes here, for example opening the camera intent
        setContent {
            MaterialTheme(
                colorScheme = LivenessColorScheme.default()
            ) {
                FaceLivenessDetector(
                    sessionId = "179baeef-1b43-47e7-b032-a39a372ed6a7",//179baeef-1b43-47e7-b032-a39a372ed6a7
                    region = "us-east-1",
                    onComplete = {
                        Log.i("MyApp", "Face Liveness flow is complete")
                        // The Face Liveness flow is complete and the session
                        // results are ready. Use your backend to retrieve the
                        // results for the Face Liveness session.
                    },
                    onError = { error ->
                        Log.e("MyApp", "Error during Face Liveness flow",)
                        // An error occurred during the Face Liveness flow, such as
                        // time out or missing the required permissions.
                    }
                )
            }
        }
    }

}