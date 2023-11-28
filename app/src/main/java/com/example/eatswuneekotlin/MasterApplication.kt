package com.example.eatswuneekotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.eatswuneekotlin.server.ServiceApi
import com.example.eatswuneekotlin.server.login.AuthInterceptor
import com.example.eatswuneekotlin.server.login.TokenRefreshApi
import com.example.eatswuneekotlin.server.login.Utils
import com.facebook.stetho.Stetho
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MasterApplication : Application() {
    lateinit var serviceApi: ServiceApi
    private val baseUrl = "http://43.201.201.163:8080"
    private lateinit var activity : Context

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

    fun createRetrofit(currentActivity: Context) {
        activity = currentActivity

        val header = Interceptor {
            val original = it.request()
            if (checkIsLogin()) {
                getUserToken().let { token ->
                    Log.d("createRetrofit token", token.toString())
                    val request = original.newBuilder()
                        .header("Authorization","Bearer $token")
                        .build()
                    it.proceed(request)
                }
            } else {
                Log.d("createRetrofit token", "실행")
                it.proceed(original)
            }
        }

        // Retrofit 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("$baseUrl/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getRetrofitClient(header))
            .build()

        serviceApi = retrofit.create(ServiceApi::class.java)
    }

    /** TokenRefreshApi를 빌드 **/
    private fun buildTokenApi() : TokenRefreshApi {
        // 임시 클라이언트
        val client = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl("$baseUrl/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenRefreshApi::class.java)
    }


    /** Retrofit Client 생성 함수
     * Build : okhttp client
     * Interceptor를 통한 request를 보냄
     */
    private fun getRetrofitClient(header: Interceptor): OkHttpClient {
        val accessToken = Utils.getAccessToken("1234")

        return OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Authorization", "Bearer $accessToken")
                }.build())
            }.also { client ->
                client.addInterceptor(header)
                client.addInterceptor(AuthInterceptor(activity, buildTokenApi()))

                // 오류 관련 인터셉터도 등록 (오류 출력 가능)
                val logInterceptor = HttpLoggingInterceptor()
                logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logInterceptor)
            }.build()
    }

    // 401 에러 발생 시 인증자가 토큰을 새로 갱신하려고 시도
    // 새로 고침에 성공하면 사용자가 로그아웃 되지 않음

    private fun checkIsLogin() : Boolean{
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("accessToken","null")
        return token!="null"
        //is not default
    }
    private fun getUserToken(): String?{
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("accessToken","null")
        return if (token=="null") null
        else token
    }
}