package com.dimi.superheroapp.presentation.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.presentation.common.gone
import com.dimi.superheroapp.presentation.common.visible
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
        val commitCallback = Runnable {
            interaction?.restoreListPosition()
        }
        differ.submitList(superHeroList, commitCallback)
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

        var animationFinished = true
        private val rotateAnimDuration = itemView.resources.getInteger(R.integer.rotate_anim_duration).toLong()
        companion object {

            const val ALPHA_VISIBLE = 1f
            const val ALPHA_INVISIBLE = 0f
        }

        fun bind(item: SuperHero) = with(itemView) {

            itemView.setOnClickListener {
                if( !animationFinished ) return@setOnClickListener
                animationFinished = false
                if (!item.expended) itemView.expandableLayout.alpha = ALPHA_VISIBLE
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
                interaction?.saveListPosition()
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

                    val typedValue = TypedValue()
                    resources.getValue(R.integer.rotate_anim_alpha_start_value, typedValue, true)
                    val alphaStartingPoint = typedValue.float

                    if (alpha == alphaStartingPoint && expandableLayout.visibility == View.GONE) {
                        expandableLayout.visible()
                        alpha = ALPHA_VISIBLE
                        doRotation = true
                        resId = R.anim.rotate_from_0_to_180
                    } else {
                        if (alpha == ALPHA_VISIBLE) {
                            doRotation = true
                            animate()
                                .alpha(ALPHA_INVISIBLE)
                                .setDuration(rotateAnimDuration)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        expandableLayout.gone()
                                        animationFinished = true
                                        alpha = ALPHA_VISIBLE
                                    }
                                })
                        }
                    }
                }
                AnimationUtils.loadAnimation(context, resId)

            } else {

                itemView.expandableLayout.apply {
                    if (alpha == ALPHA_VISIBLE) {
                        alpha = ALPHA_INVISIBLE
                        expandableLayout.visible()
                        animate()
                            .alpha(ALPHA_VISIBLE)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    animationFinished = true
                                }
                            })
                            .duration = rotateAnimDuration
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

        fun saveListPosition()
    }
}