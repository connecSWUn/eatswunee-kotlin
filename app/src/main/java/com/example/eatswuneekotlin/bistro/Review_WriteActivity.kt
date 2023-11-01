package com.example.eatswuneekotlin.bistro

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.mypage.review_content
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Review_WriteActivity : AppCompatActivity() {

    private lateinit var cancel: Button
    private lateinit var done: Button

    private lateinit var res: TextView
    private lateinit var menu: TextView
    private lateinit var review_text: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var review_image_btn: ImageView

    private lateinit var review_image_layout: LinearLayout
    private lateinit var review_image: ImageView
    private lateinit var tempFile: File

    lateinit var mCurrentPhotoPath: String
    lateinit var imageURI: Uri
    lateinit var photoURI: Uri
    lateinit var albumURI: Uri

    var menu_review_rating: Double = 0.0
    var menu_id: Long = 0

    val masterApp = MasterApplication()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)

        cancel = findViewById(R.id.review_cancel_btn)
        done = findViewById(R.id.review_done_btn)
        review_image_btn = findViewById(R.id.review_img_btn)
        review_image = findViewById(R.id.review_image)
        review_image_layout = findViewById(R.id.review_image_layout)

        res = findViewById(R.id.review_write_res)
        menu = findViewById(R.id.review_write_menu)
        review_text = findViewById(R.id.review_write_content)
        ratingBar = findViewById(R.id.ratingBar)

        val intent = intent
        val restaurant_name = intent.getStringExtra("restaurant_name")
        val menu_name = intent.getStringExtra("menu_name")

        menu_id = intent.getLongExtra("menu_id", 0)
        res.text = "[$restaurant_name]"
        menu.text = menu_name
        ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                menu_review_rating = java.lang.Double.valueOf(rating.toDouble())
            }

        review_image_btn.setOnClickListener(imageOnClickListener())
        done.setOnClickListener(doneBtnOnClickListener())
        cancel.setOnClickListener { finish() }
    }

    private inner class doneBtnOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val image_url = tempFile.toString()
            val content = review_text!!.text.toString()
            val review_content = review_content(menu_id, menu_review_rating!!, image_url, content)

            masterApp.createRetrofit(this@Review_WriteActivity)
            val service = masterApp.serviceApi

            service.postReview(review_content)?.enqueue(object : Callback<Result?> {
                override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                    if (response.isSuccessful) {
                        Log.d("review", "POST Success")

                        Toast.makeText(
                            this@Review_WriteActivity,
                            "리뷰가 등록되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    }
                }

                override fun onFailure(call: Call<Result?>, t: Throwable) {}
            })
        }
    }

    private inner class imageOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            verifyStoragePermissions(this@Review_WriteActivity)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_TAKE_ALBUM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_ALBUM) {
            photoURI = data!!.data!!
            var cursor: Cursor? = null
            try {

                /*
                 * Uri Schema를
                 * content:/// 에서 file:/// 로 변경한다.
                 */
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                assert(photoURI != null)
                cursor = contentResolver.query(photoURI!!, proj, null, null, null)
                assert(cursor != null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                tempFile = File(cursor.getString(column_index))

            } finally {
                cursor?.close()
            }
            setImage()
        }
    }

    private fun setImage() {
        val options = BitmapFactory.Options()
        val originalBm = BitmapFactory.decodeFile(tempFile!!.absolutePath, options)
        review_image_layout!!.visibility = View.GONE
        review_image!!.visibility = View.VISIBLE
        review_image!!.setImageBitmap(originalBm)
    }

    companion object {
        private const val REQUEST_TAKE_ALBUM = 3333
        private const val REQUESTCODE = 101

        /* 앱 시작 시 퍼미션 확인 */
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        fun verifyStoragePermissions(activity: Activity?) {
            val permission = ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }
}