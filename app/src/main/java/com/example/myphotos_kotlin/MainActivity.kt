package com.example.myphotos_kotlin

import android.content.Intent
import android.net.DnsResolver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myphotos_kotlin.View.PhotoAdapter
import com.example.myphotos_kotlin.View.SinglePhotoActivity
import com.example.myphotos_kotlin.model.Photo

import com.example.myphotos_kotlin.network.PhotoService
import com.example.myphotos_kotlin.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var  recyclerView:RecyclerView
    companion object{
        val KEY_PHOTO_BUNDLE = "key_position"
        val LIST_PHOTO_REQUEST_CODE = "pass_animal_request_code"

    }
lateinit var  list:List<Photo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //RetrofitClient.getInstanceOfRetrofit()?.create(PhotoService::class.java)
        recyclerView=findViewById(R.id.recyclerview)

        val getPhotoService = RetrofitClient.instance?.create(PhotoService::class.java)

        getPhotoService?.getPhotos()?.enqueue(object :Callback<List<Photo>>{
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
             val photoListFromResponse =response.body()
                list= response.body()!!
                CreateRecycler()
                photoListFromResponse?.forEach{
                    Log.d("MyApp","${it.albumId}&&${it.thumbnailUrl}")
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
             Toast.makeText(this@MainActivity,"HOUSTON WE HAVE A PROBLEM",Toast.LENGTH_SHORT).show()

            }

        })



    }
    fun CreateRecycler(){
        val adapter = PhotoAdapter(list) {

            val result = Bundle()
            result.putParcelable(KEY_PHOTO_BUNDLE, list[it])
            val intent = Intent(this, SinglePhotoActivity::class.java)

            intent.putExtras(result)
            startActivity(intent)


        }
        val layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }
}