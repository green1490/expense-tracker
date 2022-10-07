package com.example.expensetracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class HomeViewModel: ViewModel() {

    private val _date = MutableLiveData<LocalDate>().apply {
        value = LocalDate.now()
    }

    private val _balance = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _maxPayment = MutableLiveData<UInt>().apply {
        value = UInt.MIN_VALUE
    }

    private val _percentage = MutableLiveData<Int>().apply {
        value = 0
    }


    fun setDate(date:LocalDate) {
        _date.value = date
    }

    fun setPercentage(percentage:Int) {
        _percentage.value = percentage
    }

    fun setBalance(balance:Int) {
        _balance.value = balance
    }

    init {
        _date.value = LocalDate.now()
        _maxPayment.value = 6000u
    }


    // bank identifier variable
    val balance:    LiveData<Int> = _balance
    val maxPayment: LiveData<UInt> = _maxPayment
    val percentage: LiveData<Int> = _percentage
}