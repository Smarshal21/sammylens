package com.smartherd.googlelens

import android.annotation.SuppressLint

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
//import com.google.firebase.ml.vision.FirebaseVision
//import com.google.firebase.ml.vision.common.FirebaseVisionImage
//import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity() : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var snapbutton:Button
    private lateinit var resultsbutton:Button
    private lateinit var recyclerView: RecyclerView
    lateinit var searchRvAdapter: SearchRvAdapter
    lateinit var searchRvModelArrayList: ArrayList<SearchRvModel>
    var REQUEST_CODE:Int = 1
    lateinit var loadingPB:ProgressBar
    lateinit var imagebitmap: Bitmap
    lateinit var title: String
    lateinit var link:String
    lateinit var displayed_link:String
    lateinit var snippet:String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        imageView = findViewById(R.id.image)
        snapbutton = findViewById(R.id.idnSnapbutton)
       resultsbutton = findViewById(R.id.idnresultsbutton)
       recyclerView = findViewById(R.id.idsearchresults)
       loadingPB = findViewById(R.id.idpbloading)
        recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
        searchRvModelArrayList =  ArrayList()

       searchRvAdapter = SearchRvAdapter(this@MainActivity,searchRvModelArrayList)
        recyclerView.adapter = searchRvAdapter

        resultsbutton.setOnClickListener (object:View.OnClickListener{
            override fun onClick(p0: View?) {
                searchRvModelArrayList.clear()
                searchRvAdapter.notifyDataSetChanged()
                getResults()
                loadingPB.visibility = (View.VISIBLE)
            }
        })
        snapbutton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                searchRvModelArrayList.clear()
                searchRvAdapter.notifyDataSetChanged()
                takePictureIntent()
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE&&resultCode == RESULT_OK){
            val extras: Bundle? = data?.extras
            if (extras != null) {
                imagebitmap = extras.get("data") as Bitmap
            }
            imageView.setImageBitmap(imagebitmap)
        }
    }
    fun getResults() {
        val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(imagebitmap)
        val labeler: FirebaseVisionImageLabeler = FirebaseVision.getInstance().onDeviceImageLabeler
        labeler.processImage(image).addOnSuccessListener(object :OnSuccessListener<List<FirebaseVisionImageLabel>>{
            override fun onSuccess(p0: List<FirebaseVisionImageLabel>) {
                val searchquery = p0.get(0).text
                getSearchResults(searchquery)
            }

        }).addOnFailureListener {
            Toast.makeText(this@MainActivity,"Fail to detect image",Toast.LENGTH_SHORT).show()

        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getSearchResults(searchquery: String) {
        val url= "https://serpapi.com/search.json?q=mobile&location=Delhi,India&hl=en&gl=us&google_domain=google.com&api_key=c6d751f2be03b91fbcbce513bc70b659c40b875b9840da8d43b2dbda0f01c855"
        val queue:RequestQueue = Volley.newRequestQueue(this@MainActivity)
        val jsonObjectRequest = JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
            { response ->
                loadingPB.visibility = View.GONE
                try {
                    val organicArray:JSONArray = response.getJSONArray("organic_results")
                    for (i in 0..organicArray.length()){
                        val organicobj: JSONObject = organicArray.getJSONObject(i)
                        if (organicobj.has("title")){
                            title= organicobj.getString("title")
                        }
                        if (organicobj.has("link")){
                            link = organicobj.getString("link")
                        }
                        if (organicobj.has("displayed_link")){
                            displayed_link = organicobj.getString("displayed_link")
                        }
                        if (organicobj.has("snippet")){
                            snippet  = organicobj.getString("snippet")
                        }
                        searchRvModelArrayList.add(SearchRvModel(title, link, displayed_link, snippet))
                    }
                    searchRvAdapter.notifyDataSetChanged()

                }catch (e:JSONException){
                    e.printStackTrace()

                }



            },
            { error ->
                Toast.makeText(this@MainActivity,"Failed to display search results",Toast.LENGTH_SHORT).show()
            })
        queue.add(jsonObjectRequest)

    }
    private fun takePictureIntent() {
        val i :Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (i.resolveActivity(packageManager)!=null){
            startActivityForResult(i,REQUEST_CODE)
        }


    }

}