package com.wuliner.mylogin.user

import android.content.Context
import com.wuliner.mylogin.file.FileManager
import com.wuliner.mylogin.model.User
import java.lang.ref.WeakReference
import java.util.regex.Pattern

class UserManager private constructor(private val context: WeakReference<Context>){
    private val users = arrayListOf<User>() //保存所有用户对象
    private val fileManager = FileManager.sharedInstance()
    companion object{
        private var instance:UserManager? = null
        fun sharedInstance(context: Context):UserManager{
            if (instance == null){
                synchronized(this) {
                    if (instance == null) {
                        instance = UserManager(WeakReference(context))
                    }
                }
            }
            return instance!!
        }
    }
    /**
     * 加载用户信息
     */
    fun loadAllUserInfo() {
        fileManager.readData(context.get()!!).also {
            users.addAll(it)
        }
    }

    /**
     * 判断是否有注册的用户
     */
    fun hasUser() = users.size > 0

    /**
     * 用户名是否存在
     */
    fun chcckUserExist(name: String): Boolean {
        users.forEach {
            if (it.name == name) {
                return true
            }
        }
        return false
    }

    /**
     * 登录
     */
    fun login(name: String, password: String, type: PasswordType): Boolean {
        var user: User? = null
        users.forEach {
            if (it.name == name) {
                if (type == PasswordType.Pin) {
                    if (it.pin == password) {
                        user = it
                    }
                } else {
                    if (type == PasswordType.Pattern) {
                        user = it
                    }
                }
            }
        }
        return if (user != null) {
            currentUser()?.let { it.isLogin = false } //修改上一个用户的状态
            user?.isLogin = true //将当前登录用户状态改为true
            fileManager.writeData(context.get()!!, users)//保存用户数据
            true
        } else {
            false
        }
    }

    /**
     * 保存注册信息
     */
    fun registUser(name: String, pin: String, pattern: String) {
        //添加保存注册的用户到用户集合
        users.add(User(name, pin, pattern))
        fileManager.writeData(context.get()!!, users)
    }

    /**
     * 获取当前登录用户
     */
    fun currentUser(): User? {
        users.filter { it.isLogin }.also {
            return if (it.isNotEmpty()) {
                it[0]
            } else {
                null
            }
        }
    }



}