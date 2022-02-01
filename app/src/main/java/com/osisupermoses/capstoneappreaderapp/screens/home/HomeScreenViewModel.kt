package com.osisupermoses.capstoneappreaderapp.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.osisupermoses.capstoneappreaderapp.data.DataOrException
import com.osisupermoses.capstoneappreaderapp.model.MBook
import com.osisupermoses.capstoneappreaderapp.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: FireRepository): ViewModel() {

        val data: MutableState<DataOrException<List<MBook>, Boolean, Exception>>
        = mutableStateOf(DataOrException(emptyList(), true, Exception("")))

    init {
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            try {
//            data.value.loading = true
                data.value = repository.getAllBooksFromDatabase()
                if (!data.value.data.isNullOrEmpty()) data.value.loading = false

            } catch (ex: FirebaseFirestoreException) {
                data.value.e = ex
                print("An unknown error occurred! while getting the file")
                Log.d("Error", "There is an error getting files from database: ${ex.message.toString()}")
            }
        }
//        Log.d("GET", "Get All Books From Database: ${data.value.data?.toList().toString()}")
    }
}