package com.dimi.superheroapp.framework.presentation.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.StateMessageCallback
import kotlinx.android.synthetic.main.fragment_details_superhero.*
import kotlinx.coroutines.*
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
@FlowPreview
class SuperHeroDetailsFragment
constructor(
    private val requestManager: RequestManager
) : BaseFragment(R.layout.fragment_details_superhero) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(null)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        uiController.expandAppBar()
        subscribeObservers()
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.superHeroDetail?.let { superHero ->
                    setSuperHeroProperties(superHero)
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { message ->
                uiController.onResponseReceived(
                    response = message.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    private fun calculateProgressBarDurability(value: Int): Long {
        return ((value * 100) / 3).toLong()
    }

    private fun setSuperHeroProperties(superHero: SuperHero) {

        requestManager.load(superHero.image).into(image)

        name.text = superHero.name
        full_name.text = superHero.fullName

        val sbNames = StringBuilder()
        sbNames.append("Publisher:")
        sbNames.append("\n")
        sbNames.append("Place of birth:")
        sbNames.append("\n")
        sbNames.append("First appearance:")
        sbNames.append("\n")
        sbNames.append("Aliases:")

        val sbValues = StringBuilder()
        sbValues.append(superHero.getValidPublisher())
        sbValues.append("\n")
        sbValues.append(superHero.getValidPlaceOfBirth())
        sbValues.append("\n")
        sbValues.append(superHero.getValidFirstAppearance())
        sbValues.append("\n")
        sbValues.append(superHero.getValidAliases())

        biography_name.text = sbNames
        biography_value.text = sbValues


        durability_value.text = superHero.durability.toString()
        speed_value.text = superHero.speed.toString()
        intelligence_value.text = superHero.intelligence.toString()
        combat_value.text = superHero.combat.toString()
        power_value.text = superHero.power.toString()
        strength_value.text = superHero.strength.toString()
        progress_bar_durability.apply {
            setProgressWithAnimation(
                superHero.durability.toFloat(),
                calculateProgressBarDurability(superHero.durability)
            )
        }
        progress_bar_combat.apply {
            setProgressWithAnimation(
                superHero.combat.toFloat(),
                calculateProgressBarDurability(superHero.combat)
            )
        }
        progress_bar_intelligence.apply {
            setProgressWithAnimation(
                superHero.intelligence.toFloat(),
                calculateProgressBarDurability(superHero.intelligence)
            )
        }
        progress_bar_power.apply {
            setProgressWithAnimation(
                superHero.power.toFloat(),
                calculateProgressBarDurability(superHero.power)
            )
        }
        progress_bar_speed.apply {
            setProgressWithAnimation(
                superHero.speed.toFloat(),
                calculateProgressBarDurability(superHero.speed)
            )
        }
        progress_bar_strength.apply {
            setProgressWithAnimation(
                superHero.strength.toFloat(),
                calculateProgressBarDurability(superHero.strength)
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigate(R.id.action_superHeroDetailsFragment_to_searchFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}