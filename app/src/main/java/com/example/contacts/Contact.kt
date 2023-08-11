package com.example.contacts

import android.graphics.Bitmap

data class Contact(
    var contactName:String,
    val phoneNumber:String,
    val img: Bitmap?
)