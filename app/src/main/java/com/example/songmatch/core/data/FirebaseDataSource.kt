package com.example.songmatch.core.data

import android.util.Log
import com.example.songmatch.core.data.FirebaseCollections.PLAYLISTS_COLLECTION
import com.example.songmatch.core.data.FirebaseCollections.ROOM_COLLECTION
import com.example.songmatch.core.data.FirebaseCollections.TRACK_COLLECTION
import com.example.songmatch.core.data.FirebaseCollections.USER_COLLECTION
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.domain.model.TrackArtist
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.framework.room.entities.UserEntity
import com.example.songmatch.core.mappers.UserEntityToUserMapper
import com.example.songmatch.core.models.ResultOf
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

object FirebaseCollections {
    val USER_COLLECTION = "users"
    val ROOM_COLLECTION = "rooms"
    val TRACK_COLLECTION = "tracks"
    val PLAYLISTS_COLLECTION = "playlists"
}

interface FirebaseDataSource {
    //Todo: Replace all these UserEntity, User and Track parameters for Firebase's ones
    suspend fun addUser(userEntity: UserEntity): ResultOf<Unit, Unit>
    suspend fun removeUser(user: User): ResultOf<Unit, Unit>
    suspend fun addUserTracks(tracks: List<Track>): ResultOf<Unit, Unit>
    suspend fun createRoom(user: User): ResultOf<Int, Unit>
    suspend fun listenToRoom(roomCode: String): Flow<ResultOf<FirebaseRoom, Unit>>
    suspend fun getRoom(roomCode: String): ResultOf<FirebaseRoom?, Unit>
    suspend fun joinRoom(roomCode: String, userToken: String): ResultOf<Unit, Unit>
    suspend fun getUserTracks(userToken: String): ResultOf<List<FirebaseTrack>, Unit>

    suspend fun createPlaylist(roomCode: String, tracksUri: List<String>): ResultOf<Unit, Unit>
    suspend fun getPlaylist(roomCode: String): ResultOf<FirebasePlaylist, Unit>
    suspend fun addSpotifyPlaylistUri(roomCode: String, uri: String): ResultOf<Unit, Unit>

    suspend fun getTrackDetails(trackUri: String): ResultOf<FirebaseTrack, Unit>
    suspend fun leaveRoom(roomCode: String, userToken: String): ResultOf<Unit, Unit>

    data class FirebasePlaylist(
        val roomCode: String,
        val tracksUri: List<String>
    )

    data class FirebaseRoom(
        val usersToken: List<String>,
        val roomCode: String,
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
        val albumImageUri: String?,

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
                artists = track.artists,
                albumImageUri = track.albumImageUri
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

    override suspend fun addSpotifyPlaylistUri(
        roomCode: String,
        uri: String
    ): ResultOf<Unit, Unit> {
        lateinit var result: ResultOf<Unit, Unit>

        firestore
            .collection(ROOM_COLLECTION)
            .document(roomCode)
            .set(hashMapOf("playlistLink" to uri), SetOptions.merge())
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
                roomCode = roomCode.toString(),
                playlistCreated = false,
                playlistLink = null
            )
        ).mapSuccess { roomCode }.onSuccess {
            this.addUserRoomToUser(roomCode = roomCode.toString(), userToken = user.spotifyUser.token)
        }
    }

    override suspend fun createPlaylist(
        roomCode: String,
        tracksUri: List<String>
    ): ResultOf<Unit, Unit> {
        val data = FirebaseDataSource.FirebasePlaylist(
            roomCode = roomCode,
            tracksUri = tracksUri
        )
        return this.addDocument(PLAYLISTS_COLLECTION, roomCode, data).onSuccess {
            firestore
                .collection(FirebaseCollections.ROOM_COLLECTION)
                .document(roomCode)
                .set(hashMapOf("playlistCreated" to true), SetOptions.merge())
        }
    }

    override suspend fun getPlaylist(roomCode: String): ResultOf<FirebaseDataSource.FirebasePlaylist, Unit> {
        return this.getDocument(PLAYLISTS_COLLECTION, roomCode).mapSuccess {
            FirebaseDataSource.FirebasePlaylist(
                roomCode = it?.get("roomCode") as String,
                tracksUri = it["tracksUri"] as List<String>
            )
        }
    }

    override suspend fun getTrackDetails(trackUri: String): ResultOf<FirebaseDataSource.FirebaseTrack, Unit> {
        lateinit var result: ResultOf<FirebaseDataSource.FirebaseTrack, Unit>
        firestore
            .collection(TRACK_COLLECTION)
            .whereEqualTo("uri", trackUri)
            .get()
            .addOnSuccessListener {

                val document = it.firstOrNull()
                result = document?.data?.let {
                    ResultOf.Success(
                        FirebaseDataSource.FirebaseTrack(
                            id = it["id"] as String,
                            name = it["name"] as String,
                            popularity = (it["popularity"] as Long).toInt(),
                            timeRange = it["timeRange"] as String?,
                            type = it["type"] as String,
                            uri = it["uri"] as String,
                            userToken = it["userToken"] as String,
                            artists = it["artists"] as List<TrackArtist>,
                            albumImageUri = it["albumImageUri"] as String?
                        )
                    )
                } ?: ResultOf.Error(Unit)
            }
            .addOnFailureListener {
                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
                result = ResultOf.Error(Unit)
            }
            .await()
        return result
    }

    override suspend fun getRoom(roomCode: String): ResultOf<FirebaseDataSource.FirebaseRoom?, Unit> {
        return  this.getDocument(FirebaseCollections.ROOM_COLLECTION, roomCode).mapSuccess {it ->
            it?.let { data ->
                FirebaseDataSource.FirebaseRoom(
                    usersToken = data["usersToken"] as List<String>,
                    roomCode = data["roomCode"] as String,
                    playlistCreated = data["playlistCreated"] as Boolean,
                    playlistLink = data["playlistLink"] as String?
                )
            }
        }
    }
    override suspend fun getUserTracks(userToken: String): ResultOf<List<FirebaseDataSource.FirebaseTrack>, Unit> {
        lateinit var result: ResultOf<MutableList<FirebaseDataSource.FirebaseTrack>, Unit>
        firestore
            .collection(FirebaseCollections.TRACK_COLLECTION)
            .whereEqualTo("userToken", userToken)
            .get()
            .addOnSuccessListener {
                val tracks = mutableListOf<FirebaseDataSource.FirebaseTrack>()

                for (document in it.documents) {
                    val data = document.data
                    tracks.add(
                        FirebaseDataSource.FirebaseTrack(
                            id = data?.get("id") as String,
                            name = data?.get("name") as String,
                            popularity = (data?.get("popularity") as Long).toInt(),
                            timeRange = data?.get("timeRange") as String?,
                            type = data?.get("type") as String,
                            uri = data?.get("uri") as String,
                            userToken = data?.get("userToken") as String,
                            artists = data?.get("artists") as List<TrackArtist>,
                            albumImageUri = data?.get("albumImageUri") as String?
                        )
                    )
                }
                result = ResultOf.Success(tracks)
            }
            .addOnFailureListener {
                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
                result = ResultOf.Error(Unit)
            }
            .await()
        return result
    }

    override suspend fun joinRoom(roomCode: String, userToken: String): ResultOf<Unit, Unit> {
        lateinit var result: ResultOf<Unit, Unit>
        firestore
            .collection(FirebaseCollections.ROOM_COLLECTION)
            .document(roomCode)
            .update("usersToken", FieldValue.arrayUnion(userToken))
            .addOnSuccessListener {
                result = ResultOf.Success(Unit)
            }
            .addOnFailureListener {
                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
                result = ResultOf.Error(Unit)
            }
            .await()

        if (result is ResultOf.Success) {
            this.addUserRoomToUser(roomCode = roomCode, userToken = userToken)
        }

        return result
    }

    override suspend fun leaveRoom(roomCode: String, userToken: String): ResultOf<Unit, Unit> {
        lateinit var result: ResultOf<Unit, Unit>
        firestore.collection(ROOM_COLLECTION)
            .document(roomCode)
            .update("usersToken", FieldValue.arrayRemove(userToken))
            .addOnSuccessListener {
                result = ResultOf.Success(Unit)
            }
            .addOnFailureListener {
                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
                result = ResultOf.Error(Unit)
            }
            .await()

        firestore.collection(USER_COLLECTION)
            .document(userToken)
            .set(hashMapOf("userRoom" to null), SetOptions.merge())
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
                            usersToken = data?.get("usersToken") as List<String>,
                            roomCode = data?.get("roomCode") as String,
                            playlistCreated = data?.get("playlistCreated") as Boolean,
                            playlistLink = data?.get("playlistLink") as String?
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

    private suspend fun addUserRoomToUser(userToken: String, roomCode: String): ResultOf<Unit, Unit> {
        lateinit var result: ResultOf<Unit, Unit>
        firestore
            .collection(FirebaseCollections.USER_COLLECTION)
            .document(userToken)
            .set(hashMapOf("userRoom" to roomCode), SetOptions.merge())
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

    private suspend inline fun getDocument(collection: String, document: String): ResultOf<Map<String, Any>?, Unit> {
        lateinit var result: ResultOf<Map<String, Any>?, Unit>
        firestore
            .collection(collection)
            .document(document)
            .get()
            .addOnSuccessListener {
                result = ResultOf.Success(it.data)
            }
            .addOnFailureListener {
                Log.e("FirebaseError", it.localizedMessage ?: it.message ?: "")
                result = ResultOf.Error(Unit)
            }
            .await()
        return result
    }
}