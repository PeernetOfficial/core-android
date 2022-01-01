package com.peernet.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

class Blockchain_info : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blockchain_info)


        val queue = Volley.newRequestQueue(this)

        val url = "http://127.0.0.1:5125/blockchain/file/list"

        var ResponseObjectSearch: JSONObject
        ResponseObjectSearch = JSONObject("{\"keyword\": null}");


// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                ResponseObjectSearch = JSONTokener(response.toString()).nextValue() as JSONObject

                val blockchainResult = findViewById<ListView>(R.id.SearchResult)

                val listItems = arrayOfNulls<String>(ResponseObjectSearch.getJSONArray("files").length())

                for (i in 0 until ResponseObjectSearch.getJSONArray("files").length()) {
                    val searchResultElement = ResponseObjectSearch.getJSONArray("files")[i]

                    val ResponseObjectInLoop = JSONTokener(ResponseObjectSearch.getJSONArray("files")[i].toString()).nextValue() as JSONObject

                    listItems[i] = ResponseObjectInLoop.get("name").toString()
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

                blockchainResult.adapter = adapter
            },
            Response.ErrorListener { errorresponse ->
//                        val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
//                        connectionLabel1.text = errorresponse.toString()
                Log.d("myTag",  errorresponse.toString());
            })

        queue.add(stringRequest)

        val blockchainResult = findViewById<ListView>(R.id.SearchResult)

        blockchainResult.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val searchResultElement = ResponseObjectSearch.getJSONArray("files")[position]

            val FileName = JSONTokener(ResponseObjectSearch.getJSONArray("files")[position].toString()).nextValue() as JSONObject


//            // Remove file warehouse
//            val urlRemoveFileWarehouse = "http://127.0.0.1:5125/warehouse/delete?hash=" + FileName.get("hash")
//
//            // Request a string response from the provided URL.
//            val stringRequest1 = StringRequest(
//                Request.Method.GET, urlRemoveFileWarehouse,
//                Response.Listener<String> { response ->
//                    //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
//                    // Display the first 500 characters of the response string.
//                    // textView.text = "Response is: ${response.substring(0, 500)}"
//                    //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
//                     Log.d("myTag", "Response is: ${response}");
//                },
//                Response.ErrorListener { errorresponse ->
////                        val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
////                        connectionLabel1.text = errorresponse.toString()
//                    Log.d("myTag",  errorresponse.toString());
//                })
//
//// Add the request to the RequestQueue.
//            queue.add(stringRequest1)

            // Remove file from blockchain
            val urlRemoveBlockChain = "http://127.0.0.1:5125/blockchain/file/delete"

            val jsonobj = JSONObject()
            jsonobj.put("id", FileName.get("id"))

            val jsonArrayObj = JSONArray()

            val JSONPostRequest = JSONObject()

            jsonArrayObj.put(0,jsonobj)

            JSONPostRequest.put("files",jsonArrayObj)



            Log.d("myTag",  JSONPostRequest.toString())


            // remove file meta-data / information from the blockchain

            val stringRequest = JsonObjectRequest(
                Request.Method.POST,urlRemoveBlockChain,JSONPostRequest,
                Response.Listener { response ->
                    //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                    // Display the first 500 characters of the response string.
                    // textView.text = "Response is: ${response.substring(0, 500)}"
                    //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
                    //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                    // connectionLabel1.text = response.toString()
                    Log.d("RemoveBlockchain", "Response is: ${response}");

                    // val ResponseObject = JSONTokener(response.toString()).nextValue() as JSONObject
                },
                Response.ErrorListener { errorresponse ->
                    //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                    //  connectionLabel1.text = errorresponse.toString()
                    Log.d("myTag",  errorresponse.toString());
                })

            queue.add(stringRequest)

            finish();
            startActivity(getIntent());
        }

    }


}