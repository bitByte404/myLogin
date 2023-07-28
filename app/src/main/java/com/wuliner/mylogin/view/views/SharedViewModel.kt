package com.wuliner.mylogin.view.views

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wuliner.mylogin.model.User
import com.wuliner.mylogin.user.PasswordType
import com.wuliner.mylogin.user.UserManager

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private var userManager = UserManager.sharedInstance(application)

    //是否实现跳转到图案界面的文本
    val showChangeToPatternLogin = MutableLiveData(false)

    //按钮是否能点击
    val loginBtnIsEnabled = MutableLiveData(false)
    val registerBtnIsEnabled = MutableLiveData(false)

    //登录结果
    val loginState = MutableLiveData(LoginState.Default)

    //记录用户名和密码
    var user = User()

    //登录的用户
    val loginedUser = MutableLiveData(User())

    /**改变登录按钮的状态*/
    fun changeLoginButtonState(state: ButtonState) {
        loginBtnIsEnabled.postValue(state == ButtonState.Enabled)
    }

    /**改变注册按钮的状态*/
    fun changeRegisterButtonState(state: ButtonState) {
        registerBtnIsEnabled.postValue(state == ButtonState.Enabled)
    }

    /**用户登录*/
    fun login(name: String, password: String, type: PasswordType) {
        val result = userManager.login(name, password, type)
        val state = if (result) LoginState.Success else LoginState.Failure
        loginState.postValue(state)
    }

    /**用户注册*/
    fun register() {
        userManager.registUser(user.name, user.pin, user.pattern)
        showChangeToPatternLogin.postValue(userManager.hasUser())
    }

    fun loadUserInfo(name: String) {
        val user = userManager.getUserInfo(name) ?: return
        loginedUser.postValue(user)
    }
}
//按钮状态
enum class ButtonState {
    Enabled, UnEnabled
}
//登录状态
enum class LoginState {
    Success, Failure, Default
}