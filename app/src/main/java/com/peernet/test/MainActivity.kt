package com.peernet.test

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
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
import android.widget.Button
import com.android.volley.Request

import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule
import android.provider.MediaStore
import java.io.File
import android.os.Environment
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.nio.file.Files
import java.nio.file.Paths


class MainActivity : AppCompatActivity() {

    //private val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Mobile.mobileMain(this.filesDir.absolutePath)

        if (isNetwork(this)){

            Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(this, "Internet Is Not Connected", Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this)

        Timer().schedule(500){
            // do something after 1 second
            GlobalScope.launch {
                val url = "http://127.0.0.1:5125/account/info"



// Request a string response from the provided URL.
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    Response.Listener<String> { response ->
                        //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                        // Display the first 500 characters of the response string.
                        // textView.text = "Response is: ${response.substring(0, 500)}"
                        //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
                        val ResponseObject = JSONTokener(response.toString()).nextValue() as JSONObject
                        val  peerID = findViewById<View>(R.id.PeerIDFIeld) as TextView
                        val  nodeID = findViewById<View>(R.id.NodeIDFIeld) as TextView
                        peerID.text = ResponseObject.get("peerid").toString()
                        nodeID.text = ResponseObject.get("nodeid").toString()
                        // Log.d("myTag", "Response is: ${response.substring(0, 500)}");
                    },
                    Response.ErrorListener { errorresponse ->
//                        val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
//                        connectionLabel1.text = errorresponse.toString()
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
                        // Parse response as JSON
                        val ResponseObject = JSONTokener(response.toString()).nextValue() as JSONObject

                        val  isConnected  = findViewById<View>(R.id.isConnectedField) as TextView
                        val  numPeers  = findViewById<View>(R.id.PeerListField) as TextView

                        isConnected.text = ResponseObject.get("isconnected").toString()
                        numPeers.text = ResponseObject.get("countpeerlist").toString()

                       // connectionLabel.text = response.toString()
                        // Log.d("myTag", "Response is: ${response.substring(0, 500)}");
                    },
                    Response.ErrorListener { errorresponse ->
                        //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                       // connectionLabel.text = errorresponse.toString()
                        Log.d("myTag",  errorresponse.toString());
                    })

                queue.add(stringRequest1)

                val url2 = "http://127.0.0.1:5125/blockchain/file/list"

                val stringRequest2 = StringRequest(
                    Request.Method.GET, url2,
                    Response.Listener<String> { response ->
                        //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                        // Display the first 500 characters of the response string.
                        // textView.text = "Response is: ${response.substring(0, 500)}"
                        //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
//                        val  connectionLabel = findViewById<View>(R.id.PeernetInfo2) as TextView
//                        connectionLabel.text = response.toString()
                   //     Log.d("myTag", "Response is: ${response.substring(0, 500)}");
                    },
                    Response.ErrorListener { errorresponse ->
//                        val  connectionLabel = findViewById<View>(R.id.PeernetInfo2) as TextView
//                        connectionLabel.text = errorresponse.toString()
                        Log.d("myTag",  errorresponse.toString());
                    })

                queue.add(stringRequest2)
            }
        }

        val button = findViewById<Button>(R.id.AddFIle)

        button.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 777)


        }

        // Go to the search page
        val SearchHomeButton = findViewById<Button>(R.id.SearchHomeButton)

        SearchHomeButton.setOnClickListener {
            val intent = Intent(this, Search::class.java)
            startActivity(intent)
        }










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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val queue = Volley.newRequestQueue(this)

        if (requestCode == 777) {
            var filePath = data?.data?.isAbsolute

            val path = FileUtils.getPath(this, data?.data)

            Log.d("path",path.toString())

            // convert Path string to URI
            val pathUri = path.toUri()

            // convert file to bytes

            var encoded: ByteArray
           // try {
                encoded = Files.readAllBytes(Paths.get(path))
                Log.d("bytes", encoded.toString())
//            } catch (e: IOException) {
//                Log.d("bytes", e.toString())
//            }

          //  data?.data

           // imageFile = new File(getRealPathFromURI(selectedImageURI));

        //    filePath = File((data?.data?.let { getRealPathFromURI(it) })).toString()

//            var file = File(
//                Environment.getExternalStorageDirectory().absolutePath,
//                filePath
//            )

//            file.path
//
//            Log.d("myTag",  file.absolutePath)
//
////            val ResponseObjectSearch = JSONObject("{\"keyword\":" + file.readBytes());
//
//            Log.d("myTag", java.net.URLEncoder.encode(filePath.toString(), "utf-8"));
//
//            val UploadFilePOST = "http://127.0.0.1:8881/warehouse/create"
//
//            val GetRequestWithFilePath = "http://127.0.0.1:8881/warehouse/create"

//            val stringRequest2 = JsonObjectRequest(
//                Request.Method.POST, UploadFile,ResponseObjectSearch,
//                Response.Listener<JSONObject> { response ->
//                    //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
//                    // Display the first 500 characters of the response string.
//                    // textView.text = "Response is: ${response.substring(0, 500)}"
//                    //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
//                    // val  connectionLabel = findViewById<View>(R.id.PeernetInfo2) as TextView
//                    //connectionLabel.text = response.toString()
//                    Log.d("myTag", "Response is: ${response.toString()}");
//                },
//                Response.ErrorListener { errorresponse ->
//                   // val  connectionLabel = findViewById<View>(R.id.PeernetInfo2) as TextView
//                   // connectionLabel.text = errorresponse.toString()
//                    Log.d("myTag",  errorresponse.toString());
//                })

            val url2 = "http://127.0.0.1:5125/warehouse/create/path?path=" + java.net.URLEncoder.encode(path, "utf-8")

            val stringRequest2 = StringRequest(
                Request.Method.GET, url2,
                Response.Listener<String> { response ->
                    //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                    // Display the first 500 characters of the response string.
                    // textView.text = "Response is: ${response.substring(0, 500)}"
                    //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
//                    val  connectionLabel = findViewById<View>(R.id.PeernetInfo2) as TextView
//                    connectionLabel.text = response.toString()
                      Log.d("myTag", "Response is: ${response.toString()}");

                    // read hash from the json object and add to the blockchain
                    val ResponseObject = JSONTokener(response.toString()).nextValue() as JSONObject

                    if (ResponseObject.get("status") == 0) {
                        val urlAddBlockChain = "http://127.0.0.1:5125/blockchain/file/add"

                        val jsonobj = JSONObject()
                        // Generate UUID
                        jsonobj.put("id", UUID.randomUUID().toString())
                        jsonobj.put("hash", ResponseObject.get("hash"))
                        jsonobj.put("type", 5)
                        jsonobj.put("format", 1)
                        jsonobj.put("size", encoded.size.toString())
                        jsonobj.put("name", path.substring(path.lastIndexOf("/")+1))
                        jsonobj.put("folder", path)
                        jsonobj.put("description", "")

                        val jsonArrayObj = JSONArray()

                        val JSONPostRequest = JSONObject()

                        jsonArrayObj.put(0,jsonobj)

                        JSONPostRequest.put("files",jsonArrayObj)



                        Log.d("myTag",  JSONPostRequest.toString())



                        val stringRequest = JsonObjectRequest(
                            Request.Method.POST,urlAddBlockChain,JSONPostRequest,
                            Response.Listener { response ->
                                //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                                // Display the first 500 characters of the response string.
                                // textView.text = "Response is: ${response.substring(0, 500)}"
                                //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
                                //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                                // connectionLabel1.text = response.toString()
                                Log.d("myTag1", "Response is: ${response}");

                               // val ResponseObject = JSONTokener(response.toString()).nextValue() as JSONObject



                            },
                            Response.ErrorListener { errorresponse ->
                                //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                                //  connectionLabel1.text = errorresponse.toString()
                                Log.d("myTag",  errorresponse.toString());
                            })

                        queue.add(stringRequest)


                    }






                },
                Response.ErrorListener { errorresponse ->
//                    val  connectionLabel = findViewById<View>(R.id.PeernetInfo2) as TextView
//                    connectionLabel.text = errorresponse.toString()
                    Log.d("myTag",  errorresponse.toString());
                })

            queue.add(stringRequest2)

        }
    }

    fun isNetwork(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnectedOrConnecting) {
            true
        } else false
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath().toString()
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    @JvmName("getRealPathFromURI1")
    fun getRealPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        if (null != cursor && cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
            cursor.close()
        }
        return res
    }

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