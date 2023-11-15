package com.example.songmatch.mainMenu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.songmatch.core.presentation.BaseFragment
import com.example.songmatch.databinding.RoomFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomFragment: BaseFragment() {
    private val viewModel: RoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return RoomFragmentBinding.inflate(inflater, container, false).apply {
            this.lifecycleOwner = this@RoomFragment.viewLifecycleOwner
        }.root
    }
}