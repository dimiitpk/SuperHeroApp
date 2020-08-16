package com.dimi.superheroapp.framework.presentation.main

import com.dimi.superheroapp.business.domain.model.SuperHero
import kotlinx.android.synthetic.main.fragment_details_superhero.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.StringBuilder

@FlowPreview
@ExperimentalCoroutinesApi
fun SuperHeroDetailsFragment.setupDurabilityProperties( durability: Int ) {
    durability_value.text = durability.toString()
    progress_bar_durability.apply {
        setProgressWithAnimation(
            durability.toFloat(),
            calculateProgressBarDurability(durability)
        )
    }
}
@FlowPreview
@ExperimentalCoroutinesApi
fun SuperHeroDetailsFragment.setupIntelligenceProperties( intelligence: Int ) {
    intelligence_value.text = intelligence.toString()
    progress_bar_intelligence.apply {
        setProgressWithAnimation(
            intelligence.toFloat(),
            calculateProgressBarDurability(intelligence)
        )
    }

}

@FlowPreview
@ExperimentalCoroutinesApi
fun SuperHeroDetailsFragment.setupCombatProperties( combat: Int ) {
    combat_value.text = combat.toString()
    progress_bar_combat.apply {
        setProgressWithAnimation(
            combat.toFloat(),
            calculateProgressBarDurability(combat)
        )
    }

}

@FlowPreview
@ExperimentalCoroutinesApi
fun SuperHeroDetailsFragment.setupSpeedProperties( speed: Int ) {
    speed_value.text = speed.toString()
    progress_bar_speed.apply {
        setProgressWithAnimation(
            speed.toFloat(),
            calculateProgressBarDurability(speed)
        )
    }

}

@FlowPreview
@ExperimentalCoroutinesApi
fun SuperHeroDetailsFragment.setupStrengthProperties( strength: Int ) {
    strength_value.text = strength.toString()
    progress_bar_strength.apply {
        setProgressWithAnimation(
            strength.toFloat(),
            calculateProgressBarDurability(strength)
        )
    }

}

@FlowPreview
@ExperimentalCoroutinesApi
fun SuperHeroDetailsFragment.setupPowerProperties( power: Int ) {
    power_value.text = power.toString()
    progress_bar_power.apply {
        setProgressWithAnimation(
            power.toFloat(),
            calculateProgressBarDurability(power)
        )
    }

}

@FlowPreview
@ExperimentalCoroutinesApi
fun SuperHeroDetailsFragment.setupBiographyProperties(superHero: SuperHero) {
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
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun calculateProgressBarDurability(value: Int): Long {
    return ((value * 100) / 3).toLong()
}

