package com.wuliner.mylogin.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.wuliner.mylogin.R
import com.wuliner.mylogin.databinding.FragmentPatternRegisterBinding
import com.wuliner.mylogin.databinding.FragmentPinRegistBinding
import com.wuliner.mylogin.tool.AnimTools
import com.wuliner.mylogin.tool.navigateTo
import com.wuliner.mylogin.view.views.ButtonState
import com.wuliner.mylogin.view.views.SharedViewModel


class PinRegistFragment : Fragment() {
    lateinit var binding: FragmentPinRegistBinding
    private lateinit var viewModel: SharedViewModel

    //记录用户输入用户名 密码 确认密码
    private var name = ""
    private var password = ""
    private var confirmPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinRegistBinding.inflate(layoutInflater, container, false)

        //监听登录是否能点击
        viewModel.registerBtnIsEnabled.observe(viewLifecycleOwner) {
            binding.button.isEnabled = it
        }

        //监听用户名的输入
        binding.nameInputView.textChangeListener = { userInputView, text ->
            if (text.isNotEmpty() && binding.passwordInputView.text.isNotEmpty() && binding.confirmPasswordTextView.text.isNotEmpty()) {
                viewModel.changeRegisterButtonState(ButtonState.Enabled)
                name = text
            } else {
                viewModel.changeRegisterButtonState(ButtonState.UnEnabled)
            }
        }
        //监听密码的输入
        binding.passwordInputView.textChangeListener = { userInputView, text ->
            if (text.isNotEmpty() && binding.nameInputView.text.isNotEmpty() && binding.confirmPasswordTextView.text.isNotEmpty()) {
                viewModel.changeRegisterButtonState(ButtonState.Enabled)
                Log.v("test", "password $password text: $text")
                password = text
            } else {
                viewModel.changeRegisterButtonState(ButtonState.UnEnabled)
            }
        }

        //监听确认密码的输入
        binding.confirmPasswordTextView.textChangeListener = { userInputView, text ->
            if (text.isNotEmpty() && binding.nameInputView.text.isNotEmpty() && binding.passwordInputView.text.isNotEmpty()) {
                viewModel.changeRegisterButtonState(ButtonState.Enabled)
                confirmPassword = text
            } else {
                viewModel.changeRegisterButtonState(ButtonState.UnEnabled)
            }
        }

        binding.button.setOnClickListener {
            checkPassword()
        }

        return binding.root
    }

    private fun checkPassword() {
        if (password == confirmPassword) {
            viewModel.user.name = name
            viewModel.user.pin = password
            navigateTo(PatternRegisterFragment())
        } else {
            binding.passwordInputView.showError()
            binding.confirmPasswordTextView.showError()
            AnimTools.startSwingAnim(binding.button, time = 100)
        }
    }

}