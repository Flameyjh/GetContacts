package com.yjhpermissionx.yjhlibrary

import androidx.fragment.app.FragmentActivity

//对外接口部分
//单例类，让接口更方便被调用

object PermissionX {

    private const val TAG = "InvisibleFragment"

    //供外部调用
    fun request(activity: FragmentActivity, vararg permission: String, callback: PermissionCallback){
        val fragmentManager = activity.supportFragmentManager
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if (existedFragment != null){ //判断是否存在指定TAG的实例，不存在就创建一个
            existedFragment as InvisibleFragment
        }else{
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }
        fragment.requestNow(callback, *permission) //*表示将数组变成可变长度参数
    }
}