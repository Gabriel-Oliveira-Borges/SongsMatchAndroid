package com.example.songmatch.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.songmatch.presentation.model.BaseFragment
import com.example.songmatch.databinding.MainMenuFragmentBinding
import com.example.songmatch.presentation.model.MainMenuViewAction
import com.example.songmatch.presentation.model.MainMenuViewState
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

    private fun observeActions() {
        viewModel.viewState.navigationAction.observe(viewLifecycleOwner) {
            when(it) {
                is MainMenuViewState.Action.NavigateToJoinRoom -> {
                    navController.navigate(MainMenuFragmentDirections.actionRoomSelectionFragmentToJoinRoomFragment())
                }
                is MainMenuViewState.Action.NavigateToRoom -> {
                    navController.navigate(MainMenuFragmentDirections.actionRoomSelectionFragmentToRoomFragment())
                }
            }
        }
    }

    companion object {
        fun newInstance() = MainMenuFragment()
    }
}