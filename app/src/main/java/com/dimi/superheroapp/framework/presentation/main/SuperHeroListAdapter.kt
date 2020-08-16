package com.dimi.superheroapp.framework.presentation.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.domain.model.SuperHero
import kotlinx.android.synthetic.main.layout_superhero_list_item.view.*
import java.lang.Runnable

class SuperHeroListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _diffCallback = object : DiffUtil.ItemCallback<SuperHero>() {

        override fun areItemsTheSame(oldItem: SuperHero, newItem: SuperHero): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SuperHero, newItem: SuperHero): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            SuperHeroRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(_diffCallback).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return SuperHeroViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_superhero_list_item,
                parent,
                false
            ),
            interaction = interaction,
            requestManager = requestManager
        )
    }

    internal inner class SuperHeroRecyclerChangeCallback(
        private val adapter: SuperHeroListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<SuperHero>
    ) {
        for (superHero in list) {
            requestManager
                .load(superHero.image)
                .circleCrop()
                .preload()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].id
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun submitList(
        superHeroList: List<SuperHero>?
    ) {
        val newList = superHeroList?.toMutableList()

        val commitCallback = Runnable {
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SuperHeroViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    class SuperHeroViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: SuperHero) = with(itemView) {

            itemView.setOnClickListener {
                if (!item.expended) itemView.expandableLayout.alpha = 1f
                checkRotateAnimation(itemView, context, item)
                item.expended = !item.expended
            }
            checkRotateAnimation(itemView, context, item)

            requestManager
                .load(item.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)

            itemView.name.text = item.name
            itemView.full_name.text = item.fullName

            itemView.aliases.text = item.getValidAliases()

            itemView.button_more_info.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }
        }


        private fun checkRotateAnimation(itemView: View, context: Context?, item: SuperHero) {

            val expended = item.expended
            var doRotation = false
            val rotation: Animation
            rotation = if (expended) {

                var resId = R.anim.rotate_from_180_to_0
                itemView.expandableLayout.apply {

                    if (alpha == 0.5f && expandableLayout.visibility == View.GONE) {
                        expandableLayout.visibility = View.VISIBLE
                        alpha = 1f
                        doRotation = true
                        resId = R.anim.rotate_from_0_to_180
                    } else {
                        if (alpha == 1f) {
                            doRotation = true
                            animate()
                                .alpha(0f)
                                .setDuration(500)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        expandableLayout.visibility = View.GONE
                                        alpha = 1f
                                    }
                                })
                        }
                    }
                }
                AnimationUtils.loadAnimation(context, resId)

            } else {

                itemView.expandableLayout.apply {
                    if (alpha == 1f) {
                        alpha = 0f
                        expandableLayout.visibility = View.VISIBLE
                        animate().alpha(1f).setListener(null).duration = 500
                        doRotation = true
                    }
                }
                AnimationUtils.loadAnimation(context, R.anim.rotate_from_0_to_180)
            }
            rotation.fillAfter = true
            if (doRotation) itemView.arrow_rotation.startAnimation(rotation)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: SuperHero)

        fun restoreListPosition()
    }
}