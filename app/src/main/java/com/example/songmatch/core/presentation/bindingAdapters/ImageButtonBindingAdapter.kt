package com.example.songmatch.core.presentation.bindingAdapters

import android.widget.ImageButton
import androidx.databinding.BindingAdapter

import android.R;
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.transition.Visibility

@BindingAdapter("isPlaying")
fun changePlayPauseButton(view: ImageButton, isPlaying: Boolean) {
    if (isPlaying) {
        view.setImageResource(R.drawable.ic_media_pause)
    } else {
        view.setImageResource(R.drawable.ic_media_play)
    }
}

@BindingAdapter("loading")
fun disableButton(view: Button, loading: Boolean) {
    view.isEnabled = !loading
}

@BindingAdapter("loading")
fun showProgressBar(view: ProgressBar, loading: Boolean) {
    if (loading)
        view.visibility = VISIBLE
    else
        view.visibility = GONE
}

@BindingAdapter("loading")
fun showImageView(view: ImageView, loading: Boolean) {
    if (loading)
        view.visibility = INVISIBLE
    else
        view.visibility = VISIBLE
}