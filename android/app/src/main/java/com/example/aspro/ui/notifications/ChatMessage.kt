package com.example.aspro.ui.notifications

import android.graphics.Bitmap

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val bitmap: Bitmap? = null
)
