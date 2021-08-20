package com.example.songmatch.core.useCase

import android.util.Log
import com.example.songmatch.core.api.SpotifyAPI
import com.example.songmatch.core.domain.SpotifyRepository
import javax.inject.Inject

interface SaveSpotifyUserUseCase {
    suspend operator fun invoke(token: String, expiresIn: Int, name: String?)
}

// Todo: Esse token é válido por apenas 1 hora. Quando eu precisar atualizar as músicas, eu vou precisar pedir novamente pelo login. A parte boa é que fica salvo os dados do login
class SaveSpotifyUserUseCaseImp @Inject constructor(
    private val spotifyRepository: SpotifyRepository,
    private val spotifyAPI: SpotifyAPI
): SaveSpotifyUserUseCase {
    override suspend fun invoke(token: String, expiresIn: Int, name: String?) {
//        TODO: Está funcionando, caralho! O que eu preciso fazer:
//        1: Chamar no spotifyRepository as infos de que eu preciso da api do spotify. Aqui eu vou precisar salvar o usuário localmente antes de fazer essa chamada para poder pegar o token no interceptor!
//        2: Criar um safeApiCall, que lida com as respostas HTTP
//        3: Essa chamada deve retornar um objeto User(Entity do Room) (Criar um mapper para isso)
//        4: Salvar esse objeto retornado localmente
//        5: Se alguma coisa aqui falhar, não esquecer de deletear o usuário localmente.
        spotifyRepository.saveUser(token, expiresIn, name)

        val response = spotifyAPI.getUser()

        if (response.isSuccessful) {
            val body = response.body()
            Log.d("Blah", "Deu certo!\n DisplayName: ${body?.displayName}\n ExternalUrls${body?.externalUrls}\n Images${body?.images}\nURI: ${body?.uri}")
        }
    }
}