package com.dimi.superheroapp.framework.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class MainFragmentFactory @Inject constructor(
    private val requestManager: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            SearchFragment::class.java.name -> {
                SearchFragment(requestManager)
            }
            SuperHeroDetailsFragment::class.java.name -> {
                SuperHeroDetailsFragment(requestManager)
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}