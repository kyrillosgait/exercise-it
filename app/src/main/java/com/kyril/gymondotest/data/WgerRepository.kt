package com.kyril.gymondotest.data

import androidx.paging.LivePagedListBuilder
import com.kyril.gymondotest.api.CategoryResponse
import com.kyril.gymondotest.api.EquipmentResponse
import com.kyril.gymondotest.api.MuscleResponse
import com.kyril.gymondotest.api.WgerService
import com.kyril.gymondotest.db.WgerLocalCache
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
     * Get exercises
     */
    fun getExercises(): ExerciseResult {
        debug("Getting Exercises...")
        val dataSourceFactory = cache.exercises()

        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = ExerciseBoundaryCallback(cache)
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
        debug("Starting Categories API Call...")
        WgerService.create()
                .getAllCategories()
                .enqueue(object : Callback<CategoryResponse> {
                    override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                        debug("Getting categories...")
                        val categories = response.body()?.results
                        cache.insertCategories(categories!!) {}
                    }

                    override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                        debug(t)
                    }
                })
    }

    private fun getMuscles() {
        debug("Starting Muscles API Call...")
        WgerService.create()
                .getAllMuscles()
                .enqueue(object : Callback<MuscleResponse> {
                    override fun onResponse(call: Call<MuscleResponse>, response: Response<MuscleResponse>) {
                        debug("Getting muscles...")
                        val muscles = response.body()?.results
                        cache.insertMuscles(muscles!!) {}
                    }

                    override fun onFailure(call: Call<MuscleResponse>, t: Throwable) {
                        debug(t)
                    }
                })
    }

    private fun getEquipment() {
        debug("Starting Equipment API Call...")

        WgerService.create()
                .getAllEquipment()
                .enqueue(object : Callback<EquipmentResponse> {
                    override fun onResponse(call: Call<EquipmentResponse>, response: Response<EquipmentResponse>) {
                        debug("Getting equipment...")
                        val equipment = response.body()?.results
                        cache.insertEquipment(equipment!!) {}
                    }

                    override fun onFailure(call: Call<EquipmentResponse>, t: Throwable) {
                        debug(t)
                    }
                })
    }

    private fun getExerciseImagesById(exerciseId: Int) {

        WgerService.create()
                .getAllEquipment()
                .enqueue(object : Callback<EquipmentResponse> {
                    override fun onResponse(call: Call<EquipmentResponse>, response: Response<EquipmentResponse>) {
                        debug("Getting equipment...")
                        val equipment = response.body()?.results
                        cache.insertEquipment(equipment!!) {}
                    }

                    override fun onFailure(call: Call<EquipmentResponse>, t: Throwable) {
                        debug(t)
                    }
                })
    }

}
