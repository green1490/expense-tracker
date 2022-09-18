package com.example.expensetracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class HomeViewModel : ViewModel() {

     private  val _title = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private  val _sum = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _date = MutableLiveData<Date>().apply {
        value = Date()
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

    fun setTitle(text:String) {
        _title.value = text
    }

    fun setSum(value:Int) {
        _sum.value = value
    }

    fun setDate(date:Date) {
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


    // bank identifier variable
    val title:          LiveData<String> = _title
    val sum:            LiveData<Int> = _sum
    val date:           LiveData<Date> = _date
    val labels:         LiveData<MutableList<String>> = _labels
    val description:    LiveData<String> = _description
    val balance:        LiveData<UInt> = _balance
}