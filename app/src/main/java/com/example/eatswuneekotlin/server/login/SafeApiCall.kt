package com.example.eatswuneekotlin.server.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/** 안전한 api 전송을 위한 코드
 * Resource.Success(apiCall.invoke()) 코드를 반환하는 것이 목표
 * 실패할 경우 에러 반환
 */
interface SafeApiCall {

    suspend fun <T> safeApiCall(
        apiCall: suspend() -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                // 통신에 성공하면 전달 받은 값 전송
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when(throwable) {
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }
}