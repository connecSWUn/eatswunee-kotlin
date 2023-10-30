package com.example.eatswuneekotlin.server.login

import android.content.Context
import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context,
    private val tokenApi: TokenRefreshApi,
) : Interceptor, BaseRepository() {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when(response.code) {
            400 -> {

            }
            401 -> {
                // 토큰 재인증
                return runBlocking {
                    when (val token = getUpdateToken()) {
                        is Resource.Success -> {
                            val sp = context.getSharedPreferences("login_sp",Context.MODE_PRIVATE)
                            val editor = sp.edit()
                            val tokenValue = token.value!!
                            val accessToken = tokenValue.accessToken
                            val refreshToken =tokenValue.refreshToken
                            editor.putString("accessToken", accessToken)
                            editor.putString("refreshToken", refreshToken)
                            editor.apply()

                            response.request.newBuilder()
                                .header("Authorization","Bearer $accessToken")
                                .build()
                        }
                        else -> { }
                    }
                    response
                }
            }
            403 -> {

            }
            404 -> {

            }
        }
        return response
    }

    private suspend fun getUpdateToken() : Resource<Token?> {
        val refreshToken = context.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            .getString("refreshToken", "").toString()

        // safeApiCall을 통한 api 요청
        // refresh token이 비었을 경우에는 null 전송을 통해서 에러 반환을 받음
        return safeApiCall { tokenApi.patchToken(refreshToken) }
    }
}