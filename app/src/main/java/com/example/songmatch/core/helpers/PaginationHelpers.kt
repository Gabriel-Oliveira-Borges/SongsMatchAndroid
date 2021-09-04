package com.example.songmatch.core.helpers

import com.example.songmatch.core.api.PagingObjectResponse

suspend fun <T> getAllPaginatedItems(
    limit: Int,
    offset: Int = 0,
    block: suspend (limit: Int, offset: Int) -> PagingObjectResponse<T>
): List<T>? {
    return safeApiCall {
        val resp = block(limit, offset)
        if (!resp.hasNext) {
            return@safeApiCall resp.items
        }

        return@safeApiCall resp.items + getAllPaginatedItems(
            limit = limit,
            offset = limit + offset,
            block = block
        )!!
    }.handleResult()
}