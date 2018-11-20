package com.kyril.gymondotest.ui.detail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyril.gymondotest.R
import com.kyril.gymondotest.db.AppDatabase
import com.kyril.gymondotest.model.Exercise
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    lateinit var exercise: Exercise
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val exerciseId = intent!!.extras!!.getInt("exercise_id")
        exercise = AppDatabase.getInstance(this).exerciseDao().getExerciseById(exerciseId)

        setUpToolbar()
        loadExerciseDetails(exercise)
        setUpRecyclerView()

    }

    /**
     * Adds language flag as action bar icon to the toolbar depending on the exercise's language
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        when (exercise.languageId) {
            1 -> addFlagToActionBar(menu, R.drawable.ic_german)
            2 -> addFlagToActionBar(menu, R.drawable.ic_english)
            3 -> addFlagToActionBar(menu, R.drawable.ic_bulgarian)
            4 -> addFlagToActionBar(menu, R.drawable.ic_spanish)
            5 -> addFlagToActionBar(menu, R.drawable.ic_russian)
            6 -> addFlagToActionBar(menu, R.drawable.ic_dutch)
            7 -> addFlagToActionBar(menu, R.drawable.ic_portuguese)
            8 -> addFlagToActionBar(menu, R.drawable.ic_greek)
            9 -> addFlagToActionBar(menu, R.drawable.ic_czech)
            10 -> addFlagToActionBar(menu, R.drawable.ic_swedish)
            11 -> addFlagToActionBar(menu, R.drawable.ic_norwegian)
        }

        return true
    }

    private fun addFlagToActionBar(menu: Menu, languageFlag: Int) {
        menu.add(0, 0, 0, "Language")
                .setIcon(languageFlag)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }


    private fun setUpToolbar() {
        setSupportActionBar(detailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (!exercise.name.isNullOrBlank()) {
            detailToolbarTitleTextView.text = exercise.name
        }
    }

    private fun setUpRecyclerView() {
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseImagesRecyclerView.layoutManager = horizontalLayoutManager

        adapter = ImageAdapter()
        exerciseImagesRecyclerView.adapter = adapter

        if (exercise.images.isNullOrEmpty()) {
            exerciseNoImagesTextView.visibility = View.VISIBLE
        } else {
            adapter.submitList(exercise.images)
        }

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
