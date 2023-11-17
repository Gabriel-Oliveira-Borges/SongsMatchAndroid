package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.domain.model.SpotifyUser
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.models.ResultOf
import java.util.Date
import javax.inject.Inject

// Todo: Fazer testes de tudo o que for possível testar
interface GetCurrentUserUseCase {
    suspend operator fun invoke(): ResultOf<User?, Unit>
}

class GetCurrentUserUseCaseImp @Inject constructor(
    private val sessionRepository: SessionRepository
) : GetCurrentUserUseCase {
    override suspend operator fun invoke(): ResultOf<User?, Unit> {
        //TODO: Replace remove nullable user
//        return ResultOf.Success(User(
//            name = "Mariana Helena",
//            spotifyUser = SpotifyUser(
//                token = "BQBUT4eZy0SH3rJG6kePQzjYG2MMp92fw4qR68vfZyrjNubf1YkPMV61lvgFdT8nNOYboWKlzm25FSjI3RcKiZpuvbvRHxyTijx8JwD9bB-_SpQ1Gp7lAfJ7JM5t2RClBBq-OPnBiy2Se57hS6-WnGbxkGoYlQLYDTb9GpmPVtICniKZ1rwA1KVfm0P5qBqEJ4SnxHOLD4CGXOytIX9JSgowh7Dhg32pC9mI7j7bqNEiEHm-gzKD6CGhkw",
//                tokenExpiration = Date(),
//                email = "",
//                imageUri = "https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=1355364867827621&height=50&width=50&ext=1702734592&hash=AeS_nFO3RUTIq6gcEDA",
//                uri = "",
//            ),
//            lastTrackUpdate = null,
//            tracksUploaded = true,
//            currentRoom = "71071",
//        ))

//        return ResultOf.Success(User(
//            name = "Paulo Alves",
//            spotifyUser = SpotifyUser(
//                token = "BQBXZj7nb39lK8Eu_GiBPkgBaxhcmWTXT7I6f8Zp8bmrjs3c4KPTOfxOtKNm3bu0JT71IClHx0RRNrHD7GFTU6zKzoBXS1IV17R-KnaRvgpj_uKbAm_r-ZbIXbWeZ4WbBF9I7Q70-2O-sXXV1rD9JgLxr0GHvkhe9yWC3P3R_KZXw_1PXH2LPv09nmIUeAjGz_Djv-GbFDqUbxv9bTQaLiIdrmqmlJzMdM3xK2wsgpGRCWHiBiJIayXpjpslthY",
//                tokenExpiration = Date(),
//                email = "",
//                imageUri = "https://i.scdn.co/image/ab67757000003b8234817b66cf2396900d90eb4e",
//                uri = "",
//            ),
//            lastTrackUpdate = null,
//            tracksUploaded = true,
//            currentRoom = "71071",
//        ))


        return sessionRepository.getCurrentUser()
    }
}