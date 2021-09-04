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
        
        val a = savedTracks.onError {  }.handleResult()
//        TODO: Ver no caderninho quais sons eu vou pegar.
//        Antes de atualizar, validadar se é preciso de fato atualizar os itens
//        Criar uma request para cada "tipo" (Como é paginado, provavelmente eu vou precisar fazer várias requests no SpotifyDataSource)
//        Tipo = Todas as músicas salvas e As top músicas (short_term, medium_term e long_term)
//        Depois de pegar, criar no room as entities para esse user e salvar lá
//        Colocar hooks de quando o usuario for deletado, todas as musicas dele forem trocadas
//        Criar uma tabela somente para saber há quanto tempo as músicas não são atualizadas. Criar uma request do tipo /configs no backend para
//        retornar infos básicas do app (Ter um default para não crashar), tipo a cada quanto tempo as musicas devem ser atualizadas
//        Criar aqui três tentativas para sucesso. Se falhar, retornar ResultOf.Error
        return ResultOf.Success(Unit)
    }
}