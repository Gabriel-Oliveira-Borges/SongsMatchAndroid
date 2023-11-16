package com.example.songmatch.mainMenu.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.songmatch.databinding.PersonInRoomItemBinding
import com.example.songmatch.mainMenu.presentation.RoomViewModel


class UsersInRoomAdapter(private val viewModel: RoomViewModel): RecyclerView.Adapter<UsersInRoomAdapter.ViewHolder>() {
    lateinit var binding: PersonInRoomItemBinding

    class ViewHolder(var binding: PersonInRoomItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.room = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PersonInRoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return viewModel.viewState.room.usersToken.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewModel.viewState.room.let {
            holder.bind(it.usersToken[position])
        }
    }
}