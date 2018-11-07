package com.kyril.gymondotest.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Wger API communication setup via Retrofit.
 */
interface WgerService {

    @GET("exercise/")
    fun getExercises(
        @Query("page") page: Int
    ): Call<ExerciseResponse>

    @GET("exerciseimage/")
    fun getImagesForExercise(
        @Query("exercise") exerciseId: Int
    ): Call<ImageResponse>

    @GET("exerciseimage/?limit=204")
    fun getAllImages(): Call<ImageResponse>

    @GET("exercisecategory/")
    fun getAllCategories(): Call<CategoryResponse>

    @GET("muscle/")
    fun getAllMuscles(): Call<MuscleResponse>

    companion object {
        private const val BASE_URL = "https://wger.de/api/v2/"

        fun create(): WgerService {

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(WgerService::class.java)
        }
    }
}