package com.example.myapplication2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "در حال دریافت موقعیت مکانی..."
    }
    val text: LiveData<String> = _text

    fun updateText(newText: String) {
        _text.value = newText
    }
}
