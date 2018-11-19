package com.kyril.gymondotest.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.kyril.gymondotest.db.WgerLocalCache
import com.kyril.gymondotest.model.Exercise
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * This boundary callback gets notified when user reaches to the edges of the list for example when
 * the database cannot provide any more data.
 **/
class ExerciseBoundaryCallback(
        private val repository: WgerRepository,
        private val cache: WgerLocalCache
) : PagedList.BoundaryCallback<Exercise>(), AnkoLogger {

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = (cache.exerciseRows() / NETWORK_PAGE_SIZE) + 1

    private val _networkErrors = MutableLiveData<String>()

    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    override fun onZeroItemsLoaded() {
        debug("onZeroItemsLoaded")
        requestAndSaveData()
    }

    /**
     * When all items in the database were loaded, we need to query the backend for more items.
     */
    override fun onItemAtEndLoaded(itemAtEnd: Exercise) {
        debug("onItemAtEndLoaded $lastRequestedPage")
        requestAndSaveData()
    }


    // TODO: First download thumbnails, then insert exercises
    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        repository.getExercisesFromNetwork(lastRequestedPage, NETWORK_PAGE_SIZE, { exercises ->

            cache.insertExercises(exercises) {

                repository.getThumbnailsFromNetwork(exercises, cache) {
                    lastRequestedPage++
                    isRequestInProgress = false
                }
            }

        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }
}