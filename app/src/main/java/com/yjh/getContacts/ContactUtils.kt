package com.yjh.getContacts

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log

/**
 * 获取通讯录工具类
 */

object ContactUtils {

    fun getAllContacts(context: Context): ArrayList<MyContacts> {
        val contacts = ArrayList<MyContacts>()
        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            //新建一个联系人实例
            val temp = MyContacts(null, null, null)
            val contactId =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))

            //获取联系人姓名
            val name =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
            temp.name = name

            //获取联系人电话号码
            val phoneCursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                null,
                null
            )
            while (phoneCursor?.moveToNext() == true) {
                var phone =
                    phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phone = phone.replace("-", "")
                phone = phone.replace(" ", "")
                temp.phone = phone
            }

            //获取联系人备注信息
            val noteCursor = context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                arrayOf(ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Nickname.NAME),
                ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "='"
                        + ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE + "'",
                arrayOf(contactId),
                null
            )
            if (noteCursor?.moveToFirst() == true) {
                do {
                    val note: String = noteCursor.getString(
                        noteCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Nickname.NAME)
                    )
                    temp.note = note
                } while (noteCursor.moveToNext())
            }
            contacts.add(temp)
            Log.i("ContactUtils: Contact==", temp.toString())

            //记得要把cursor给close掉
            phoneCursor?.close()
            noteCursor?.close()
        }

        cursor?.close()
        return contacts
    }

}
