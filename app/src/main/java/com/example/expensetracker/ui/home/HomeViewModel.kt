package com.example.expensetracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class HomeViewModel: ViewModel() {

    private val _title = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val _sum = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _date = MutableLiveData<LocalDate>().apply {
        value = LocalDate.now()
    }

    private val _labels = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }

    private val _description = MutableLiveData<String>().apply {
        value = String()
    }

    private val _balance = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _maxPayment = MutableLiveData<UInt>().apply {
        value = UInt.MIN_VALUE
    }

    fun setTitle(text:String) {
        _title.value = text
    }

    fun setSum(value:Int) {
        _sum.value = value
    }

    fun setDate(date:LocalDate) {
        _date.value = date
    }

    fun setLabels(labels:MutableList<String>) {
        _labels.value = labels
    }

    fun setDescription(text:String) {
        _description.value = text
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
    val title:                  LiveData<String> = _title
    val sum:                    LiveData<Int> = _sum
    val date:                   LiveData<LocalDate> = _date
    val labels:                 LiveData<MutableList<String>> = _labels
    val description:            LiveData<String> = _description
    val balance:                LiveData<Int> = _balance
    val maxPayment:             LiveData<UInt> = _maxPayment
}