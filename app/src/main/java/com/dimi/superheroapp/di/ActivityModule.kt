package com.dimi.superheroapp.di

import android.app.Activity
import androidx.fragment.app.FragmentManager
import com.dimi.superheroapp.presentation.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@Module
@InstallIn(ActivityComponent::class)
object ActivityModule  {

    @FlowPreview
    @ExperimentalCoroutinesApi
    @ActivityScoped
    @Provides
    fun provideSupportFragmentManager(activity: Activity): FragmentManager {
        return (activity as MainActivity).supportFragmentManager
    }
}