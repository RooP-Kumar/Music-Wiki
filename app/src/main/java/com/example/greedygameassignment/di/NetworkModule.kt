package com.example.greedygameassignment.di

import com.example.greedygameassignment.data.network.ApiService
import com.example.greedygameassignment.util.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesMoshi() : Moshi = Moshi
        .Builder()
        .run {
            add(KotlinJsonAdapterFactory())
            build()
        }


    @Provides
    @Singleton
    fun providesApiService(moshi: Moshi):ApiService=
        Retrofit.Builder()
            .run {
                baseUrl(BASE_URL)
                addConverterFactory(MoshiConverterFactory.create(moshi))
                build()
            }.create(ApiService::class.java)
}