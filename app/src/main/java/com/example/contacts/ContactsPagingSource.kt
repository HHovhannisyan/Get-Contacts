package com.example.contacts

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.io.FileNotFoundException
import java.io.IOException

class ContactsPagingSource(private val contentResolver: ContentResolver) :
    PagingSource<Int, Contact>() {

    override fun getRefreshKey(state: PagingState<Int, Contact>): Int? =
        state.anchorPosition?.inc()


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contact> {
        val offset = params.key ?: 0
        val limit = 20
        val args = bundleOf(
            ContentResolver.QUERY_ARG_LIMIT to limit,
            ContentResolver.QUERY_ARG_OFFSET to offset,
        )
        val contacts = mutableListOf<Contact>()
        var phone = ""
        var bitmap: Bitmap? = null
        return contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null,
            args, null
        )?.takeIf { cursor ->
            cursor.count > 0
        }?.let { cursor ->
            while (cursor.moveToNext()) {
                val id =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

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
                        phone =
                            phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    phoneCursor?.close()
                }
                if (cursor.getType(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)) == Cursor.FIELD_TYPE_STRING) {
                    val imageUri =
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
            cursor.close()

            LoadResult.Page(contacts, null, offset +1)
        }.run {
            LoadResult.Error(IllegalStateException("Can't get contacts"))
        }
    }
}