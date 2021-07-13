package com.yyide.chatim.base

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.WebViewActivity
import com.yyide.chatim.databinding.FragmentWebBinding
import com.yyide.chatim.model.WebModel

private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [WebFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WebFragment : BaseFragment() {
    private var url: String? = null
    private var mWebView: WebView? = null
    private var webSettings: WebSettings? = null
    private lateinit var fragmentWebBinding: FragmentWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        fragmentWebBinding = FragmentWebBinding.inflate(layoutInflater)
        return fragmentWebBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebView()
    }

    @SuppressLint("JavascriptInterface")
    private fun initWebView() {
        mWebView = WebView(Utils.getApp().applicationContext)
        fragmentWebBinding.flContent.addView(mWebView)
        mWebView!!.overScrollMode = View.OVER_SCROLL_NEVER
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenUtils.getScreenHeight() - SizeUtils.dp2px(50f)))
        mWebView!!.layoutParams = layoutParams
        webSettings = mWebView!!.settings
        webSettings!!.loadsImagesAutomatically = true

//        webSettings.setMixedContentMode(WebSettings.);

        //设置自适应屏幕，两者合用
        webSettings!!.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings!!.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings!!.useWideViewPort = true
        //允许js代码
        webSettings!!.javaScriptEnabled = true
        //允许SessionStorage/LocalStorage存储
        webSettings!!.domStorageEnabled = true
        webSettings!!.blockNetworkImage = false

        //缩放操作
        webSettings!!.setSupportZoom(false) //支持缩放，默认为true。是下面那个的前提。
        webSettings!!.builtInZoomControls = false //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings!!.displayZoomControls = true //隐藏原生的缩放控件
        webSettings!!.allowFileAccess = true
        mWebView!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("MyApplication", consoleMessage?.message() + " -- From line "
                        + consoleMessage?.lineNumber() + " of "
                        + consoleMessage?.sourceId())
                return true;
            }
        }
        mWebView!!.loadUrl(url)
        mWebView!!.addJavascriptInterface(WebFragment(), "android")
        mWebView!!.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (SpData.User() != null) {
                    Log.d("onPageFinished", "SpData.User().getToken(:" + SpData.User().token)
                    //mWebView!!.loadUrl("javascript:sendH5Event('" + "setToken" + "','" + SpData.User().token + "')")
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WebFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(url: String) =
                WebFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, url)
                    }
                }
    }

    override fun onDestroy() {
        if (mWebView != null) {
            mWebView!!.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            mWebView!!.clearHistory()
            fragmentWebBinding.flContent.removeView(mWebView)
            mWebView!!.destroy()
            mWebView = null
        }
        super.onDestroy()
    }

}