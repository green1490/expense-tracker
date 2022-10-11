package com.example.expensetracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    private val _balance = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _maxPayment = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _percentage = MutableLiveData<Int>().apply {
        value = 0
    }

    fun setPercentage(percentage:Int) {
        _percentage.value = percentage
    }

    fun setBalance(balance:Int) {
        _balance.value = balance
    }

    fun setMaxPayment(maxPayment:Int) {
        _maxPayment.value = maxPayment
    }

    // bank identifier variable
    val balance:    LiveData<Int> = _balance
    val maxPayment: LiveData<Int> = _maxPayment
    val percentage: LiveData<Int> = _percentage
}