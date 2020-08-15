package com.dimi.superheroapp.di

import android.content.Context
import android.content.SharedPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.data.network.abstraction.NetworkDataSource
import com.dimi.superheroapp.business.data.network.implementation.NetworkDataSourceImpl
import com.dimi.superheroapp.framework.datasource.PreferenceKeys
import com.dimi.superheroapp.framework.datasource.network.abstraction.MainApiService
import com.dimi.superheroapp.framework.datasource.network.api.MainApi
import com.dimi.superheroapp.framework.datasource.network.implementation.MainApiServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPrefsEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context
            .getSharedPreferences(
                PreferenceKeys.APP_PREFERENCE,
                Context.MODE_PRIVATE
            )
    }

    @Singleton
    @Provides
    fun provideMainApiService(
        mainApi: MainApi
    ) : MainApiService {
        return MainApiServiceImpl( mainApi )
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(
        mainApiService: MainApiService
    ) : NetworkDataSource {
        return NetworkDataSourceImpl( mainApiService )
    }


    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .circleCropTransform()
            .error(R.drawable.default_image)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
    }
}