package com.lucasbueno.basiclogin.core

interface DatabaseService {
    suspend fun <T> addDocument(collection: String, data: T): Result<String>
    suspend fun <T> getDocuments(collection: String, clazz: Class<T>): Result<List<T>>
    suspend fun deleteDocument(collection: String, documentId: String): Result<Unit>
}
