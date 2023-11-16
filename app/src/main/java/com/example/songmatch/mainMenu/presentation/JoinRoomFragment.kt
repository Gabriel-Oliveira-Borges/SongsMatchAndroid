package com.example.songmatch.mainMenu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.songmatch.databinding.JoinRoomFragmentBinding
import com.example.songmatch.core.presentation.BaseFragment
import com.example.songmatch.mainMenu.presentation.model.JoinRoomViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinRoomFragment : BaseFragment() {
    private val viewModel: JoinRoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return JoinRoomFragmentBinding.inflate(inflater, container, false).apply {
            this.viewModel = this@JoinRoomFragment.viewModel
            this.lifecycleOwner = this@JoinRoomFragment.viewLifecycleOwner
            this@JoinRoomFragment.observeActions()
        }.root
    }

    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner) {
            when(it) {
                is JoinRoomViewState.Action.NavigateToRoomFragment -> {
                    navController.navigate(JoinRoomFragmentDirections.actionJoinRoomFragmentToRoomFragment())
                }
            }
        }
    }

    companion object {
        fun newInstance() = JoinRoomFragment()
    }
}

//O que falta fazer:
//            5 - Entrar na sala
//            6 - Tela da sala. Mostrar quantas pessoas estao nela. Quando tiver mais que 2, criar playlist com as músicas já subidas no firebase!
//            7 - Tela de player e recicle view das músicas(se der tempo)
//            8 - Reorganizar arquivos.
//            9 - Fazer testes unitários das principais classes
//            10 - Ajustes finos de design