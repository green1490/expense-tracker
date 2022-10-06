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


    fun setDate(date:LocalDate) {
        _date.value = date
    }

    fun setBalance(balance:Int) {
        _balance.value = balance
    }

    init {
        _date.value = LocalDate.now()
        _maxPayment.value = 6000u
        _balance.value = 3000
    }


    // bank identifier variable
    val balance:                LiveData<Int> = _balance
    val maxPayment:             LiveData<UInt> = _maxPayment
}