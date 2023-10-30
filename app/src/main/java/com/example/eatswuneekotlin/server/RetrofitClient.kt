package com.example.eatswuneekotlin.server

import retrofit2.Retrofit

class RetrofitClient {

    companion object {
        @JvmStatic
        var instance: RetrofitClient? = null
            get() {
                if (field == null) {
                    field = RetrofitClient()
                }
                return field
            }
            private set
        private val retrofit: Retrofit? = null
        @JvmStatic
        lateinit var serviceApi: ServiceApi
            private set
        private const val baseUrl = "http://43.201.201.163:8080"
    }
}