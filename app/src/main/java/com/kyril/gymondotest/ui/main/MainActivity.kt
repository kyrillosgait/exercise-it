package com.kyril.gymondotest.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyril.gymondotest.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ExerciseAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "Setting up ViewModel")
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        Log.d("MainActivity", "Configuring RecyclerView")

        val layoutManager = LinearLayoutManager(this)
        exercisesRecyclerView.layoutManager = layoutManager

        adapter = ExerciseAdapter()
        exercisesRecyclerView.adapter = adapter

        viewModel.getExercises().observe(this, Observer { adapter.submitList(it) })
    }
}
