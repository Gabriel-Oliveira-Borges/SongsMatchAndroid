package com.example.songmatch.core.presentation

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<A, S> : ViewModel() {
    abstract val viewState: S

    abstract fun dispatchViewAction(action: A)
}