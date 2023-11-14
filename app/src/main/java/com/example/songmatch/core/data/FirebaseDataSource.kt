package com.example.songmatch.core.data

import android.util.Log
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.framework.room.entities.UserEntity
import com.example.songmatch.core.mappers.UserEntityToUserMapper
import com.example.songmatch.core.models.ResultOf
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

interface FirebaseDataSource {
    suspend fun addUser(user: UserEntity): ResultOf<Unit, Unit>

    suspend fun removeUser(user: User): ResultOf<Unit, Unit>
}

class FirebaseDataSourceImp @Inject constructor(
    private val userEntityToUserMapper: UserEntityToUserMapper,
) : FirebaseDataSource {
    private val firestore = Firebase.firestore

    override suspend fun addUser(userEntity: UserEntity): ResultOf<Unit, Unit> {
//        return ResultOf.Success(Unit)
        lateinit var result: ResultOf<Unit, Unit>
        val user = userEntityToUserMapper.map(userEntity)

        Firebase.firestore

        val firebaseUser = hashMapOf(
            "name" to user.name,
            "imageUri" to user.spotifyUser.imageUri
        )

//        this.addDocument(collection = "users", document = user.spotifyUser.token, data = firebaseUser)
        firestore.collection("users")
            .document(user.spotifyUser.token)
            .set(firebaseUser)
            .addOnSuccessListener {
                result = ResultOf.Success(Unit)
            }
            .addOnFailureListener {
                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
                result = ResultOf.Error(Unit)
            }
//            .await()
        return ResultOf.Success(Unit)
    }

    override suspend fun removeUser(user: User): ResultOf<Unit, Unit> {
        return ResultOf.Success(Unit)
//        lateinit var result: ResultOf<Unit, Unit>
//        firestore.collection( "users")
//            .document(user.spotifyUser.token)
//            .delete()
//            .addOnSuccessListener {
//                result = ResultOf.Success(Unit)
//            }
//            .addOnFailureListener {
//                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
//                result = ResultOf.Error(Unit)
//            }
//            .await()
//        //TODO: DELETE USER'S TRACKS
//        return result
    }

//    private suspend inline fun addDocument(collection: String, document: String, data: Any): ResultOf<Unit, Unit> {
//        lateinit var result: ResultOf<Unit, Unit>
//
//        firestore
//            .collection(collection)
//            .document(document)
//            .set(data)
//            .addOnSuccessListener {
//                result = ResultOf.Success(Unit)
//            }
//            .addOnFailureListener {
//                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
//                result = ResultOf.Error(Unit)
//            }
//            .await()
//        return result
//    }
}