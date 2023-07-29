package com.wuliner.mylogin.view.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.wuliner.mylogin.R
import com.wuliner.mylogin.databinding.UserInputViewBinding

class UserInputView(context: Context, attrs: AttributeSet?): FrameLayout(context, attrs) {
    private var title: String
    private var errorTitle: String
    private val binding: UserInputViewBinding by lazy {
        UserInputViewBinding.inflate(LayoutInflater.from(context))
    }
    var textChangeListener: ((UserInputView, String) -> Unit)? = null
    val text: String
        get() = binding.inputTextView.text.toString()

    init {
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(binding.root, lp)

        //解析属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UserInputView)

        title = typedArray.getString(R.styleable.UserInputView_title)?:"设置标题"
        val placeholder = typedArray.getString(R.styleable.UserInputView_placeholder)
        val inputType = typedArray.getInteger(R.styleable.UserInputView_input_type, 2)
        errorTitle = typedArray.getString(R.styleable.UserInputView_error_title)?:title

        binding.titleTextView.text = title
        binding.inputTextView.hint = placeholder

        //判断输入的是密码还是正常的
        if (inputType == 1) {
            binding.inputTextView.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        //监听输入
        binding.inputTextView.addTextChangedListener (
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                //将内容传递给外部
                textChangeListener?.let {

                    it(this, text.toString())
                }
            }
        )

        typedArray.recycle()
    }
}