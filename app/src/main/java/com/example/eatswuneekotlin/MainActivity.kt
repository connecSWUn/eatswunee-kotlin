package com.example.eatswuneekotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.eatswuneekotlin.community.articlesActivity
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.chat.ChatListActivity
import com.example.eatswuneekotlin.server.chat.MyFirebaseMessagingService
import com.example.eatswuneekotlin.server.login.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mDrawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    lateinit var nav_header_text: TextView
    lateinit var nav_header_image: ImageView

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        val toolbar = findViewById<View>(R.id.review_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_format_list_bulleted_24)

        bottomNavigationView = findViewById(R.id.bottomNavi)
        mDrawerLayout = findViewById(R.id.drawer_layout)

        mDrawerToggle =
            ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close)
        mDrawerLayout.addDrawerListener(mDrawerToggle!!)
        mDrawerToggle!!.syncState()
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView!!.setNavigationItemSelectedListener { item ->
            mDrawerLayout.closeDrawers()
            val id = item.itemId
            if (id == R.id.item_info) {
                val intent = Intent(this@MainActivity, articlesActivity::class.java)
                startActivity(intent)
            } else if (id == R.id.item_chat) {
                val intent = Intent(this@MainActivity, ChatListActivity::class.java)
                startActivity(intent)
            }
            true
        }
        init()

        // 초기 화면 : FrameLayout에 fragment.xml 띄우기
        supportFragmentManager.beginTransaction().add(R.id.mainFrameLayout, orderFragment())
            .commit()

        // 바텀 네비게이션 기본 선택 화면
        bottomNavigationView.setSelectedItemId(R.id.item_cafeteria)

        // 바텀 네비게이션 뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_community -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrameLayout, CommunityFragment()).commit()
                R.id.item_cafeteria -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrameLayout, orderFragment()).commit()
                R.id.item_mypage -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrameLayout, MyPageFragment()).commit()
            }
            true
        })


        /** FCM 설정, Token값 가져오기 */
        MyFirebaseMessagingService().getFirebaseToken()

        /** PostNotification 대응 */
        checkAppPushNotification()

        // 사용 안 하면 삭제하기
        /** DynamicLink 수신 확인 */
        initDynamicLink()
    }

    /** Android 13 PostNotification */
    private fun checkAppPushNotification() {
        // Android 13 이상 && 푸시 권한 없음
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)) {
            // 푸쉬 권한 없음
            permissionPostNotification.launch(Manifest.permission.POST_NOTIFICATIONS)
            return
        }

        // 권한이 있을 때
    }

    /** 권한 요청 **/
    private val permissionPostNotification =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 권한 허용
            } else {
                // 권한 비허용
            }
        }

    // 사용 안 하면 삭제하기
    /** DynamicLink **/
    private fun initDynamicLink() {
        val dynamicLinkData = intent.extras
        if (dynamicLinkData != null) {
            var dataStr = "DynamicLink 수신받은 값 \n"
            for (key in dynamicLinkData.keySet()) {
                dataStr += "key: $key / value: ${dynamicLinkData.getString(key)}\n"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.menu_shopping_bag -> {
                val intent = Intent(this@MainActivity, ShopBagActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_notification -> {
                val intent = Intent(this@MainActivity, notificationActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun init() {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@MainActivity)

        val service = masterApp.serviceApi

        service.getProfile().enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val result = response.body()
                val data = result!!.data
                Log.d("retrofit", "Data fetch success")

                val nav_header = navigationView!!.getHeaderView(0)
                nav_header_text = nav_header.findViewById(R.id.navi_header_name)
                nav_header_text.text = data!!.user_name
                nav_header_image = nav_header.findViewById(R.id.navi_header_img)
                DownloadFilesTask().execute(data.user_profile_url)
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
            nav_header_image!!.setImageBitmap(result)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}