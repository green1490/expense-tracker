package com.example.expensetracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.util.*

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

    private val _balance = MutableLiveData<UInt>().apply {
        value = UInt.MIN_VALUE
    }

    private val _maxPayment = MutableLiveData<UInt>().apply {
        value = UInt.MIN_VALUE
    }

    private val _balancePercentage = MutableLiveData<Int>().apply {
        value = 0
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

    fun setBalance(balance:UInt) {
        _balance.value = balance
    }

    init {
        _title.value = "Test buying"
        _sum.value = 4000
        _date.value = LocalDate.now()
        _labels.value?.addAll(mutableListOf("food","electronic"))
        _description.value = "Test for desc"
        _maxPayment.value = 6000u
        _balance.value = 5000u
        _balancePercentage.value = (_balance.value!!.toInt() / _maxPayment.value!!.toInt()) * 100
    }


    // bank identifier variable
    val title:          LiveData<String> = _title
    val sum:            LiveData<Int> = _sum
    val date:           LiveData<LocalDate> = _date
    val labels:         LiveData<MutableList<String>> = _labels
    val description:    LiveData<String> = _description
    val balance:        LiveData<UInt> = _balance
    val maxPayment:     LiveData<UInt> = _maxPayment
    val balancePercentage:     LiveData<Int> = _balancePercentage
}