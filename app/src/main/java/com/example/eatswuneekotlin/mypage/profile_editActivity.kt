package com.example.eatswuneekotlin.mypage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class profile_editActivity : AppCompatActivity() {
    var logout: Button? = null
    var withdrawal: Button? = null
    var camera_btn: ImageButton? = null
    var user_id: TextView? = null
    var nickname: EditText? = null
    var profile: ImageView? = null
    private var retrofitClient: RetrofitClient? = null
    private var serviceApi: ServiceApi? = null
    var mCurrentPhotoPath: String? = null
    var imageURI: Uri? = null
    var photoURI: Uri? = null
    var albumURI: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        val toolbar = findViewById<View>(R.id.profile_edit_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
        init()
        logout = findViewById(R.id.logout_btn)
        withdrawal = findViewById(R.id.withdrawal_btn)
        camera_btn = findViewById(R.id.camera_btn)
        camera_btn.setOnClickListener(CameraOnClickListener())
        user_id = findViewById(R.id.edit_id)
        nickname = findViewById(R.id.edit_nickname)
        profile = findViewById(R.id.edit_profile)

        // 로그아웃, 회원탈퇴 버튼 밑줄 표현
        logout.setPaintFlags(logout.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        withdrawal.setPaintFlags(withdrawal.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_img_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        retrofitClient = RetrofitClient.instance
        serviceApi = RetrofitClient.serviceApi
        serviceApi.profile.enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data
                Log.d("retrofit", "Data fetch success")
                nickname!!.setText(data.user_name)
                user_id!!.text = data.loginId
                DownloadFilesTask().execute(data.user_profile_url)
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    internal inner class DownloadFilesTask : AsyncTask<String?, Void?, Bitmap?>() {
        protected override fun doInBackground(vararg strings: String): Bitmap? {
            var bmp: Bitmap? = null
            try {
                val img_url = strings[0] //url of the image
                val url = URL(img_url)
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bmp
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Bitmap?) {
            profile!!.setImageBitmap(result)
        }
    }

    private inner class CameraOnClickListener : View.OnClickListener {
        @SuppressLint("UseCheckPermission")
        override fun onClick(view: View) {
            val pop = PopupMenu(applicationContext, view)
            menuInflater.inflate(R.menu.profile_img_menu, pop.menu)
            pop.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.two ->                             // 갤러리에 관한 권한을 받아오는 코드
                            album
                        R.id.three -> {}
                    }
                    return true
                }
            })
            pop.show()
        }
    }

    // popup_menu에서 '갤러리'를 클릭하면 getAlbum 함수 호출
    private val album: Unit
        private get() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_TAKE_ALBUM)
        }

    // 사진을 crop할 수 있도록 하는 함수
    fun cropImage() {
        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        cropIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        cropIntent.setDataAndType(photoURI, "image/*")
        cropIntent.putExtra("aspectX", 1)
        cropIntent.putExtra("aspectY", 1)
        cropIntent.putExtra("scale", true)
        cropIntent.putExtra("output", albumURI)
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val selectedImageUri: Uri?
        val option = RequestOptions().circleCrop()
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            when (requestCode) {
                REQUEST_TAKE_ALBUM -> {
                    selectedImageUri = data.data
                    Glide.with(applicationContext).load(selectedImageUri).apply(option)
                        .into(profile!!)
                }
            }
        }
    }

    companion object {
        private const val MY_PERMISSION_CAMERA = 1111
        private const val REQUEST_TAKE_ALBUM = 3333
        private const val REQUEST_IMAGE_CROP = 4444
        const val REQUESTCODE = 101
    }
}