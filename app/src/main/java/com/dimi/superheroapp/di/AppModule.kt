package com.dimi.superheroapp.di

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.data.cache.abstraction.CacheDataSource
import com.dimi.superheroapp.business.data.cache.implementation.CacheDataSourceImpl
import com.dimi.superheroapp.business.data.network.abstraction.NetworkDataSource
import com.dimi.superheroapp.business.data.network.implementation.NetworkDataSourceImpl
import com.dimi.superheroapp.framework.datasource.PreferenceKeys
import com.dimi.superheroapp.framework.datasource.cache.abstraction.MainDaoService
import com.dimi.superheroapp.framework.datasource.cache.database.MainDao
import com.dimi.superheroapp.framework.datasource.cache.implementation.MainDaoServiceImpl
import com.dimi.superheroapp.framework.datasource.cache.mappers.CacheMapper
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
    fun provideDaoService(
        mainDao: MainDao,
        cacheMapper: CacheMapper
    ): MainDaoService {
        return MainDaoServiceImpl(mainDao, cacheMapper)
    }

    @Singleton
    @Provides
    fun provideCacheDataSource(
        mainDaoService: MainDaoService
    ): CacheDataSource {
        return CacheDataSourceImpl(mainDaoService)
    }

    @Singleton
    @Provides
    fun provideMainApiService(
        mainApi: MainApi
    ): MainApiService {
        return MainApiServiceImpl(mainApi)
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(
        mainApiService: MainApiService
    ): NetworkDataSource {
        return NetworkDataSourceImpl(mainApiService)
    }

    @Singleton
    @Provides
    fun providePlaceHolder(
        @ApplicationContext context: Context
    ): Bitmap {
        return BitmapFactory.decodeResource(context.resources, R.drawable.default_image)
    }

    @Singleton
    @Provides
    fun provideRoundedBitmapDrawable(
        @ApplicationContext context: Context,
        placeholder: Bitmap
    ): RoundedBitmapDrawable {
        val circularBitmap = RoundedBitmapDrawableFactory.create(context.resources, placeholder)
        circularBitmap.isCircular = true
        return circularBitmap
    }

    @Singleton
    @Provides
    fun provideRequestOptions(
        circularBitmapDrawable: RoundedBitmapDrawable
    ): RequestOptions {

        return RequestOptions
            .circleCropTransform()
            .error(circularBitmapDrawable)
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