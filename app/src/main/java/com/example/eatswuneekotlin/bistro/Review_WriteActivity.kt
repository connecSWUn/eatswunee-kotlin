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
import android.view.Menu
import android.view.View
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.community.Friend_ViewActivity
import com.example.eatswuneekotlin.mypage.MyReviewActivity
import com.example.eatswuneekotlin.mypage.review_content
import com.example.eatswuneekotlin.server.Result
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

    lateinit var photoURI: Uri

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
        ratingBar.onRatingBarChangeListener = OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            menu_review_rating = rating.toDouble()
        }

        review_image_btn.setOnClickListener(imageOnClickListener())
        done.setOnClickListener(doneBtnOnClickListener())
        cancel.setOnClickListener { finish() }
    }

    private inner class doneBtnOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val id = menu_id.toString()
            val rating = menu_review_rating.toInt().toString()
            val content = review_text.text.toString()

            // Null 체크와 예외 처리
            if (::tempFile.isInitialized && ::photoURI.isInitialized) {
                val image_url = tempFile.toString()
                val review_content = review_content(id, rating, image_url, content)

                masterApp.createRetrofit(this@Review_WriteActivity)
                val service = masterApp.serviceApi

                service.postReview(review_content).enqueue(object : Callback<Result> {
                    override fun onResponse(call: Call<Result>, response: Response<Result>) {
                        if (response.isSuccessful) {
                            Log.d("review", "POST Success")

                            runOnUiThread {
                                Toast.makeText(
                                    this@Review_WriteActivity,
                                    "리뷰가 등록되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@Review_WriteActivity, MyReviewActivity::class.java)
                                startActivity(intent)

                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Result>, t: Throwable) {
                        // 실패 시 사용자에게 메시지 표시
                        runOnUiThread {
                            Toast.makeText(
                                this@Review_WriteActivity,
                                "리뷰 등록에 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            } else {
                val image_url = "review.jpg"
                val review_content = review_content(id, rating, image_url, content)

                masterApp.createRetrofit(this@Review_WriteActivity)
                val service = masterApp.serviceApi

                service.postReview(review_content).enqueue(object : Callback<Result> {
                    override fun onResponse(call: Call<Result>, response: Response<Result>) {
                        if (response.isSuccessful) {
                            Log.d("review", "POST Success")

                            runOnUiThread {
                                Toast.makeText(
                                    this@Review_WriteActivity,
                                    "리뷰가 등록되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@Review_WriteActivity, MyReviewActivity::class.java)
                                startActivity(intent)

                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Result>, t: Throwable) {
                        // 실패 시 사용자에게 메시지 표시
                        runOnUiThread {
                            Toast.makeText(
                                this@Review_WriteActivity,
                                "리뷰 등록에 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
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

        if (requestCode == REQUEST_TAKE_ALBUM && resultCode == Activity.RESULT_OK) {
            data?.data?.let { photoUri ->
                photoURI = photoUri
                // contentResolver를 사용하여 파일 처리 (Android 10 이상 버전 대응)
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                contentResolver.query(photoUri, projection, null, null, null)?.use { cursor ->
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    val filePath = cursor.getString(columnIndex)
                    tempFile = File(filePath)
                }
                setImage()
            } ?: run {
                // data가 null인 경우, 이미지를 선택하지 않은 것으로 처리
                Toast.makeText(
                    this@Review_WriteActivity,
                    "이미지를 선택해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // 선택된 이미지가 없는 경우에 대한 처리
            Toast.makeText(
                this@Review_WriteActivity,
                "이미지를 선택해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setImage() {
        tempFile?.let { file ->
            val options = BitmapFactory.Options()
            val originalBm = BitmapFactory.decodeFile(file.absolutePath, options)
            review_image_layout.visibility = View.GONE
            review_image.visibility = View.VISIBLE
            review_image.setImageBitmap(originalBm)
        }
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