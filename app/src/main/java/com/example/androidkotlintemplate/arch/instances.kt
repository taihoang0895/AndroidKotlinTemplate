package com.example.androidkotlintemplate.arch

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.androidkotlintemplate.R
import com.example.androidkotlintemplate.Util.PermissionUtil

private enum class ConfirmDialogArgumentKey {
    TITLE_KEY, CONTENT_KEY, TYPE_KEY, NO_TITLE_KEY, POSITIVE_BUTTON_TEXT_KEY, NEGATIVE_BUTTON_TEXT_KEY
}

abstract class PermissionActivity : BaseActivity() {

    val SETTING_APP_STORAGE_REQUEST_CODE = 102
    val SETTING_APP_CAMERA_REQUEST_CODE = 103

    private lateinit var mPermissionDialog: ConfirmDialog

    protected fun hasPermissionsStorage(): Boolean {
        return PermissionUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    protected fun hasPermissionCamera(): Boolean {
        return PermissionUtil.hasPermission(this, Manifest.permission.CAMERA)
    }

    fun hidePermissionDialog() {
        mPermissionDialog.dismiss()
    }

    protected fun showExplanationToGrantStoragePermission() {
        var builder: ConfirmDialog.Builder = ConfirmDialog.Builder()
        builder.mNoTitle = true
        builder.mContent = getString(R.string.permissions_txt_write_external_storage_permission_confirmation)
        builder.mType = ConfirmDialog.Type.ONLY_POSITIVE_BUTTON
        mPermissionDialog = builder.build()
        mPermissionDialog.isCancelable = false
        mPermissionDialog.setActionListener(object : ConfirmDialog.ActionListener {
            override fun onOkButtonClicked() {
                hidePermissionDialog()
                if (PermissionUtil.clickedOnNeverAskAgain(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, SETTING_APP_STORAGE_REQUEST_CODE)
                } else {
                    PermissionUtil.requestPermission(this@PermissionActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PermissionUtil.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION)
                }
            }

            override fun onCancelButtonClicked() {
                hidePermissionDialog()
            }
        })
        mPermissionDialog.show(supportFragmentManager, ConfirmDialog.TAG)
        Log.d("PERMISSION", " end showExplanationToGrantStoragePermission")
    }

    protected fun showExplanationToGrantCameraPermission() {
        val builder: ConfirmDialog.Builder = ConfirmDialog.Builder()
        builder.mNoTitle = true
        builder.mContent = resources.getString(R.string.permissions_txt_camera_confirmation)
        builder.mType = ConfirmDialog.Type.ONLY_POSITIVE_BUTTON
        mPermissionDialog = builder.build()
        mPermissionDialog.isCancelable = false;

        mPermissionDialog.setActionListener(object : ConfirmDialog.ActionListener {
            override fun onOkButtonClicked() {
                hidePermissionDialog()
                if (PermissionUtil.clickedOnNeverAskAgain(arrayOf(Manifest.permission.CAMERA))) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, SETTING_APP_CAMERA_REQUEST_CODE)
                } else {
                    PermissionUtil.requestPermission(this@PermissionActivity, arrayOf(Manifest.permission.CAMERA), PermissionUtil.REQUEST_CAMERA_PERMISSION)
                }
            }

            override fun onCancelButtonClicked() {
                hidePermissionDialog()
            }
        })

        mPermissionDialog.show(supportFragmentManager, mPermissionDialog.tag)
        Log.d("PERMISSION", " end showExplanationToGrantCameraPermission")
    }

    protected fun requestPermissionStorage() {
        if (!PermissionUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (PermissionUtil.clickedOnNeverAskAgain(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                showExplanationToGrantStoragePermission()
            } else {
                PermissionUtil.requestPermission(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PermissionUtil.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION)
            }
        } else {
            hidePermissionDialog()
            onPermissionGranted()
        }
    }

    protected fun requestPermissionCamera() {
        if (!PermissionUtil.hasPermission(this, Manifest.permission.CAMERA)) {
            if (PermissionUtil.clickedOnNeverAskAgain(arrayOf(Manifest.permission.CAMERA))) {
                showExplanationToGrantCameraPermission()
            } else {
                PermissionUtil.requestPermission(this, arrayOf(Manifest.permission.CAMERA), PermissionUtil.REQUEST_CAMERA_PERMISSION)
            }
        } else {
            hidePermissionDialog()
            onPermissionGranted()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SETTING_APP_STORAGE_REQUEST_CODE -> {
                mMainThread.postDelayed({
                    if (hasPermissionsStorage()) {
                        onPermissionGranted()
                    }
                }, 100)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionUtil.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION -> {
                PermissionUtil.onRequestPermissionsResult(this, permissions, grantResults.toTypedArray())
                if (PermissionUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    onPermissionGranted()
                }
            }
            PermissionUtil.REQUEST_CAMERA_PERMISSION -> {
                PermissionUtil.onRequestPermissionsResult(this, permissions, grantResults.toTypedArray())
                if (PermissionUtil.hasPermission(this, Manifest.permission.CAMERA)) {
                    onPermissionGranted()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onPermissionGranted() {
    }

}

abstract class LoadingActivity : BaseActivity() {
    val TAG = "LoadingActivity"

    private enum class State {
        SHOW_LOADING_DIALOG, HIDE_LOADING_DIALOG
    }

    private lateinit var mLoadingDialog: LoadingDialog
    private var mState = State.HIDE_LOADING_DIALOG

    override fun onPreCreate(): Boolean {
        mLoadingDialog = LoadingDialog.newInstance(getString(R.string.please_wait))
        mLoadingDialog.isCancelable = false
        return super.onPreCreate()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart ")
    }

    override fun onResume() {
        super.onResume()
        if (mState == State.SHOW_LOADING_DIALOG) {
            if (!isLoadingDialogVisible()) {
                showLoadingDialog()
            }
        } else {
            hideLoadingDialog()
        }
        Log.d(TAG, "onResume ")
    }

    protected fun showLoadingDialog(title: String? = getString(R.string.please_wait)) {
        mState = State.SHOW_LOADING_DIALOG
        if (Thread.currentThread() != Looper.getMainLooper().thread) {
            mMainThread.post(object : Runnable {
                override fun run() {
                    Log.d(TAG, "show dialog from class " + this.javaClass.simpleName)
                    if (mState == State.SHOW_LOADING_DIALOG) {
                        dismissLoadingDialog()
                        mLoadingDialog = LoadingDialog.newInstance(title)
                        mLoadingDialog.show(supportFragmentManager, TAG)
                    }
                }
            })
        } else {
            Log.d(TAG, "show dialog from class " + this.javaClass.simpleName)
            dismissLoadingDialog()
            mLoadingDialog = LoadingDialog.newInstance(title)
            mLoadingDialog.show(supportFragmentManager, TAG)
        }
    }

    protected fun hideLoadingDialog() {
        mState = State.HIDE_LOADING_DIALOG
        dismissLoadingDialog()
    }

    private fun dismissLoadingDialog() {
        if (Thread.currentThread() != Looper.getMainLooper().thread) {
            mMainThread.postDelayed(object : Runnable {
                override fun run() {
                    if (mState == State.HIDE_LOADING_DIALOG) {
                        if (isLoadingDialogVisible()) {
                            Log.d(TAG, "dismiss dialog from class " + this.javaClass.simpleName)
                            mLoadingDialog.dismissAllowingStateLoss()
                        } else {
                            Log.d(TAG, "dismiss dialog failed cause not visible from class " + this.javaClass.simpleName)
                        }
                    }
                }
            }, 150)
        } else {
            if (isLoadingDialogVisible()) {
                Log.d(TAG, "dismiss dialog from class " + this.javaClass.simpleName)
                mLoadingDialog.dismissAllowingStateLoss()
            } else {
                Log.d(TAG, "dismiss dialog failed cause not visible from class " + this.javaClass.simpleName)
            }
        }
    }

    fun isLoadingDialogVisible(): Boolean {
        return mLoadingDialog.isAdded
    }
}

class ConfirmDialog : BaseDialog(), View.OnClickListener {
    companion object {
        val TAG: String = "ConfirmDialog"

        @JvmStatic
        fun newInstance(title: String, content: String, hideCancel: Boolean): ConfirmDialog {
            val confirmDialog = ConfirmDialog()
            val bundle = Bundle()
            bundle.putString(ConfirmDialogArgumentKey.TITLE_KEY.name, title)
            bundle.putString(ConfirmDialogArgumentKey.CONTENT_KEY.name, content)
            bundle.putString(ConfirmDialogArgumentKey.TYPE_KEY.name, if (hideCancel) Type.ONLY_POSITIVE_BUTTON.name else Type.BOTH.name)
            confirmDialog.arguments = bundle
            return confirmDialog
        }

        @JvmStatic
        fun newInstance(title: String, content: String): ConfirmDialog {
            return newInstance(title, content, false)
        }

    }

    enum class Type {
        ONLY_POSITIVE_BUTTON, ONLY_NEGATIVE_BUTTON, BOTH

    }

    interface ActionListener {
        fun onOkButtonClicked()
        fun onCancelButtonClicked()
    }

    private lateinit var mActionListener: ActionListener

    private lateinit var mCancelView: TextView
    private lateinit var mOkView: TextView
    private lateinit var mContentView: TextView
    private lateinit var mTitleView: TextView
    private lateinit var mHeaderView: LinearLayout

    private var mTitleTxt: String = ""
    private var mContentTxt: String = ""
    private var mPosBtnTxt: String = ""
    private var mNegBtnTxt: String = ""
    private var mNoTitle: Boolean = false
    private var mType: String = Type.BOTH.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTitle)
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
            bundle.putString(ConfirmDialogArgumentKey.POSITIVE_BUTTON_TEXT_KEY.name, mPositiveButtonText)
            bundle.putString(ConfirmDialogArgumentKey.NEGATIVE_BUTTON_TEXT_KEY.name, mNegativeButtonText)

            confirmDialog.arguments = bundle
            return confirmDialog
        }
    }

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        getData()
        mCancelView = view.findViewById(R.id.confirm_dialog_cancel_btn)
        mOkView = view.findViewById(R.id.confirm_dialog_ok_btn)
        mTitleView = view.findViewById(R.id.confirm_dialog_title_txt)
        mContentView = view.findViewById(R.id.confirm_dialog_content_txt)
        mHeaderView = view.findViewById(R.id.confirm_dialog_header_view)

        mCancelView.visibility = if (mType.equals(Type.BOTH.name) || mType.equals(Type.ONLY_NEGATIVE_BUTTON.name)) View.VISIBLE else View.GONE

        mHeaderView.visibility = if (!mNoTitle) let {
            mTitleView.text = mTitleTxt
            View.VISIBLE
        } else View.INVISIBLE

        mOkView.text = if (mPosBtnTxt.isEmpty()) getString(R.string.common_txt_ok_upcase) else mPosBtnTxt

        mCancelView.text = if (mNegBtnTxt.isEmpty()) getString(R.string.common_txt_cancel_upcase) else mPosBtnTxt

        mContentView.text = mContentTxt
    }

    override fun getLayoutResId(): Int {
        return R.layout.common_dialog_confirm
    }

    override fun bindEvents() {
        super.bindEvents()
        mOkView.setOnClickListener(this)
        mCancelView.setOnClickListener(this)
    }

    fun setActionListener(actionListener: ActionListener) {
        mActionListener = actionListener
    }

    fun getData() {
        mTitleTxt = arguments?.getString(ConfirmDialogArgumentKey.TITLE_KEY.name) ?: ""
        mContentTxt = arguments?.getString(ConfirmDialogArgumentKey.CONTENT_KEY.name) ?: ""
        mPosBtnTxt = arguments?.getString(ConfirmDialogArgumentKey.POSITIVE_BUTTON_TEXT_KEY.name)
            ?: ""
        mNegBtnTxt = arguments?.getString(ConfirmDialogArgumentKey.NEGATIVE_BUTTON_TEXT_KEY.name)
            ?: ""
        mNoTitle = arguments?.getBoolean(ConfirmDialogArgumentKey.NO_TITLE_KEY.name) ?: false
        mType = arguments?.getString(ConfirmDialogArgumentKey.TYPE_KEY.name) ?: Type.BOTH.name
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.confirm_dialog_ok_btn -> mActionListener.onOkButtonClicked()
            R.id.confirm_dialog_cancel_btn -> mActionListener.onCancelButtonClicked()
        }
        dismiss()
    }
}

class LoadingDialog : BaseDialog() {

    companion object {
        val TAG: String = "LoadingDialog"
        val CONTENT_KEY = "CONTENT_KEY"

        @JvmStatic
        fun newInstance(content: String?): LoadingDialog {
            val loadingDialog = LoadingDialog()
            val bundle = Bundle()
            bundle.putString(CONTENT_KEY, content)
            loadingDialog.arguments = bundle
            return loadingDialog
        }
    }

    private lateinit var mContentView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTitle)
    }

    override fun getLayoutResId(): Int {
        return R.layout.common_dialog_loading
    }

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        mContentView = view.findViewById(R.id.loading_dialog_content)
        mContentView.text = arguments?.getString(CONTENT_KEY) ?: ""
    }
}