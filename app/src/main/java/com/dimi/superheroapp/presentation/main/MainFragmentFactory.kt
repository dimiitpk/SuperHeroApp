package com.dimi.superheroapp.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton


@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class MainFragmentFactory @Inject constructor(
    private val requestManager: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            SearchFragment::class.java.name -> {
                SearchFragment(
                    requestManager
                )
            }
            SuperHeroDetailsFragment::class.java.name -> {
                SuperHeroDetailsFragment(
                    requestManager
                )
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}