package com.wuliner.mylogin.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.wuliner.mylogin.R
import com.wuliner.mylogin.databinding.FragmentPinLoginBinding
import com.wuliner.mylogin.view.views.ButtonState
import com.wuliner.mylogin.view.views.SharedViewModel

class PinLoginFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentPinLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinLoginBinding.inflate(inflater, container, false)

        //监听跳转到图案解锁文本是否显示状态
        viewModel.showChangeToPatternLogin.observe(viewLifecycleOwner) {
            binding.swithToPatternTextView.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        //监听登录是否能点击
        viewModel.loginBtnIsEnabled.observe(viewLifecycleOwner) {
            binding.button.isEnabled = it
        }

        //监听用户名的输入
        binding.nameInputView.textChangeListener = { userInputView, text ->
            if (text.isNotEmpty() && binding.passwordInputView.text.isNotEmpty()) {
                viewModel.changeLoginButtonState(ButtonState.Enabled)
            } else {
                viewModel.changeLoginButtonState(ButtonState.UnEnabled)
            }
        }
        //监听密码的输入
        binding.passwordInputView.textChangeListener = { userInputView, text ->
            if (text.isNotEmpty() && binding.nameInputView.text.isNotEmpty()) {
                viewModel.changeLoginButtonState(ButtonState.Enabled)
            } else {
                viewModel.changeLoginButtonState(ButtonState.UnEnabled)
            }
        }

        return binding.root
    }

}