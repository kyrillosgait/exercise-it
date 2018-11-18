package com.kyril.gymondotest.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.kyril.gymondotest.db.WgerLocalCache
import com.kyril.gymondotest.model.Exercise
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Call to get exercise list
fun getExercises(
    page: Int,
    itemsPerPage: Int,
    onSuccess: (repos: List<Exercise>) -> Unit,
    onError: (error: String) -> Unit
) {

    WgerService.create()
        .getExercises(page, itemsPerPage)
        .enqueue(object : Callback<ExerciseResponse> {

            override fun onResponse(call: Call<ExerciseResponse>?, response: Response<ExerciseResponse>) {
                if (response.isSuccessful) {
                    val exercises = response.body()?.results ?: emptyList()
                    onSuccess(exercises)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }

            override fun onFailure(call: Call<ExerciseResponse>?, t: Throwable) {
                onError(t.message ?: "unknown error")
            }
        })
}

// Call to get thumbnails
fun getThumbnails(
    exercises: List<Exercise>,
    cache: WgerLocalCache,
    onCallFinished: () -> Unit
) {
    var i = 0
    fun forwardLoop() {
        if (i == exercises.size - 1) {
            return //loop is finished;
        }
        i++

        Log.d("WgerService", "Looking for thumbnails of exercise with id: " + exercises[i].id)
        WgerService.create()
            .getThumbnailById(exercises[i].id)
            .enqueue(object : Callback<ThumbnailResponse> {

                override fun onResponse(call: Call<ThumbnailResponse>, response: Response<ThumbnailResponse>) {
                    val thumbnail = response.body()?.thumbnail
                    cache.updateExercise(exercises[i].id, thumbnail?.url!!)
                    forwardLoop()
                }

                override fun onFailure(call: Call<ThumbnailResponse>, t: Throwable) {
                    Log.d("WgerService - E", t.toString())
                    forwardLoop()
                }
            })
    }

    forwardLoop()
    onCallFinished()
}

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

    @GET("equipment/")
    fun getAllEquipment(): Call<EquipmentResponse>

    @GET("muscle/")
    fun getAllMuscles(): Call<MuscleResponse>

    @GET("exerciseimage/{id}/thumbnails")
    fun getThumbnailById(
        @Path("id") exerciseId: Int
    ): Call<ThumbnailResponse>

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