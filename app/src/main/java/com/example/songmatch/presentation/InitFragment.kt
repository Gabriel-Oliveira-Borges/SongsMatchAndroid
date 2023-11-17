package com.example.songmatch.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.songmatch.NavGraphDirections
import com.example.songmatch.R
import com.example.songmatch.presentation.model.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InitFragment : BaseFragment() {

    private val viewModel: InitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.observeNavigation()
        return inflater.inflate(R.layout.init_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAppStart()
    }

    private fun observeNavigation() {
        viewModel.navigation.observe(viewLifecycleOwner) { navigation ->
            when (navigation) {
                is InitViewModel.Navigation.OpenAuthenticationFlow -> {
                    navController.navigate(NavGraphDirections.actionGlobalToAuthenticationNavGraph())
                }
                is InitViewModel.Navigation.OpenRoomSelectionFlow -> {
                    navController.navigate(NavGraphDirections.actionGlobalToMainNavGraph())
                }
            }
        }
    }

    companion object {
        fun newInstance() = InitFragment()
    }
}