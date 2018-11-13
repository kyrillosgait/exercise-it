package com.kyril.gymondotest.data

import android.util.Log
import androidx.paging.LivePagedListBuilder
import com.kyril.gymondotest.api.CategoryResponse
import com.kyril.gymondotest.api.EquipmentResponse
import com.kyril.gymondotest.api.MuscleResponse
import com.kyril.gymondotest.api.WgerService
import com.kyril.gymondotest.db.WgerLocalCache
import com.kyril.gymondotest.model.ExerciseResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository class that works with local and remote data sources.
 */

class WgerRepository(
    private val service: WgerService,
    private val cache: WgerLocalCache
) {

    /**
     * Get exercises
     */
    fun getExercises(): ExerciseResult {
        Log.d("WgerRepository", "Getting Exercises...")

        // Get data source factory from the local cache
        val dataSourceFactory = cache.exercises()

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = ExerciseBoundaryCallback(service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return ExerciseResult(data, networkErrors)
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }

    fun getInitialData() {

        if (cache.categories() == 0) {
            getCategories()
        }

        if (cache.equipment() == 0) {
            getEquipment()
        }

        if (cache.muscles() == 0) {
            getMuscles()
        }

    }

    private fun getCategories() {
        Log.d("WgerRepository", "Starting Categories API Call...")

        WgerService.create()
            .getAllCategories()
            .enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                    Log.d("WgerRepository - S", "Getting categories...")
                    val categories = response.body()?.results

                    cache.insertCategories(categories!!) {}
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    Log.d("WgerRepository - E", t.toString())
                }
            })
    }

    private fun getMuscles() {
        Log.d("WgerRepository", "Starting Muscles API Call...")

        WgerService.create()
            .getAllMuscles()
            .enqueue(object : Callback<MuscleResponse> {

                override fun onResponse(call: Call<MuscleResponse>, response: Response<MuscleResponse>) {
                    Log.d("WgerRepository - S", "Getting muscles...")
                    val muscles = response.body()?.results

                    cache.insertMuscles(muscles!!) {}
                }

                override fun onFailure(call: Call<MuscleResponse>, t: Throwable) {
                    Log.d("WgerRepository - E", t.toString())
                }
            })
    }

    private fun getEquipment() {
        Log.d("WgerRepository", "Starting Equipment API Call...")

        WgerService.create()
            .getAllEquipment()
            .enqueue(object : Callback<EquipmentResponse> {

                override fun onResponse(call: Call<EquipmentResponse>, response: Response<EquipmentResponse>) {
                    Log.d("WgerRepository - S", "Getting equipment...")
                    val equipment = response.body()?.results

                    cache.insertEquipment(equipment!!) {}
                }

                override fun onFailure(call: Call<EquipmentResponse>, t: Throwable) {
                    Log.d("WgerRepository - E", t.toString())
                }
            })
    }

}
