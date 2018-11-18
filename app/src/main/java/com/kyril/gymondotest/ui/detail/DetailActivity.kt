package com.kyril.gymondotest.ui.detail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kyril.gymondotest.R
import com.kyril.gymondotest.db.AppDatabase
import com.kyril.gymondotest.model.Exercise
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var exercise: Exercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setUpToolbar()

        val sortId = intent!!.extras!!.getInt("sort_id")
        exercise = AppDatabase.getInstance(this).exerciseDao().getExerciseById(sortId)
        Log.d("Exerxise is: ", exercise.toString())

        loadExerciseDetails(exercise)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun loadExerciseDetails(exercise: Exercise) {

        if (!exercise.name.isNullOrBlank()) {
            exerciseNameTextView.text = exercise.name
        }

        if (!exercise.category.isNullOrBlank()) {
            exerciseCategoryTextView.text = exercise.category
        }

        if (!exercise.muscles.isNullOrBlank()) {
            exerciseMusclesTextView.text = exercise.muscles
        }

        if (!exercise.equipment.isNullOrBlank()) {
            exerciseEquipmentTextView.text = exercise.equipment
        }

        if (!exercise.description.isNullOrBlank()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                exerciseDescriptionTextView.text = Html.fromHtml(exercise.description, Html.FROM_HTML_MODE_COMPACT)
            } else {
                exerciseDescriptionTextView.text = Html.fromHtml(exercise.description)
            }
        }

    }

}
