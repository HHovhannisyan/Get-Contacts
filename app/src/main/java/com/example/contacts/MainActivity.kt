package com.example.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor.FIELD_TYPE_STRING
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_CONTACTS = 79
    private lateinit var binding: ActivityMainBinding
    private var contacts = mutableListOf<Contact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        setupPermissions()


        binding.apply {
            rvList.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = RvAdapter(this@MainActivity, contacts)

            }
        }
        getContacts()
    }

    private fun getContacts() {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null,
            null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        var phone = ""
        var imageUri: String
        var name: String
        var bitmap: Bitmap? = null
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                )
                name = cursor
                    .getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

                if (cursor.getString(
                        cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    ).toInt() > 0
                ) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = ?", arrayOf(id), null
                    )
                    while (phoneCursor != null && phoneCursor.moveToNext()) {
                        phone = phoneCursor
                            .getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    phoneCursor?.close()
                }
                if (cursor.getType(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)) == FIELD_TYPE_STRING) {
                    imageUri =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
                    if (!imageUri.isNullOrEmpty()) {
                        try {
                            bitmap = MediaStore.Images.Media
                                .getBitmap(this.contentResolver, Uri.parse(imageUri))
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    bitmap = null
                }
                contacts.add(Contact(name, phone, bitmap))
            }
        }
        cursor?.close()
    }


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            println("Permission to read contacts denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            REQUEST_READ_CONTACTS
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_CONTACTS -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    println("Permission has been denied by user")
                } else {
                    println("Permission has been granted by user")
                }
            }
        }
    }
}
