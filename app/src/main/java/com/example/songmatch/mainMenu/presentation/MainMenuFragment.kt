package com.example.songmatch.mainMenu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.songmatch.core.presentation.BaseFragment
import com.example.songmatch.databinding.MainMenuFragmentBinding
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewAction
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuFragment: BaseFragment() {
    private val viewModel: MainManuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MainMenuFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = this@MainMenuFragment.viewModel
            this.lifecycleOwner = this@MainMenuFragment.viewLifecycleOwner
            observeActions()
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatchViewAction(MainMenuViewAction.Init)
    }

    //TODO: Qual é o melhor lugar para se observar as ações?
    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner) {
            when(it) {
                is MainMenuViewState.Action.NavigateToJoinRoom -> {
                    navController.navigate(MainMenuFragmentDirections.actionRoomSelectionFragmentToJoinRoomFragment())
                }
            }
        }
    }

    companion object {
        fun newInstance() = MainMenuFragment()
    }
}