package com.wuliner.mylogin.app

import android.app.Application
import com.wuliner.mylogin.user.UserManager

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //加载用户信息
        UserManager.sharedInstance(this).loadAllUserInfo()
    }
}