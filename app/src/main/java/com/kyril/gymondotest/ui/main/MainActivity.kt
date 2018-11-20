package com.kyril.gymondotest.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyril.gymondotest.R
import com.kyril.gymondotest.di.Injection
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ExerciseAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpToolbar()

        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
            .get(MainViewModel::class.java)

        setUpRecyclerView()

        viewModel.init("yes")
    }

    private fun setUpToolbar() {
        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarTitleTextView?.setText(R.string.main_toolbar_title)
    }

    private fun setUpRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        exercisesRecyclerView.layoutManager = layoutManager

        adapter = ExerciseAdapter { exercise: Exercise -> exerciseClicked(exercise) }
        exercisesRecyclerView.adapter = adapter
        exercisesRecyclerView.hasFixedSize()

        viewModel.exercises.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkErrors.observe(this, Observer<String> {
            when (it) {
                errorNoInternetConnection -> mainCoordinatorLayout.longSnackbar(
                    "No internet connection.",
                    "Retry"
                ) { viewModel.init("retry") }
                else -> mainCoordinatorLayout.longSnackbar("All exercises have been loaded.")
            }
        })
    }

    private fun exerciseClicked(exercise: Exercise) {
        startActivity(intentFor<DetailActivity>("exercise_id" to exercise.id).singleTop())
    }

    companion object {
        const val errorNoInternetConnection = "Unable to resolve host \"wger.de\": No address associated with hostname"
    }

}
