package com.dimi.superheroapp.presentation.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.StateMessageCallback
import com.dimi.superheroapp.presentation.common.fadeIn
import com.dimi.superheroapp.presentation.main.viewmodel.getClickedSuperHero
import kotlinx.android.synthetic.main.fragment_details_superhero.*
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@FlowPreview
class SuperHeroDetailsFragment
constructor(
    private val requestManager: RequestManager
) : BaseFragment(R.layout.fragment_details_superhero) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if( !uiController.isDeviceTabletInLandscapeMode() ) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(null)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
            setHasOptionsMenu(true)

            uiController.expandAppBar()
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {
                viewState.superHeroDetail?.let { superHero ->
                    setSuperHeroProperties(superHero)
                    details_fragment_container.fadeIn()
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->
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

    private fun setSuperHeroProperties(superHero: SuperHero) {

        requestManager.load(superHero.image).into(image)

        name.text = superHero.name
        full_name.text = superHero.fullName

        setupBiographyProperties( superHero )

        setupDurabilityProperties( superHero.durability )
        setupSpeedProperties( superHero.speed )
        setupCombatProperties( superHero.combat )
        setupIntelligenceProperties( superHero.intelligence )
        setupStrengthProperties( superHero.strength )
        setupPowerProperties( superHero.power )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if( !uiController.isDeviceTabletInLandscapeMode() ) {
            inflater.inflate(R.menu.send_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if( !uiController.isDeviceTabletInLandscapeMode() ) {
            when( item.itemId ) {
                android.R.id.home -> {
                    findNavController().navigate(R.id.action_superHeroDetailsFragment_to_searchFragment)
                    return true
                }
                R.id.action_send -> {
                    sendSuperHeroAsMessage()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendSuperHeroAsMessage() {
        val superHero = viewModel.getClickedSuperHero()
        superHero?.let {
            uiController.sendSuperHeroAsMessage(superHero)
        }?: viewModel.createSuperHeroIsNotSelectedMessage()
    }
}