package com.yyide.chatim_pro.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.PopupTextShowBinding
import razerdp.basepopup.BasePopupWindow

/**
 *
 * @author shenzhibin
 * @date 2022/3/26 16:52
 * @description 文字popup
 */
class TextPopUp : BasePopupWindow {

    private lateinit var binding: PopupTextShowBinding

    constructor(context: Context) : super(context)

    constructor(fragment: Fragment) : super(fragment)

    init {
        setContentView(R.layout.popup_text_show)
        setBackground(0)
        popupGravity = Gravity.TOP or Gravity.CENTER
    }

    override fun onViewCreated(contentView: View) {
        binding = PopupTextShowBinding.bind(contentView)
    }

    fun setText(content: String,view:View) {
        binding.popupTextShowTv.text = content
        showPopupWindow(view)
    }
}