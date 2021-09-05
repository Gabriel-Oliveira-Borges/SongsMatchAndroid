package com.example.songmatch.core.helpers

import com.example.songmatch.core.api.PagingObjectResponse
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf

suspend fun <T> getAllPaginatedItems(
    limit: Int,
    offset: Int = 0,
    block: suspend (limit: Int, offset: Int) -> PagingObjectResponse<T>
): ResultOf<List<T>, ResponseError.NetworkError> {
    return safeApiCall {
        val resp = block(limit, offset)
        if (!resp.hasNext) {
            return@safeApiCall resp.items
        }

        return@safeApiCall resp.items + getAllPaginatedItems(
            limit = limit,
            offset = limit + offset,
            block = block
        ) as T
    }
}