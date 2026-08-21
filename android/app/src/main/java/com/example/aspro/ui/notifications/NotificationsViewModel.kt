package com.example.aspro.ui.notifications

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.content

class NotificationsViewModel : ViewModel() {

    private val apiKey = ""

    fun generateResponse(prompt: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val generativeModel = GenerativeModel(modelName = "gemini-pro", apiKey = apiKey)
                val response = generativeModel.generateContent(prompt)
                withContext(Dispatchers.Main) {
                    response.text?.let {
                        callback(it)
                    } ?: callback("No response generated")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback("Error: ${e.message}")
                }
            }
        }
    }

    fun generateImageResponse(prompt: String, bitmap: Bitmap?, callback: (String) -> Unit) {
        if (bitmap == null) {
            callback("Error: No image provided")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val generativeModel = GenerativeModel(modelName = "gemini-pro-vision", apiKey = apiKey)
                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }
                val response = generativeModel.generateContent(inputContent)
                withContext(Dispatchers.Main) {
                    response.text?.let {
                        callback(it)
                    } ?: callback("No response generated")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback("Error: ${e.message}")
                }
            }
        }
    }
}
