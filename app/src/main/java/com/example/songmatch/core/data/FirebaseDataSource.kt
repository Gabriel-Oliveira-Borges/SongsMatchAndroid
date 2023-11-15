package com.example.songmatch.core.data

import android.util.Log
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.domain.model.TrackArtist
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.framework.room.entities.UserEntity
import com.example.songmatch.core.mappers.UserEntityToUserMapper
import com.example.songmatch.core.models.ResultOf
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

object FirebaseCollections {
    val USER_COLLECTION = "users"
    val ROOM_COLLECTION = "rooms"
    val TRACK_COLLECTION = "tracks"
}

interface FirebaseDataSource {
    //Todo: Replace all these UserEntity, User and Track parameters for Firebase's ones
    suspend fun addUser(userEntity: UserEntity): ResultOf<Unit, Unit>
    suspend fun removeUser(user: User): ResultOf<Unit, Unit>
    suspend fun addUserTracks(tracks: List<Track>): ResultOf<Unit, Unit>
    suspend fun createRoom(user: User): ResultOf<Int, Unit>
    suspend fun listenToRoom(roomCode: String): Flow<ResultOf<FirebaseRoom, Unit>>

    data class FirebaseRoom(
        val usersToken: List<String>,
        val roomCode: Int,
        val playlistCreated: Boolean,
        val playlistLink: String?
    )

    data class FirebaseUser(
        val imageUri: String?,
        val name: String,
        val tracksUploaded: Boolean,
        val userToken: String,
        val userRoom: String?
    )

    data class FirebaseTrack(
        val id: String,
        val name: String,
        val popularity: Int,
        val timeRange: String?,
        val type: String,
        val uri: String,
        val userToken: String,
        val artists: List<TrackArtist>,
    ) {
        companion object {
            fun fromTrack(track: Track): FirebaseTrack = FirebaseTrack(
                id = track.id,
                name = track.name,
                popularity = track.popularity,
                timeRange = track.timeRange,
                type = track.type,
                uri = track.uri,
                userToken = track.userToken,
                artists = track.artists
            )
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseDataSourceImp @Inject constructor(
    private val userEntityToUserMapper: UserEntityToUserMapper,
) : FirebaseDataSource {
    private val firestore = Firebase.firestore

    //TODO: Change dispatchers of all Datasources

    override suspend fun addUser(userEntity: UserEntity): ResultOf<Unit, Unit> {
        val user = userEntityToUserMapper.map(userEntity)

        val firebaseUser = FirebaseDataSource.FirebaseUser(
            name = user.name!!,
            imageUri = user.spotifyUser.imageUri,
            tracksUploaded = false,
            userToken = user.spotifyUser.token,
            userRoom = null
        )

        return this.addDocument(
            collection = FirebaseCollections.USER_COLLECTION,
            document = user.spotifyUser.token,
            data = firebaseUser
        )
    }

    override suspend fun addUserTracks(tracks: List<Track>): ResultOf<Unit, Unit> {
        return this.batchTask { batch ->
            tracks.forEach { track ->
                val documentReference = firestore.collection(FirebaseCollections.TRACK_COLLECTION).document()
                val firebaseTrack = FirebaseDataSource.FirebaseTrack.fromTrack(track)
                batch.set(documentReference, firebaseTrack)
            }

            tracks.firstNotNullOfOrNull { it.userToken }?.let { userToken ->
                val userReference = firestore.collection(FirebaseCollections.USER_COLLECTION).document(userToken)
                batch.set(userReference, hashMapOf("tracksUploaded" to true), SetOptions.merge())
            }
        }
    }

    override suspend fun createRoom(user: User): ResultOf<Int, Unit> {
        val roomCode = (10000..99999).random()
        return this.addDocument(
            collection = FirebaseCollections.ROOM_COLLECTION,
            document = roomCode.toString(),
            data = FirebaseDataSource.FirebaseRoom(
                usersToken = listOf(user.spotifyUser.token),
                roomCode = roomCode,
                playlistCreated = false,
                playlistLink = null
            )
        ).mapSuccess { roomCode }.onSuccess {
            firestore
                .collection(FirebaseCollections.USER_COLLECTION)
                .document(user.spotifyUser.token)
                .set(hashMapOf("userRoom" to roomCode), SetOptions.merge())
        }
    }

    override suspend fun listenToRoom(roomCode: String): Flow<ResultOf<FirebaseDataSource.FirebaseRoom, Unit>> {
        return callbackFlow {
            val subscription = firestore
                .collection(FirebaseCollections.ROOM_COLLECTION)
                .document(roomCode)
                .addSnapshotListener { snapshot, e ->
                    if (e != null || snapshot == null || !snapshot.exists()) {
                        offer(ResultOf.Error(Unit))
                    } else {
                        val data = snapshot.data!!

                        val room = FirebaseDataSource.FirebaseRoom(
                            usersToken = data["usersToken"] as List<String>,
                            roomCode = (data["roomCode"] as Long).toInt(),
                            playlistCreated = data["playlistCreated"] as Boolean,
                            playlistLink = data["playlistLink"] as String?
                        )
                        offer(ResultOf.Success(room))
                    }
                }

            awaitClose { subscription.remove() }
        }
    }

    override suspend fun removeUser(user: User): ResultOf<Unit, Unit> {
        return this.removeDocument(
            collection = FirebaseCollections.USER_COLLECTION,
            document = user.spotifyUser.token
        ).onSuccess {
            this.removeUserTracks(user)
        }
    }

    private suspend fun removeUserTracks(user: User): ResultOf<Unit, Unit> {
        val userTracksDocuments = firestore
            .collection(FirebaseCollections.TRACK_COLLECTION)
            .whereEqualTo("userToken", user.spotifyUser.token)
            .get()
            .await()

        return this.batchTask { batch ->
            for (document in userTracksDocuments) {
                batch.delete(document.reference)
            }
        }
    }

    private suspend inline fun batchTask(crossinline task: ((WriteBatch) -> Unit)): ResultOf<Unit, Unit> {
        lateinit var result: ResultOf<Unit, Unit>
        firestore.runBatch {
            task(it)
        }
        .addOnSuccessListener {
            result = ResultOf.Success(Unit)
        }
        .addOnFailureListener {
            Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
            result = ResultOf.Error(Unit)
        }.await()

        return result
    }

    private suspend inline fun removeDocument(collection: String, document: String): ResultOf<Unit, Unit> {
        lateinit var result: ResultOf<Unit, Unit>
        firestore.collection( collection)
            .document(document)
            .delete()
            .addOnSuccessListener {
                result = ResultOf.Success(Unit)
            }
            .addOnFailureListener {
                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
                result = ResultOf.Error(Unit)
            }
            .await()
        return result
    }

    private suspend inline fun addDocument(collection: String, document: String, data: Any): ResultOf<Unit, Unit> {
        lateinit var result: ResultOf<Unit, Unit>

        firestore
            .collection(collection)
            .document(document)
            .set(data)
            .addOnSuccessListener {
                result = ResultOf.Success(Unit)
            }
            .addOnFailureListener {
                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
                result = ResultOf.Error(Unit)
            }
            .await()
        return result
    }
}