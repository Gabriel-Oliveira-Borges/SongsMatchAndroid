package com.example.songmatch.mainMenu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.songmatch.core.presentation.BaseFragment
import com.example.songmatch.databinding.PersonInRoomItemBinding
import com.example.songmatch.databinding.RoomFragmentBinding
import com.example.songmatch.mainMenu.presentation.adapter.UsersInRoomAdapter
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewAction
import com.example.songmatch.mainMenu.presentation.model.RoomViewAction
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
            recyclerView.adapter = UsersInRoomAdapter(this@RoomFragment.viewModel)
            recyclerView.layoutManager = LinearLayoutManager(this@RoomFragment.context)
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatchViewAction(RoomViewAction.ListenToCurrentRoom)
    }
}