package com.kyril.gymondotest.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.kyril.gymondotest.R
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.ui.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.exercise_list_item.*

/**
 * Using ListAdapter instead of RecyclerViewAdapter, added in support library 27.1.0
 */

class ExerciseAdapter(private val clickListener: (Exercise) -> Unit) :
        PagedListAdapter<Exercise, ExerciseAdapter.ExerciseViewHolder>(ExerciseDiffCallback()) {

    class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExerciseViewHolder(inflater.inflate(R.layout.exercise_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ExerciseViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        private val context = containerView.context!!

        // Use Android's CircularProgressDrawable for placeholder when thumbnail is downloading
        private val circularProgressDrawable = CircularProgressDrawable(context)

        private val noCategoryInfo = "No category info"
        private val noNameInfo = "No name info"
        private val noEquipmentInfo = "No equipmentRows info"
        private val noMusclesInfo = "No muscleRows info"
        private val categoryLabel = "Category:"
        private val nameLabel = "Name:"
        private val equipmentLabel = "Equipment:"
        private val musclesLabel = "Muscles:"

        // TODO: Remove lint suppression
        @SuppressLint("SetTextI18n")
        fun bind(exercise: Exercise?, clickListener: (Exercise) -> Unit) {

            if (exercise != null) {

                containerView.setOnClickListener { clickListener(exercise) }

                // Load image
                if (exercise.thumbnailUrl.isNullOrBlank()) {
                    GlideApp.with(context)
                            .load(R.drawable.ic_fitness)
                            .into(exerciseThumbnailImageView)
                } else {
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()

                    GlideApp.with(context)
                            .load(exercise.thumbnailUrl)
                            .placeholder(circularProgressDrawable)
                            .into(exerciseThumbnailImageView)
                }

                if (exercise.category.isNullOrBlank()) {
                    exerciseCategoryTextView.text = noCategoryInfo
                } else {
                    val exerciseCategory = exercise.category
                    exerciseCategoryTextView.text = "$categoryLabel $exerciseCategory"
                }

                if (exercise.name.isNullOrBlank()) {
                    exerciseNameTextView.text = noNameInfo
                } else {
                    val exerciseName = exercise.name
                    exerciseNameTextView.text = "$nameLabel $exerciseName"
                }

                if (exercise.muscles.isNullOrBlank()) {
                    exerciseMusclesTextView.text = noMusclesInfo
                } else {
                    val exerciseMuscles = exercise.muscles
                    exerciseMusclesTextView.text = "$musclesLabel $exerciseMuscles"
                }

                if (exercise.equipment.isNullOrBlank()) {
                    exerciseEquipmentTextView.text = noEquipmentInfo
                } else {
                    val exerciseEquipment = exercise.equipment
                    exerciseEquipmentTextView.text = "$equipmentLabel $exerciseEquipment"
                }
            }

        }

    }
}