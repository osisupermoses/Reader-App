package com.osisupermoses.capstoneappreaderapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.osisupermoses.capstoneappreaderapp.DaggerReaderApplication_HiltComponents_SingletonC.builder
import com.osisupermoses.capstoneappreaderapp.network.BooksApi
import com.osisupermoses.capstoneappreaderapp.repository.FireRepository
import com.osisupermoses.capstoneappreaderapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFireBookRepository()
    = FireRepository(queryBook = FirebaseFirestore.getInstance()
        .collection("books"))

    @Singleton
    @Provides
    fun provideBookApi(): BooksApi {
        return Retrofit.Builder()
            .baseUrl(Constants.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }
}