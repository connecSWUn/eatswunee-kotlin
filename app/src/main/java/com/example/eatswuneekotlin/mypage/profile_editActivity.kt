package com.example.eatswuneekotlin.mypage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat


class profile_editActivity : AppCompatActivity() {
    private lateinit var logout: Button
    private lateinit var withdrawal: Button
    private lateinit var isDuplicated: Button
    private lateinit var camera_btn: ImageButton
    private lateinit var user_id: TextView
    private lateinit var nickname: EditText
    private lateinit var profile: ImageView

    private lateinit var mCurrentPhotoPath: String
    private lateinit var imageURI: Uri
    private lateinit var photoURI: Uri
    private lateinit var albumURI: Uri

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
        isDuplicated = findViewById(R.id.nick_duplicated)
        isDuplicated.setOnClickListener(confirmOnClickListener())
        camera_btn = findViewById(R.id.camera_btn)
        camera_btn.setOnClickListener(CameraOnClickListener())

        user_id = findViewById(R.id.edit_id)
        nickname = findViewById(R.id.edit_nickname)
        profile = findViewById(R.id.edit_profile)

        // 로그아웃, 회원탈퇴 버튼 밑줄 표현
        logout.paintFlags = logout.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        withdrawal.paintFlags = withdrawal.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }

    private inner class confirmOnClickListener() : View.OnClickListener {
        @SuppressLint("UseCheckPermission")
        override fun onClick(view: View) {
            val masterApp = MasterApplication()
            masterApp.createRetrofit(this@profile_editActivity)

            val service = masterApp.serviceApi

            service.changeNickname(nickname.text.toString()).enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    val result = response.body()
                    val data = result?.data
                    if(!data!!.isIs_duplicated){
                        Toast.makeText(this@profile_editActivity, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@profile_editActivity, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Toast.makeText(this@profile_editActivity, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }

            })
        }
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
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@profile_editActivity)

        val service = masterApp.serviceApi

        service.getProfile().enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val result = response.body()
                val data = result!!.data

                Log.d("user_profile", data.user_profile_url)

                nickname!!.setText(data?.user_name)
                user_id!!.text = data?.loginId
                DownloadFilesTask().execute(data?.user_profile_url)
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    internal inner class DownloadFilesTask : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg strings: String?): Bitmap? {
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

            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.one -> {
                        // 실제 카메라 구동 코드는 함수로 처리
                        captureCamera()
                    }
                    R.id.two -> {
                        // 갤러리에 관한 권한을 받아오는 코드
                        getAlbum()
                    }
                    /*
                    R.id.three -> {
                        // 올린 이미지 삭제

                    }
                     */
                }
                true
            }
            pop.show()
        }
    }

    // 카메라 촬영 프로필 업데이트 : 상수 지정
    private val PERMISSION_CAMERA : Int = 1111
    private val REQUEST_CAMERA : Int = 2222
    lateinit var realUri : Uri

    // 앨범 사진 프로필 업데이트 : 상수 지정
    private val PERMISSION_ALBUM = 101 // 앨범 권한 처리
    private val REQUEST_STORAGE = 3333


    // 내부 함수로 처리하는 사진을 촬영할 수 있도록 하는 함수 구현
    // 사진 촬영 함수
    fun captureCamera() {
        requirePermissions(arrayOf(android.Manifest.permission.CAMERA), PERMISSION_CAMERA)
    }

    // 갤러리에서 사진이 추가되고 선택할 수 있도록 하는 함수 구현
    // profile_menu에서 갤러리를 클릭하면 getAlbum 함수 호출
    private fun getAlbum(){
        requirePermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_ALBUM)
    }


    /** 자식 액티비티에서 권한 요청 시 직접 호출하는 메소드
     * @param permissions 권한 처리를 할 권한 목록
     * @param requestCode 권한을 요청한 주체가 어떤 것인지 구분하기 위함.
     * */
    fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            // isAllPermissionsGranted : 권한이 모두 승인되었는지 여부 저장
            // all 메서드를 사용하면 배열 속에 들어있는 모든 값을 체크할 수 있다.
            val isAllPermissionsGranted =
                permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
            if(isAllPermissionsGranted) {
                permissionGranted(requestCode)
            } else {
                // 사용자에 권한 승인 요청
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }
        }
    }

    /** 사용자가 권한을 승인하거나 거부한 다음에 호출되는 메서드
     * @param requestCode 요청한 주체를 확인하는 코드
     * @param permissions 요청한 권한 목록
     * @param grantResults 권한 목록에 대한 승인/미승인 값, 권한 목록의 개수와 같은 수의 결괏값이 전달된다.
     * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    private fun permissionGranted(requestCode: Int) {
        when(requestCode) {
            PERMISSION_CAMERA -> openCamera()
            PERMISSION_ALBUM -> openGallery()
        }
    }

    private fun permissionDenied(requestCode: Int) {
        when(requestCode) {
            PERMISSION_CAMERA -> Toast.makeText(
                this,
                "카메라 권한을 승인해야 카메라를 사용할 수 있습니다.",
                Toast.LENGTH_LONG
            ).show()

            PERMISSION_ALBUM -> Toast.makeText(
                this,
                "저장소 권한을 승인해야 앨범에서 이미지를 불러올 수 있습니다.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // 카메라를 이용해 찍은 값을 Uri로 받기 위한 코드
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri
            // MediaStore.EXTRA_OUTPUT을 Key로 하여 Uri를 넘겨주면
            // 일반적인 Camera App은 이를 받아 내가 지정한 경로에 사진을 찍어서 저장시킨다.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    // 앨범에서 선택된 이미지를 Uri로 받기 위한 코드
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_STORAGE)
    }

    private fun newFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }

    private fun createImageUri(filename: String, mimeType: String) : Uri? {
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    /** 카메라 및 앨범 Intent 결과
     * */
    @SuppressLint("RestrictedApi", "Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@profile_editActivity)

        val service = masterApp.serviceApi

        if(resultCode == RESULT_OK) {
            when(requestCode) {
                REQUEST_CAMERA -> {
                    realUri?.let { uri ->
                        // 서버 업로드를 위해 파일 형태로 변환
                        var imageFile = File(getRealPathFromURI(uri))
                        DownloadFilesTask().execute("file://$imageFile")

                        service.uploadProfile(imageFile).enqueue(object : Callback<Result>{
                            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                                val result = response.body()
                                val data = result?.data

                                Log.d("upload", data?.isIs_duplicated.toString())
                                Toast.makeText(this@profile_editActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<Result>, t: Throwable) {
                                Toast.makeText(this@profile_editActivity, "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                }

                REQUEST_STORAGE -> {
                    data?.data?.let { uri ->
                        // 서버 업로드를 위해 파일 형태로 변환한다
                        var imageFile = File(getRealPathFromURI(uri))
                        DownloadFilesTask().execute("file://$imageFile")

                        service.uploadProfile(imageFile).enqueue(object : Callback<Result>{
                            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                                val result = response.body()
                                val data = result?.data

                                Log.d("upload", data?.isIs_duplicated.toString())
                                Toast.makeText(this@profile_editActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<Result>, t: Throwable) {
                                Toast.makeText(this@profile_editActivity, "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                }
            }
        }
    }


    /**
     * @param uri: data에서 content 형태로 받은 Uri
     *  다른 Activity 혹은 Fragment에서 활용하기 위해 Real Path를 담은 Uri 생성 함수
     */
    fun getRealPathFromURI (uri : Uri) : String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")) {
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }
}