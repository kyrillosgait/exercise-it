package com.kyril.gymondotest.ui.detail

import androidx.lifecycle.ViewModel
import com.kyril.gymondotest.data.WgerRepository
import org.jetbrains.anko.AnkoLogger

class DetailViewModel(private val repository: WgerRepository) : ViewModel(), AnkoLogger {

//    private var imageList = MutableLiveData<List<Image>>().apply { postValue(emptyList()) }
//
//    fun getImagesForExercise(exerciseId: Int): MutableLiveData<List<Image>> {
//        imageList.postValue(repository.getImagesFromDb(exerciseId))
//        return imageList
//    }
}