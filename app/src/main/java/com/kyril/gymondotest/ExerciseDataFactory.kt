//package com.kyril.gymondotest
//
//import androidx.lifecycle.MutableLiveData
//import androidx.paging.DataSource
//import com.kyril.gymondotest.model.Exercise
//
//
//class ExerciseDataFactory : DataSource.Factory<Int, Exercise>() {
//
//    private val exerciseDataSourceLiveData = MutableLiveData<ExerciseDataSource>()
//
//    override fun create(): DataSource<Int, Exercise> {
//        val exerciseDataSource = ExerciseDataSource()
//        exerciseDataSourceLiveData.postValue(exerciseDataSource)
//        return exerciseDataSource
//    }
//}