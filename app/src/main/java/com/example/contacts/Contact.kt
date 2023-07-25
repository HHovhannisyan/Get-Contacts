package com.example.contacts

import android.graphics.Bitmap

data class Contact(
    val contactName:String,
    val phoneNumber:String,
    val img: Bitmap?
)