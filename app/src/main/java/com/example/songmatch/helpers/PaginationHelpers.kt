package com.example.songmatch.helpers

import com.example.songmatch.data.api.PagingObjectResponse
import com.example.songmatch.domain.model.ResponseError
import com.example.songmatch.domain.model.ResultOf

suspend fun <T> getAllPaginatedItems(
    limit: Int,
    initialOffset: Int = 0,
    block: suspend (limit: Int, offset: Int) -> PagingObjectResponse<T>
): ResultOf<List<T>, ResponseError> {
    return safeApiCall {
        var offset = initialOffset
        val items = mutableListOf<T>()

        do {
            val resp = block(limit, offset)
            items.addAll(resp.items)
            offset = resp.nextOffset
        } while (resp.hasNext)

        return@safeApiCall  items
    }
}