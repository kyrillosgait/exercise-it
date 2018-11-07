package com.kyril.gymondotest.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kyril.gymondotest.R
import com.kyril.gymondotest.model.Exercise
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ExerciseAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidThreeTen.init(this)

        Log.d("MainActivity", "Setting up ViewModel")
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.init()

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        Log.d("MainActivity", "Configuring RecyclerView")

        val layoutManager = LinearLayoutManager(this)
        exercisesRecyclerView.layoutManager = layoutManager

        adapter = ExerciseAdapter { exercise : Exercise -> exerciseClicked(exercise) }
        exercisesRecyclerView.adapter = adapter

        viewModel.getExercises().observe(this, Observer { adapter.submitList(it) })
    }

    private fun exerciseClicked(exercise: Exercise) {
        Toast.makeText(this, "Clicked: ${exercise.id}", Toast.LENGTH_LONG).show()
    }
}
