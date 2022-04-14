package com.yjhpermissionx.yjhlibrary

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

//不可见的fragment，用来处理运行时权限


typealias PermissionCallback = (Boolean, List<String>) -> Unit //typealias关键字给类型指定别名

class InvisibleFragment : Fragment() {

    //权限申请结果的回调通知方式（第一个参数表示是否全部权限被允许，第二个参数表示被拒绝的权限列表）
    private var callback: PermissionCallback? = null

    //供调用，第一个参数与callback参数类型相同，第二个参数是可变长度的权限申请列表
    fun requestNow(cb: PermissionCallback, vararg permission: String){
        callback = cb
        requestPermissions(permission, 1) //Fragment提供的权限申请方法
    }

    //处理权限申请结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            val deniedList = ArrayList<String>() //记录被用户拒绝的权限
            for ((index, result) in grantResults.withIndex()){
                if (result != PackageManager.PERMISSION_GRANTED){
                    deniedList.add(permissions[index])
                }
            }
            val allGranted = deniedList.isEmpty()
            callback?.let {
                it(allGranted, deniedList)
            }
        }
    }
}