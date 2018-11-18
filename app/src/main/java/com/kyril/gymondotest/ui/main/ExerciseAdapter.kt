package com.kyril.gymondotest.ui.main

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

        fun bind(exercise: Exercise?, clickListener: (Exercise) -> Unit) {

            containerView.setOnClickListener {
                if (exercise != null) {
                    clickListener(exercise)
                }
            }

            if (exercise != null) {

                // TODO: Placeholder while image is downloading.
                if (exercise.thumbnailUrl != null) {

                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()

                    GlideApp.with(context)
                            .load(exercise.thumbnailUrl)
                            .placeholder(circularProgressDrawable)
                            .into(exerciseThumbnailImageView)

                } else {
                    GlideApp.with(context)
                            .load(R.drawable.ic_fitness)
                            .into(exerciseThumbnailImageView)
                }

                exerciseCategoryTextView?.text = "Category: " + exercise.category
                if (exercise.name != "") {
                    exerciseNameTextView?.text = "Name: " + exercise.name
                } else {
                    exerciseNameTextView?.text = "No name info"
                }

                if (exercise.muscles != null) {
                    exerciseMusclesTextView?.text = "Muscles: " + exercise.muscles
                } else {
                    exerciseMusclesTextView?.text = "No muscles info"
                }

                if (exercise.equipment != null) {
                    exerciseEquipmentTextView?.text = "Equipment: " + exercise.equipment
                } else {
                    exerciseEquipmentTextView?.text = "No equipment info"
                }
            }

        }

    }
}