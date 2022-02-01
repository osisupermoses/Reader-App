package com.osisupermoses.capstoneappreaderapp.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osisupermoses.capstoneappreaderapp.components.showToast
import com.osisupermoses.capstoneappreaderapp.data.Resource
import com.osisupermoses.capstoneappreaderapp.model.Item
import com.osisupermoses.capstoneappreaderapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class BooksSearchViewModel @Inject constructor(private val repository: BookRepository)
    : ViewModel() {

    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBook("flutter")
    }

    fun searchBook(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                return@launch
            }
            try {
                when(val response = repository.getBooks(query)) {
                    is Resource.Success -> {
                        list = response.data!!
                        if (list.isNotEmpty()) isLoading = false
                    }
                    is Resource.Error -> {
                        Log.e("Network", "SearchBooks: Failed getting books")
                    }
                    else -> { isLoading = false }
                }
            } catch (ex: Exception) {
                Log.d("Error", "searchBooks: ${ex.message.toString()}")
            }
        }
    }

}