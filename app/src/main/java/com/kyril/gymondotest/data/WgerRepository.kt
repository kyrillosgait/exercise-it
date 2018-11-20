package com.kyril.gymondotest.data

import androidx.paging.LivePagedListBuilder
import com.kyril.gymondotest.api.*
import com.kyril.gymondotest.db.WgerLocalCache
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.model.ExerciseResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository class that works with local and remote data sources.
 */

class WgerRepository(private val service: WgerService, private val cache: WgerLocalCache) {

    /**
     * Get exercises for MainViewModel. It creates a new BoundaryCallback to handle downloading
     * new exercises when user is scrolling the list and inserting them to ROOM database.
     */
    fun getExercises(): ExerciseResult {
        val dataSourceFactory = cache.exercises()

        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = ExerciseBoundaryCallback(this, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return ExerciseResult(data, networkErrors)
    }

    /**
     * This is the way to get the exercise list from Wger API. It's called from BoundaryCallback
     * when the app needs to load new exercises to display.
     */
    fun getExercisesFromNetwork(
        page: Int,
        onSuccess: (exercises: List<Exercise>) -> Unit,
        onError: (error: String) -> Unit
    ) {

        service.getExercises(page)
            .enqueue(object : Callback<ExerciseResponse> {
                override fun onResponse(call: Call<ExerciseResponse>?, response: Response<ExerciseResponse>) {
                    if (response.isSuccessful) {
                        val exercises = response.body()?.exercises ?: emptyList()
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

    /**
     * It's called from ExerciseBoundaryCallback, after getting the list of exercises, so we can
     * make API calls to get images and thumbnails.
     */
    fun getImagesAndThumbnails(exercises: List<Exercise>) {

        for (exercise in exercises) {
            getImagesFromNetwork(exercise.id) {
                getThumbnailsFromNetwork(exercise.id, it)
            }
        }
    }

    private fun getImagesFromNetwork(exerciseId: Int, onSuccess: (imageId: Int) -> Unit) {

        service.getImagesById(exerciseId)
            .enqueue(object : Callback<ImageResponse> {

                override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                    val images = response.body()?.images
                    cache.updateExerciseImages(exerciseId, images!!)
                    if (!images.isNullOrEmpty()) {
                        onSuccess(images[0].id!!)
                    }
                }

                override fun onFailure(call: Call<ImageResponse>, t: Throwable) {}
            })
    }

    private fun getThumbnailsFromNetwork(exerciseId: Int, imageId: Int) {

        service.getThumbnailById(imageId)
            .enqueue(object : Callback<ThumbnailResponse> {
                override fun onResponse(call: Call<ThumbnailResponse>, response: Response<ThumbnailResponse>) {
                    val thumbnail = response.body()?.thumbnail
                    cache.updateExerciseThumbnail(exerciseId, thumbnail!!.url)
                }

                override fun onFailure(call: Call<ThumbnailResponse>, t: Throwable) {}
            })
    }

    /**
     * It's called from ViewModel. It pre-loads the database with the categoryRows, equipmentRows, and,
     * muscleRows, so the app is able to link each exercise category/equipmentRows/muscle ids with the real
     * names. TODO: There is probably a much better way to do the initial loading.
     */
    fun getInitialData() {
        if (cache.categoryRows() == 0) {
            getCategories()
        }
        if (cache.equipmentRows() == 0) {
            getEquipment()
        }
        if (cache.muscleRows() == 0) {
            getMuscles()
        }
    }

    /**
     * Populates the database with the categoryRows from API
     */
    private fun getCategories() {

        service.getAllCategories()
            .enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                    val categories = response.body()?.categories
                    cache.insertCategories(categories!!)
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {}
            })
    }

    /**
     * Populates the database with the muscleRows from API
     */
    private fun getMuscles() {

        service.getAllMuscles()
            .enqueue(object : Callback<MuscleResponse> {
                override fun onResponse(call: Call<MuscleResponse>, response: Response<MuscleResponse>) {
                    val muscles = response.body()?.muscles
                    cache.insertMuscles(muscles!!)
                }

                override fun onFailure(call: Call<MuscleResponse>, t: Throwable) {}
            })
    }

    /**
     * Populates the database with the equipmentRows from API
     */
    private fun getEquipment() {

        service.getAllEquipment()
            .enqueue(object : Callback<EquipmentResponse> {
                override fun onResponse(call: Call<EquipmentResponse>, response: Response<EquipmentResponse>) {
                    val equipment = response.body()?.equipment
                    cache.insertEquipment(equipment!!)
                }

                override fun onFailure(call: Call<EquipmentResponse>, t: Throwable) {}
            })
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }

}
