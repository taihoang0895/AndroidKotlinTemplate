package com.example.androidkotlintemplate.arch

import android.os.Bundle
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
import com.example.androidkotlintemplate.R

private const val DIALOG_STYLE_KEY = "DIALOG_STYLE_KEY"
private const val DIALOG_EVENT_ACTION_OK_SELECTED = "DIALOG_EVENT_ACTION_OK_SELECTED"
private const val DIALOG_EVENT_ACTION_CANCEL_SELECTED = "DIALOG_EVENT_ACTION_CANCEL_SELECTED"

typealias DialogConfirmListener = (action: String) -> Unit

abstract class BaseActivity : AppCompatActivity() {
    protected abstract fun getLayoutResId(): Int
    protected abstract fun findViewById()
    protected abstract fun bindEvents()

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
        dialog?.window?.let {
            it.requestFeature(Window.FEATURE_NO_TITLE)
        }
        val view = inflater.inflate(getLayoutResId(), container, false)
        initViews(view, savedInstanceState)

        return view
    }

    protected fun initViews(view: View?, savedInstanceState: Bundle?) {

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
        arguments?.let {
            it.putInt(DIALOG_STYLE_KEY, style)

        } ?: let {
            val bundle = Bundle()
            bundle.putInt(DIALOG_STYLE_KEY, style)
        }
    }
}

private enum class ConfirmDialogArgumentKey {
    TITLE_KEY,
    CONTENT_KEY,
    TYPE_KEY,
    NO_TITLE_KEY,
    POSITIVE_BUTTON_TEXT_KEY,
    NEGATIVE_BUTTON_TEXT_KEY
}

class ConfirmDialog : BaseDialog() {
    companion object {
        val TAG: String = "ConfirmDialog"
    }

    enum class Type {
        ONLY_POSITIVE_BUTTON,
        ONLY_NEGATIVE_BUTTON,
        BOTH

    }

    class Builder {
        var mTitle: String = ""
        var mContent: String = ""
        var mType: Type = Type.BOTH
        var mNoTitle = false
        var mPositiveButtonText = ""
        var mNegativeButtonText = ""

        fun build(): ConfirmDialog {
            var confirmDialog = ConfirmDialog()
            val bundle = Bundle()
            bundle.putString(ConfirmDialogArgumentKey.TITLE_KEY.name, mTitle)
            bundle.putString(ConfirmDialogArgumentKey.CONTENT_KEY.name, mContent)
            bundle.putString(ConfirmDialogArgumentKey.TYPE_KEY.name, mType.name)
            bundle.putBoolean(ConfirmDialogArgumentKey.NO_TITLE_KEY.name, mNoTitle)
            bundle.putString(
                ConfirmDialogArgumentKey.POSITIVE_BUTTON_TEXT_KEY.name,
                mPositiveButtonText
            )
            bundle.putString(
                ConfirmDialogArgumentKey.NEGATIVE_BUTTON_TEXT_KEY.name,
                mNegativeButtonText
            )

            confirmDialog.arguments = bundle
            return confirmDialog
        }
    }

    override fun getLayoutResId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class LoadingDialog : BaseDialog() {
    override fun getLayoutResId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
