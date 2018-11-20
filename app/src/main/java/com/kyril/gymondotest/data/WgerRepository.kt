package com.kyril.gymondotest.data

import androidx.paging.LivePagedListBuilder
import com.kyril.gymondotest.api.*
import com.kyril.gymondotest.db.WgerLocalCache
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.model.ExerciseResult
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository class that works with local and remote data sources.
 */


class WgerRepository(
        private val service: WgerService,
        private val cache: WgerLocalCache
) : AnkoLogger {

    /**
     * Get exercises for MainViewModel. It creates a new BoundaryCallback to handle downloading
     * new exercises when user is scrolling the list and inserting them to ROOM database.
     */
    fun getExercises(): ExerciseResult {
        debug("Getting Exercises...")
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
     * This is the way to get the exercise list from Wger API. It's called from BoundaryCallback
     * when the app needs to load new exercises to display.
     */
    fun getExercisesFromNetwork(
            page: Int,
            onSuccess: (exercises: List<Exercise>) -> Unit,
            onError: (error: String) -> Unit
    ) {

        WgerService.create()
                .getExercises(page)
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


    fun getImagesAndThumbnails(exercises: List<Exercise>) {

        for (exercise in exercises) {
            getImagesFromNetwork(exercise.id) {
                getThumbnailsFromNetwork(exercise.id, it)
            }

        }
    }

    private fun getImagesFromNetwork(exerciseId: Int, onSuccess: (imageId: Int) -> Unit) {

        WgerService.create()
            .getImagesById(exerciseId)
            .enqueue(object : Callback<ImageResponse> {

                override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                    val images = response.body()?.images
                    cache.updateExerciseImages(exerciseId, images)
                    if (!images.isNullOrEmpty()) {
                        onSuccess(images[0].id!!)
                    }
                }

                override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
//                        onError(t.toString())
                }
            })
    }

    /**
     * This function gets the exercise list from network and for each exercise.id it makes a new API
     * call for the thumbnail of each exercise. It's called after the network call for exercise list.
     * TODO: figure out if forward loop is better.
     */
    private fun getThumbnailsFromNetwork(exerciseId: Int, imageId: Int) {

        WgerService.create()
                .getThumbnailById(imageId)
                .enqueue(object : Callback<ThumbnailResponse> {

                    override fun onResponse(call: Call<ThumbnailResponse>, response: Response<ThumbnailResponse>) {
                        val thumbnail = response.body()?.thumbnail
                        cache.updateExerciseThumbnail(exerciseId, thumbnail!!.url)
                    }

                    override fun onFailure(call: Call<ThumbnailResponse>, t: Throwable) {
//                        Log.d("WgerService - E", t.toString())
                    }
                })
    }

    /**
     * Populates the database with the categoryRows from API
     */
    private fun getCategories() {
        debug("Starting Categories API Call...")
        WgerService.create()
                .getAllCategories()
                .enqueue(object : Callback<CategoryResponse> {
                    override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                        debug("Getting categoryRows...")
                        val categories = response.body()?.results
                        cache.insertCategories(categories!!) {}
                    }

                    override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                        debug(t)
                    }
                })
    }

    /**
     * Populates the database with the muscleRows from API
     */
    private fun getMuscles() {
        debug("Starting Muscles API Call...")
        WgerService.create()
                .getAllMuscles()
                .enqueue(object : Callback<MuscleResponse> {
                    override fun onResponse(call: Call<MuscleResponse>, response: Response<MuscleResponse>) {
                        debug("Getting muscleRows...")
                        val muscles = response.body()?.results
                        cache.insertMuscles(muscles!!) {}
                    }

                    override fun onFailure(call: Call<MuscleResponse>, t: Throwable) {
                        debug(t)
                    }
                })
    }

    /**
     * Populates the database with the equipmentRows from API
     */
    private fun getEquipment() {
        debug("Starting Equipment API Call...")

        WgerService.create()
                .getAllEquipment()
                .enqueue(object : Callback<EquipmentResponse> {
                    override fun onResponse(call: Call<EquipmentResponse>, response: Response<EquipmentResponse>) {
                        debug("Getting equipmentRows...")
                        val equipment = response.body()?.results
                        if (equipment != null) {
                            cache.insertEquipment(equipment) {}
                        }
                    }

                    override fun onFailure(call: Call<EquipmentResponse>, t: Throwable) {
                        debug(t)
                    }
                })
    }

    /**
     * This function makes an API call for the exercise's images.
     * TODO: Implement
     */
//    private fun getExerciseImagesById(exerciseId: Int) {
//
//        WgerService.create()
//                .getImagesById(exerciseId)
//                .enqueue(object : Callback<ImageResponse> {
//                    override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
//                        debug("Getting equipmentRows...")
//                        val imageUrls = response.body()?.results
//                    }
//
//                    override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
//                        debug(t)
//                    }
//                })
//    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }

}
