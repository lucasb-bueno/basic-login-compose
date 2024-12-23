package com.lucasbueno.basiclogin.data.remote.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.lucasbueno.basiclogin.core.DatabaseService
import kotlinx.coroutines.tasks.await

class FirestoreDatabaseService : DatabaseService {

    private val firestore = FirebaseFirestore.getInstance()
    private val gson = Gson()

    override suspend fun <T> addDocument(collection: String, data: T): Result<String> {
        return try {
            if (data != null) {
                val document = firestore.collection(collection).add(data).await()
                Result.success(document.id)
            } else {
                Result.failure(exception = Throwable())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun <T> getDocument(collection: String, documentId: String, clazz: Class<T>): Result<T> {
        return try {
            val documentSnapshot = firestore.collection(collection).document(documentId).get().await()
            if (documentSnapshot.exists()) {
                val json = gson.toJson(documentSnapshot.data)
                val data = gson.fromJson(json, clazz)
                Result.success(data)
            } else {
                Result.failure(Exception("Document not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun <T> getDocuments(collection: String, clazz: Class<T>): Result<List<T>> {
        return try {
            val querySnapshot = firestore.collection(collection).get().await()
            val documents = querySnapshot.documents.map { it.toObject(clazz)!! }
            Result.success(documents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteDocument(collection: String, documentId: String): Result<Unit> {
        return try {
            firestore.collection(collection).document(documentId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
