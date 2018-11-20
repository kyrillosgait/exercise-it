package com.kyril.gymondotest.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.kyril.gymondotest.R
import com.kyril.gymondotest.model.Image
import com.kyril.gymondotest.ui.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.exercise_image_list_item.*

class ImageAdapter : ListAdapter<Image, ImageAdapter.ImageViewHolder>(ExerciseDiffCallback()) {

    class ExerciseDiffCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImageViewHolder(inflater.inflate(R.layout.exercise_image_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.stepNumber = position + 1
        holder.bind(getItem(position))
    }

    class ImageViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        private val context = containerView.context

        // Use Android's CircularProgressDrawable for placeholder when thumbnail is downloading
        private val circularProgressDrawable = CircularProgressDrawable(context)

        // Variables for incrementing step
        private val stepLabel = "Step"
        var stepNumber = 0

        @SuppressLint("SetTextI18n")
        fun bind(image: Image?) {

            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            GlideApp.with(context)
                    .load(image?.imageUrl)
                    .placeholder(circularProgressDrawable)
                    .into(exerciseImageImageView)

            exerciseImageStepTextView.text = "$stepLabel $stepNumber"

        }
    }
}