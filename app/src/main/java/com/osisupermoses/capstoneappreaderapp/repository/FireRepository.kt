package com.osisupermoses.capstoneappreaderapp.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.osisupermoses.capstoneappreaderapp.data.DataOrException
import com.osisupermoses.capstoneappreaderapp.model.MBook
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FireRepository @Inject constructor(private val queryBook: Query) {

    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        } catch (ex: FirebaseFirestoreException) {
            dataOrException.e = ex
        }
        return dataOrException
    }
}