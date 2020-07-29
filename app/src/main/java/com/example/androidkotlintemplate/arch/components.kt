package com.example.androidkotlintemplate.arch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

private const val DIALOG_STYLE_KEY = "DIALOG_STYLE_KEY"
private const val DIALOG_EVENT_ACTION_OK_SELECTED = "DIALOG_EVENT_ACTION_OK_SELECTED"
private const val DIALOG_EVENT_ACTION_CANCEL_SELECTED = "DIALOG_EVENT_ACTION_CANCEL_SELECTED"

typealias DialogConfirmListener = (action: String) -> Unit

abstract class BaseActivity : AppCompatActivity() {
    protected abstract fun getLayoutResId(): Int
    protected abstract fun findViewById()
    protected abstract fun bindEvents()
    val mMainThread = Handler(Looper.getMainLooper())

    protected open fun onPreCreate(): Boolean {
        return true
    }

    protected open fun onPostCreate() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!onPreCreate())
            return;

        onCreateInit(savedInstanceState);
    }

    protected fun onCreateInit(savedInstanceState: Bundle?) {
        setContentView(getLayoutResId())
        findViewById()
        bindEvents()
        initData()

    }

    protected open fun initData() {

    }

}

abstract class BaseFragment : Fragment() {
    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    protected abstract fun findViewById(view: View)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutResId(), container, false)
        findViewById(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}


abstract class BaseDialog : DialogFragment() {
    protected abstract fun getLayoutResId(): Int

    override fun show(manager: FragmentManager, tag: String?) {
        validateDialogTag(tag)
        super.show(manager, tag)
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        TODO("not implemented")
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        validateDialogTag(tag)
        super.showNow(manager, tag)
    }

    private fun validateDialogTag(tag: String?) {
        tag?.let {
            if (it.isEmpty()) {
                throw IllegalArgumentException("tag is not empty")
            }
        } ?: throw IllegalArgumentException("tag is not null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(getLayoutResId(), container, false)
        initViews(view, savedInstanceState)
        bindEvents()
        return view
    }

    open protected fun initViews(view: View, savedInstanceState: Bundle?) {

    }

    open protected fun bindEvents() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.let { window ->
            arguments?.let {
                val style = it.getInt(DIALOG_STYLE_KEY, -1)
                if (style != -1) {
                    window.attributes.windowAnimations = style
                }
            }
        }
    }

    fun setStyle(style: Int) {
        arguments?.putInt(DIALOG_STYLE_KEY, style) ?: let {
            val bundle = Bundle()
            bundle.putInt(DIALOG_STYLE_KEY, style)
        }
    }
}

