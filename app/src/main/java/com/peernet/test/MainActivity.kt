package com.peernet.test

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import mobilecore.Mobilecore
import androidx.core.app.ActivityCompat.requestPermissions

import android.os.Build

import androidx.annotation.RequiresApi




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView




        if (Build.VERSION.SDK_INT >= 23) {
            askForPermissions(
                arrayOf<String>(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ),//for both GPS and Network Provider
                REQUEST_PERMISSIONS_CODE
            );
        }

        connectionLabel.text = Mobilecore.mobileCoreStart().toString()


    }

    val REQUEST_PERMISSIONS_CODE = 1982

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun askForPermissions(permissions: Array<String>, requestCode: Int) {
        val permissionsToRequest: MutableList<String> = ArrayList()
        for (permission in permissions) {
            if (checkSelfPermission(permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                //permision which have not been granted need to be request again
                permissionsToRequest.add(permission)
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            //nếu gửi 2 lệnh request liên tiếp thì, request đầu tiên sẽ đc xử lý, request sau sẽ bị loại bỏ
            // vì thế chỉ gửi từng request 1 thôi
            // nếu có nhiều permision thì phải đưa chúng vào Array cho 1 request thôi (dã test)
            requestPermissions(permissionsToRequest.toTypedArray(), requestCode)
        }
    }

    /*
     * Permission on for UI (Activity, Fragment). Service can not receive the response from the OS
     * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE) { //dùng chung permission code hoặc riêng thì tùy
            //check xem permission nào đc granted/deny
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //
                       // Log.d(TAG, "WRITE_EXTERNAL_STORAGE ok")
                    } else {
                        //
                    }
                }
            }
        }
    }


}