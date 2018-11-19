package com.kyril.gymondotest.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyril.gymondotest.Injection
import com.kyril.gymondotest.R
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var adapter: ExerciseAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpToolbar()
        debug("Setting up ViewModel")
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(MainViewModel::class.java)

        setUpRecyclerView()

        viewModel.init("yes")
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarTitleTextView?.setText(R.string.main_toolbar_title)
    }

    private fun setUpRecyclerView() {
        debug("Configuring RecyclerView")

        val layoutManager = LinearLayoutManager(this)
        exercisesRecyclerView.layoutManager = layoutManager

        adapter = ExerciseAdapter { exercise: Exercise -> exerciseClicked(exercise) }
        exercisesRecyclerView.adapter = adapter
        exercisesRecyclerView.hasFixedSize()

        viewModel.exercises.observe(this, Observer {
            debug(it)
            adapter.submitList(it)
        })

        viewModel.networkErrors.observe(this, Observer<String> { toast("\uD83D\uDE28 Wooops $it") })
    }

    private fun exerciseClicked(exercise: Exercise) {
//        Toast.makeText(this, "Clicked: ${exercise.sortId}", Toast.LENGTH_LONG).show()
        startActivity(intentFor<DetailActivity>("sort_id" to exercise.sortId).singleTop())
    }
}
