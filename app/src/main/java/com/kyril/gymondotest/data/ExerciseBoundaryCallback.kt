package com.kyril.gymondotest.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.kyril.gymondotest.db.WgerLocalCache
import com.kyril.gymondotest.model.Exercise

/**
 * This boundary callback gets notified when user reaches to the edges of the list for example when
 * the database cannot provide any more data.
 **/
class ExerciseBoundaryCallback(
    private val repository: WgerRepository,
    private val cache: WgerLocalCache
) : PagedList.BoundaryCallback<Exercise>() {

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    // figure out which page was last downloaded and increment the number
    private var lastRequestedPage = (cache.exerciseRows() / NETWORK_PAGE_SIZE) + 1

    private val _networkErrors = MutableLiveData<String>()

    // LiveData of network errors
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    /**
     * When all items in the database were loaded, we need to query the backend for more items.
     */
    override fun onItemAtEndLoaded(itemAtEnd: Exercise) {
        requestAndSaveData()
    }

    /**
     * This function first downloads the exercise list, then matches the exercise id's with the real
     * category/muscle/equipment names and it inserts them one by one in the database. Then it makes
     * a call to get the exercises' images and thumbnails.
     */
    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        repository.getExercisesFromNetwork(lastRequestedPage, { exercises ->

            cache.insertExercises(exercises) {
                lastRequestedPage++
                isRequestInProgress = false
                repository.getImagesAndThumbnails(exercises)
            }

        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }
}