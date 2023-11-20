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
                            // 기존 토큰
                            Log.d("confirm tokenValue", sp.getString("accessToken", "no token").toString())
                            val tokenValue = token.value!!

                            // new 토큰
                            Log.d("confirm newToken", token.value.accessToken)
                            val accessToken = tokenValue.accessToken
                            val refreshToken =tokenValue.refreshToken
                            editor.putString("accessToken", accessToken)
                            editor.putString("refreshToken", refreshToken)
                            editor.apply()

                            // 기존 토큰 지우고 새로 response 반환
                            val newRequest = chain.request().newBuilder().removeHeader("Authorization")
                            newRequest.addHeader("Authorization", "Bearer $accessToken")
                            Log.d("newRequest response 1", newRequest.toString())
                            return@runBlocking chain.proceed(newRequest.build())

                        }
                        else -> {
                            Log.d("newRequest response 2", response.request.toString())

                            return@runBlocking response
                        }
                    }
                }
            }
            403 -> {

            }
            404 -> {

            }
        }
        Log.d("newRequest response 3", response.request.toString())
        // 에러가 아니라면 정상 response 반환
        return response
    }

    private suspend fun getUpdateToken() : Resource<Token?> {
        val refreshToken = context.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            .getString("refreshToken", "").toString()

        // updateToken 확인
        Log.d("confirm updateToken", refreshToken.toString())

        // safeApiCall을 통한 api 요청
        // refresh token이 비었을 경우에는 null 전송을 통해서 에러 반환을 받음
        return safeApiCall { tokenApi.patchToken(refreshToken) }
    }
}