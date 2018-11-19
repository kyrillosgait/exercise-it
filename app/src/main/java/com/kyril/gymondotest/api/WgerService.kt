package com.kyril.gymondotest.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Wger API communication setup via Retrofit.
 */
interface WgerService {

    @GET("exercise/")
    fun getExercises(
            @Query("page") page: Int,
            @Query("limit") itemsPerPage: Int
    ): Call<ExerciseResponse>

    @GET("exercisecategory/")
    fun getAllCategories(): Call<CategoryResponse>

    @GET("equipmentRows/")
    fun getAllEquipment(): Call<EquipmentResponse>

    @GET("muscle/")
    fun getAllMuscles(): Call<MuscleResponse>

    @GET("exerciseimage/{id}/thumbnails")
    fun getThumbnailById(
            @Path("id") exerciseId: Int
    ): Call<ThumbnailResponse>

    @GET("exerciseimage/{exercise}")
    fun getImagesById(
            @Path("exercise") exerciseId: Int
    ): Call<ImageResponse>

    companion object {
        private const val BASE_URL = "https://wger.de/api/v2/"

        fun create(): WgerService {

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            val gson = GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()
                    .create(WgerService::class.java)
        }
    }
}