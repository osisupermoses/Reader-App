package com.osisupermoses.capstoneappreaderapp.repository

import com.osisupermoses.capstoneappreaderapp.data.Resource
import com.osisupermoses.capstoneappreaderapp.model.Item
import com.osisupermoses.capstoneappreaderapp.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {

    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)

        } catch (ex: Exception) {
            return Resource.Error(message = ex.message.toString())
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId)

        } catch (ex: Exception) {
            return Resource.Error(message = "An error occurred ${ex.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)

    }
}