package com.kyril.gymondotest.ui.detail

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.kyril.gymondotest.R
import com.kyril.gymondotest.db.AppDatabase
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.ui.GlideApp
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.toast

class DetailActivity : AppCompatActivity(), AnkoLogger {

    lateinit var exercise: Exercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setUpToolbar()

        val sortId = intent!!.extras!!.getInt("sort_id")
        exercise = AppDatabase.getInstance(this).exerciseDao().getExerciseById(sortId)
        debug(exercise)

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

        // Language icon
        when (exercise.languageId) {
            1 -> loadLanguageFlag(R.drawable.ic_german)
            2 -> loadLanguageFlag(R.drawable.ic_english)
            3 -> loadLanguageFlag(R.drawable.ic_bulgarian)
            4 -> loadLanguageFlag(R.drawable.ic_spanish)
            5 -> loadLanguageFlag(R.drawable.ic_russian)
            6 -> loadLanguageFlag(R.drawable.ic_dutch)
            7 -> loadLanguageFlag(R.drawable.ic_portuguese)
            8 -> loadLanguageFlag(R.drawable.ic_greek)
            9 -> loadLanguageFlag(R.drawable.ic_czech)
            10 -> loadLanguageFlag(R.drawable.ic_swedish)
            11 -> loadLanguageFlag(R.drawable.ic_norwegian)
            else -> toast("No language flag found")
        }

    }

    private fun loadLanguageFlag(icon: Int) {
        GlideApp.with(this)
                .load(icon)
                .into(exerciseLanguageImageView)
    }

}
