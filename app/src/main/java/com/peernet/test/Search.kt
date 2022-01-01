package com.peernet.test

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*


class Search : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Go to the home page
        val Back = findViewById<Button>(R.id.Back)
        // Explore / search result varaibles
        var ResponseObjectSearch: JSONObject
        ResponseObjectSearch = JSONObject("{\"keyword\": null}");

        // redirect back to the home page
        Back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val queue = Volley.newRequestQueue(this)


        // Explore from golang
        // Mobile.searchResults()

        // Default display explore
        val url = "http://127.0.0.1:5125/explore"



        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                // Display the first 500 characters of the response string.
                // textView.text = "Response is: ${response.substring(0, 500)}"
                //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
              //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
               // connectionLabel1.text = response.toString()
                 Log.d("myTag1", "Response is: ${response}");

                ResponseObjectSearch = JSONTokener(response.toString()).nextValue() as JSONObject

                val searchResult = findViewById<ListView>(R.id.SearchResult)

                val listItems = arrayOfNulls<String>(ResponseObjectSearch.getJSONArray("files").length())

                for (i in 0 until ResponseObjectSearch.getJSONArray("files").length()) {
                    val searchResultElement = ResponseObjectSearch.getJSONArray("files")[i]

                    val ResponseObjectInLoop = JSONTokener(ResponseObjectSearch.getJSONArray("files")[i].toString()).nextValue() as JSONObject

                    listItems[i] = ResponseObjectInLoop.get("name").toString()
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

                searchResult.adapter = adapter



            },
            Response.ErrorListener { errorresponse ->
              //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
              //  connectionLabel1.text = errorresponse.toString()
                Log.d("myTag",  errorresponse.toString());
            })

        queue.add(stringRequest)

        // Search Button
        val SearchButton = findViewById<Button>(R.id.Search)
        val SearchField = findViewById<EditText>(R.id.SearchField)

        // Search onclick action
        SearchButton.setOnClickListener {
            // get text from search field
            val search = SearchField.text.toString()


            // Get search job UUID
            val url = "http://127.0.0.1:5125/search"

//            val params = HashMap<String,String>()
//            params["term"] = search
//            val jsonObject = JSONObject(params)

            // "timeout": 10,
            //    "maxresults": 1000,
            //    "sort": 0,
            //    "filetype": -1,
            //    "fileformat": -1,
            //    "sizemin": -1,
            //    "sizemax": -1

            val jsonobj = JSONObject()
            jsonobj.put("term", search)
            jsonobj.put("timeout", 10)
            jsonobj.put("maxresults", 1000)
            jsonobj.put("filetype", -1)
            jsonobj.put("fileformat", -1)
            jsonobj.put("sizemin", -1)
            jsonobj.put("sizemax", -1)


            // Request a string response from the provided URL.

            val stringRequest = JsonObjectRequest(
                Request.Method.POST,url,jsonobj,
                Response.Listener { response ->
                    //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                    // Display the first 500 characters of the response string.
                    // textView.text = "Response is: ${response.substring(0, 500)}"
                    //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
                    //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                    // connectionLabel1.text = response.toString()
                    Log.d("myTag1", "Response is: ${response}");

                    val ResponseObject = JSONTokener(response.toString()).nextValue() as JSONObject

                    // request to get Job response
                    val url1 = "http://127.0.0.1:5125/search/result?id=" + ResponseObject.get("id")

                    Log.d("myTag",  url1);

                    val stringRequest1 = StringRequest(
                        Request.Method.GET,url1,
                        Response.Listener { response ->
                            //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                            // Display the first 500 characters of the response string.
                            // textView.text = "Response is: ${response.substring(0, 500)}"
                            //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
                            //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                            // connectionLabel1.text = response.toString()
                            Log.d("myTag1", "Response is: ${response}");

                            ResponseObjectSearch = JSONTokener(response.toString()).nextValue() as JSONObject

                            val searchResult = findViewById<ListView>(R.id.SearchResult)

                            val listItems = arrayOfNulls<String>(ResponseObjectSearch.getJSONArray("files").length())

                            for (i in 0 until ResponseObjectSearch.getJSONArray("files").length()) {
                                val searchResultElement = ResponseObjectSearch.getJSONArray("files")[i]

                                val ResponseObjectInLoop = JSONTokener(ResponseObjectSearch.getJSONArray("files")[i].toString()).nextValue() as JSONObject

                                listItems[i] = ResponseObjectInLoop.get("name").toString()
                            }

                            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

                            searchResult.adapter = adapter

                        },
                        Response.ErrorListener { errorresponse ->
                            //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                            //  connectionLabel1.text = errorresponse.toString()
                            Log.d("myTag",  errorresponse.toString());
                        })

                    queue.add(stringRequest1)

                },
                Response.ErrorListener { errorresponse ->
                    //  val  connectionLabel1 = findViewById<View>(R.id.PeernetInfo1) as TextView
                    //  connectionLabel1.text = errorresponse.toString()
                    Log.d("myTag",  errorresponse.toString());
                })

            queue.add(stringRequest)

            val searchResult = findViewById<ListView>(R.id.SearchResult)

            // when a file is selected, Then start the download
            searchResult.onItemClickListener = AdapterView.OnItemClickListener {parent,view, position, id ->
                // Get the selected item text from ListView
                val selectedItem = parent.getItemAtPosition(position) as String
                Log.d("myTag",  selectedItem);

                // appropriate information to do a download of the file
                for (i in 0 until ResponseObjectSearch.getJSONArray("files").length()) {
                    val searchResultElement = ResponseObjectSearch.getJSONArray("files")[i]

                    val FileName = JSONTokener(ResponseObjectSearch.getJSONArray("files")[i].toString()).nextValue() as JSONObject
                    // If there is match. Get the appropriate information such as Node ID.
                    if (FileName.get("name").toString() == selectedItem) {
                        // File hash
                        val FileHash = FileName.get("hash").toString()
                        Log.d("myTag", FileHash)
                        val NodeID = FileName.get("nodeid").toString()
                        Log.d("myTag", NodeID)

                        // decode base64 file hash
                        val FileHashByte: ByteArray = Base64.getDecoder().decode(FileHash)




                        val DownloadPath = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

                        Log.d("download",  DownloadPath.toString());

                        // Get request URL to download the file
                        val DownloadURL = "http://127.0.0.1:5125/download/start?path=" + java.net.URLEncoder.encode(DownloadPath.toString() + "/" + FileName.get("name"), "utf-8") + "&hash=" + java.net.URLEncoder.encode(FileHash, "utf-8") + "&node=" + java.net.URLEncoder.encode(NodeID, "utf-8")

                        val DownloadRequest = StringRequest(
                            Request.Method.GET, DownloadURL,
                            Response.Listener<String> { response ->
                                //val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
                                // Display the first 500 characters of the response string.
                                // textView.text = "Response is: ${response.substring(0, 500)}"
                                //connectionLabel.text= "Response is: ${response.substring(0, 500)}"
//                                val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
//                                connectionLabel.text = response.toString()
                                 Log.d("myTag", "Response is: ${response}");
                            },
                            Response.ErrorListener { errorresponse ->
//                                val  connectionLabel = findViewById<View>(R.id.PeernetInfo) as TextView
//                                connectionLabel.text = errorresponse.toString()
                                Log.d("myTag",  errorresponse.toString());
                            })

                        queue.add(DownloadRequest)


                    }


                }

            }
        }


    }





}