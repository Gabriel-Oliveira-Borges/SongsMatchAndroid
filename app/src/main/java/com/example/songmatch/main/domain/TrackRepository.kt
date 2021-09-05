package com.example.songmatch.main.domain

import com.example.songmatch.core.data.SpotifyDataSource
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface TrackRepository {
    suspend fun updateSongs(): ResultOf<Unit, ResponseError>
}

class TrackRepositoryImp @Inject constructor(
    private val spotifyDataSource: SpotifyDataSource
): TrackRepository {
    override suspend fun updateSongs(): ResultOf<Unit, ResponseError> {
        val savedTracks = spotifyDataSource.getUserSavedTracks().onError { return ResultOf.Error(ResponseError.NetworkError()) }
        val topTracks = spotifyDataSource.getUserTopTracks().onError { return ResultOf.Error(ResponseError.NetworkError()) }
//        TODO:
//        Depois de pegar, criar no room as entities para esse user e salvar lá
//        Colocar hooks de quando o usuario for deletado, todas as musicas dele forem trocadas
//        Criar uma tabela somente para saber há quanto tempo as músicas não são atualizadas. Criar uma request do tipo /configs no backend para
//        retornar infos básicas do app (Ter um default para não crashar), tipo a cada quanto tempo as musicas devem ser atualizadas
        return ResultOf.Success(Unit)
    }
}