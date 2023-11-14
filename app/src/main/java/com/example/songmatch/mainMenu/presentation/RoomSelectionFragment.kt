package com.example.songmatch.mainMenu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.songmatch.databinding.RoomSelectionBinding
import com.example.songmatch.mainMenu.presentation.model.RoomSelectionViewAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomSelectionFragment: Fragment() {
    private val viewModel: RoomSelectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return RoomSelectionBinding.inflate(inflater, container, false).apply {
            viewModel = this@RoomSelectionFragment.viewModel
            this.lifecycleOwner = this@RoomSelectionFragment
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatchViewAction(RoomSelectionViewAction.Init)
    }

    companion object {
        fun newInstance() = RoomSelectionFragment()
    }
}