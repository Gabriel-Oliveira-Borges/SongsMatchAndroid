package com.example.songmatch.core.presentation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController

abstract class BaseFragment: Fragment() {
    protected val navController: NavController by lazy {
        view?.findNavController() ?: throw Exception("No view found")
    }
}