package com.wuliner.mylogin.file

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wuliner.mylogin.model.User
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileManager private constructor() {

    companion object {
        //创建单例对象
        private var instance: FileManager? = null
        fun sharedInstance(): FileManager {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = FileManager()
                    }
                }
            }
            return instance!!
        }
    }

    //获取文件路径
    private fun filePath(context: Context): String {
        return "${context.filesDir.path}/userInfo"
    }

    //将用户写入文件
    fun writeData(context: Context, users: List<User>) {
        FileWriter(filePath(context)).use {
            val jsonString = Gson().toJson(users)
            it.write(jsonString)
        }
    }

    //将用户读取出来
    fun readData(content: Context): List<User> {
        if (fileExist(filePath(content))) {
            FileReader(filePath(content)).use {
                val jsonText = it.readText()
                val token = object : TypeToken<List<User>>() {}
                return Gson().fromJson(jsonText, token)
            }
        }
        return emptyList()
    }

    //判断文件是否存在
    fun fileExist(path: String): Boolean {
        return File(path).exists()
    }
}