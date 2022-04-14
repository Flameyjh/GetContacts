package com.yjh.getContacts

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat

import android.widget.Toast

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat

import android.os.Build
import android.util.Log
import com.yjh.getContacts.ContactUtils.getAllContacts
import com.yjhpermissionx.yjhlibrary.PermissionX

/*
* 获取联系人列表: 使用Context 中的 ContentResolver 对象与 a content provider 进行通信
* 动态权限申请部分使用之前开发的 yjhPermissionX
* */

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var getContactBtn: Button
    lateinit var showContactTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        getContactBtn = findViewById(R.id.button)
        showContactTv = findViewById(R.id.text)

        getContactBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                addPermissByPermissionList()
            }

        })
    }

    //动态权限
    private fun addPermissByPermissionList() {
        PermissionX.request(
            this,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE
        ) { allGranted, deniedList ->
            if (allGranted) {
                Toast.makeText(this, "你接受了所有权限", Toast.LENGTH_SHORT).show()
                showContacts()
            } else {
                Toast.makeText(this, "你拒绝了 $deniedList", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //显示联系人列表
    private fun showContacts() {
        val contacts = getAllContacts(this@MainActivity)
        showContactTv.text = contacts.toString()
        Log.i(TAG, "contacts: $contacts")
    }
}