package com.example.songmatch.core.data

import com.example.songmatch.core.api.RemoteAPI
import com.example.songmatch.core.api.SaveUserRequestBody
import com.example.songmatch.core.framework.room.entities.UserEntity
import com.example.songmatch.core.helpers.safeApiCall
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun saveUser(user: UserEntity): ResultOf<UserEntity, ResponseError>
}

class RemoteDataSourceImp @Inject constructor(
    private val remoteAPI: RemoteAPI
) : RemoteDataSource {
    override suspend fun saveUser(user: UserEntity): ResultOf<UserEntity, ResponseError> {
        return safeApiCall {
            val response = remoteAPI.saveUser(
                SaveUserRequestBody(name = user.name!!, email = user.email!!, imageUri = user.imageUri!!)
            )

            user.copy(remoteToken = response.token)
        }
    }
}