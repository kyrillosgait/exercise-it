package com.kyril.gymondotest.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kyril.gymondotest.R
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.ui.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.exercise_list_item.*

/**
 * Using ListAdapter instead of RecyclerViewAdapter, added in support library 27.1.0
 */

class ExerciseAdapter : ListAdapter<Exercise, ExerciseAdapter.ExerciseViewHolder>(ExerciseDiffCallback()) {

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
        holder.bind(getItem(position))
    }

    class ExerciseViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private val context = containerView.context!!

        fun bind(exercise: Exercise?) {

            if (exercise?.imageUrls!!.isNotEmpty()) {

                GlideApp.with(context)
                    .load(exercise.imageUrls!!.substringBefore(","))
                    .into(exerciseThumbnailImageView)
            } else {
                GlideApp.with(context)
                    .load(R.drawable.ic_fitness)
                    .into(exerciseThumbnailImageView)
            }


            exerciseCategoryTextView?.text = "Category: " + exercise?.category
            if (exercise?.name != "") {
                exerciseNameTextView?.text = "Name: " + exercise?.name
            } else {
                exerciseNameTextView?.text = "No name info"
            }


            if (exercise?.muscles != null) {
                exerciseMusclesTextView?.text = "Muscles: " + exercise?.muscles
            } else {
                exerciseMusclesTextView?.text = "No muscles info"
            }

            if (exercise?.equipment != null) {
                exerciseEquipmentTextView?.text = "Equipment: " + exercise?.equipment
            } else {
                exerciseEquipmentTextView?.text = "No equipment info"
            }

        }

    }
}