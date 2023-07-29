package com.wuliner.mylogin.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.wuliner.mylogin.R
import com.wuliner.mylogin.databinding.FragmentPatternRegisterBinding
import com.wuliner.mylogin.databinding.FragmentPinRegistBinding
import com.wuliner.mylogin.tool.navigateTo
import com.wuliner.mylogin.view.views.SharedViewModel


class PatternRegisterFragment : Fragment() {
    private lateinit var binding: FragmentPatternRegisterBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private var mPassword = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPatternRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alertTextView.text = "请设置图案密码"
        binding.dotView.addDrawFinishedListerner { dotView, password ->
            if (mPassword.isEmpty()) {
                mPassword = password
                binding.alertTextView.text = "请重复输入密码"
            } else {
                Log.v("cw", "$mPassword $password")
                if (mPassword == password) {
                    binding.alertTextView.text = "密码设置成功"
                    viewModel.user.pattern = password //将图案密码保存到user对象中
                    viewModel.register() //注册
                    navigateTo(PinLoginFragment())
                } else {
                    //显示错误
                    binding.alertTextView.text = "两次密码不同 请重试"
                    mPassword = ""
                    dotView.showError()
                }
            }
        }
    }



}