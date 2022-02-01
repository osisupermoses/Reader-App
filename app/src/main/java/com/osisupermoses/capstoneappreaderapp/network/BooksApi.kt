package com.osisupermoses.capstoneappreaderapp.network

import com.osisupermoses.capstoneappreaderapp.model.Book
import com.osisupermoses.capstoneappreaderapp.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {

    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String): Book

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String): Item
}