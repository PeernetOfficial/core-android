package com.peernet.test

import android.Manifest
import android.content.ContentValues.TAG
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
import mobile.Mobile
import androidx.core.app.ActivityCompat.requestPermissions

import android.os.Build
import android.provider.Settings
import android.util.Log
import com.android.volley.Request

import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.IOException


class MainActivity : AppCompatActivity() {

    //private val client = OkHttpClient()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Mobile.mobileMain(this.filesDir.absolutePath)

        val queue = Volley.newRequestQueue(this)
        val url = "http://127.0.0.1:5125/account/info"



// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                // Display the first 500 characters of the response string.
               // textView.text = "Response is: ${response.substring(0, 500)}"
                //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
                val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                connectionLabel1.text = response.toString()
               // Log.d("myTag", "Response is: ${response.substring(0, 500)}");
            },
            Response.ErrorListener { errorresponse ->
                val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                connectionLabel1.text = errorresponse.toString()
                Log.d("myTag",  errorresponse.toString());
            })

// Add the request to the RequestQueue.
       queue.add(stringRequest)

        val url1 = "http://127.0.0.1:5125/status"

        val stringRequest1 = StringRequest(
            Request.Method.GET, url1,
            Response.Listener<String> { response ->
                //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                // Display the first 500 characters of the response string.
                // textView.text = "Response is: ${response.substring(0, 500)}"
                //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
                val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                connectionLabel.text = response.toString()
                // Log.d("myTag", "Response is: ${response.substring(0, 500)}");
            },
            Response.ErrorListener { errorresponse ->
                val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                connectionLabel.text = errorresponse.toString()
                Log.d("myTag",  errorresponse.toString());
            })

        queue.add(stringRequest1)



     //   run("http://127.0.0.1:5125/status")

       // requestAppPermissions();
        //Mobilecore.mobileCoreStart()

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.System.canWrite(this)) {
//                requestPermissions(
//                    arrayOf(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ), 2909
//                )
//            } else {
//                connectionLabel.text = Mobilecore.mobileCoreStart().toString()
//            }
//        } else {
//            connectionLabel.text = Mobilecore.mobileCoreStart().toString()
//        }

    }

//    fun run(url: String) {
//        val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {}
//           // override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
//            override fun onResponse(call: Call, response: Response) {
//                /* This will print the response of the network call to the Logcat */
//               connectionLabel.text = response.body()?.string()
//                //Log.d("TAG_", response.body())
//            }
//        })
//    }


    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        if (hasReadPermissions() && hasWritePermissions()) {

         //  connectionLabel.text = "lol123"

            return
        }
        requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1
        ) // your request code
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWritePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }







}