//package com.kyril.gymondotest
//
//import android.util.Log
//import androidx.paging.PageKeyedDataSource
//import com.kyril.gymondotest.api.ExerciseResponse
//import com.kyril.gymondotest.api.WgerService
//import com.kyril.gymondotest.model.Exercise
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.concurrent.Executor
//import java.util.concurrent.Executors
//
//class ExerciseDataSource : PageKeyedDataSource<Int, Exercise>() {
//
//    //    private val database: AppDatabase = AppDatabase.getInstance()
//    val myExecutor: Executor = Executors.newSingleThreadExecutor()
//
//    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Exercise>) {
//
//        WgerService.create()
//            .getExercises(1, 20)
//            .enqueue(object : Callback<ExerciseResponse> {
//
//                override fun onResponse(call: Call<ExerciseResponse>, response: Response<ExerciseResponse>) {
//
//                    Log.d("ExerciseDataSource", "loadInitial")
//                    callback.onResult(response.body()!!.results, null, 2)
//
//                }
//
//                override fun onFailure(call: Call<ExerciseResponse>, t: Throwable) {
//                    Log.d("ExerciseDataSource - E", t.toString())
//                }
//
//            })
//    }
//
//    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Exercise>) {
//
//        WgerService.create()
//            .getExercises(params.key, 20)
//            .enqueue(object : Callback<ExerciseResponse> {
//
//                override fun onResponse(call: Call<ExerciseResponse>, response: Response<ExerciseResponse>) {
//
//                    Log.d("ExerciseDataSource", "loadAfter")
//
//                    val nextPage = params.key + 1
//                    callback.onResult(response.body()!!.results, nextPage)
//
//                }
//
//                override fun onFailure(call: Call<ExerciseResponse>, t: Throwable) {
//                    Log.d("ExerciseDataSource - E", t.toString())
//                }
//
//            })
//
//    }
//
//    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Exercise>) {
//
//    }
//}