package com.example.androidkotlintemplate.Util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionUtil {

    companion object {
        val REQUEST_CAMERA_PERMISSION = 1
        val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 2
        val CLICK_ON_NEVER_ASK_AGAIN = "CLICK_ON_NEVER_ASK_AGAIN"
        @JvmStatic
        fun hasPermission(context: Context, permission: String): Boolean {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return true
        }

        @JvmStatic
        fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        @JvmStatic
        fun clickedOnNeverAskAgain(permissions: Array<String?>): Boolean {
            val sharedPreferences: PreferencesHelper = PreferencesHelper.get()
            var neverAskAgain = false
            for (permission in permissions) {
                val action: String = sharedPreferences.getString(permission, "")
                if (action == CLICK_ON_NEVER_ASK_AGAIN) {
                    neverAskAgain = true
                    break
                }
            }
            return neverAskAgain
        }

        @JvmStatic
        fun onRequestPermissionsResult(activity: Activity, permissions: Array<String>, grantResults: Array<Int>) {
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    //denied
                    Log.e("denied", permission)
                } else {
                    if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission)
                    } else {
                        val sharedPreferences: PreferencesHelper = PreferencesHelper.get()
                        sharedPreferences.putString(permission, CLICK_ON_NEVER_ASK_AGAIN)
                    }
                }
            }
        }

        @JvmStatic
        fun requestPermission(fragment: Fragment, permissions: Array<String>, REQUEST_PERMISSION: Int) {
            fragment.requestPermissions(permissions, REQUEST_PERMISSION)
        }

        @JvmStatic
        fun requestPermission(activity: AppCompatActivity, permissions: Array<String>, REQUEST_PERMISSION: Int) {
            if (!hasPermissions(activity, permissions)) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION)
            }
        }

    }
}