package com.example.expensetracker.ui.statistic

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.expensetracker.ExpenseData
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentStatisticBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.lang.reflect.Type

class StatisticFragment : Fragment() {

    private var _binding: FragmentStatisticBinding? = null
    private val filename = "history"
    private lateinit var br:BufferedReader

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val expenses:MutableList<ExpenseData> = mutableListOf()
    private val expensesDict:MutableMap<String,Int> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(StatisticViewModel::class.java)

        _binding = FragmentStatisticBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val pieData = lifecycleScope.async(Dispatchers.IO) {

             br = try {
                requireContext().openFileInput(filename).bufferedReader()
            }
            catch (e:FileNotFoundException) {
                File(filename).createNewFile()
                File(filename).bufferedReader()
            }


            val json = Gson()
            val type: Type = object : TypeToken<MutableList<ExpenseData?>?>() {}.type
            val models: MutableList<ExpenseData>? = json.fromJson(br, type)
            val pieEntries: MutableList<PieEntry> = mutableListOf()

            models?.forEach { expense ->
                if (expense.category != getString(R.string.income)) {
                    expenses.add(expense)
                    val value = expensesDict[expense.category]
                    if(value == null) {
                        expensesDict[expense.category] = expense.sum.toInt()
                    }
                    else {
                        expensesDict[expense.category] = value + expense.sum.toInt()
                    }
                    //pieEntries.add(PieEntry(expense.sum.toFloat(),expense.category))
                }
            }

            expensesDict.forEach { (key, value) ->
                pieEntries.add(PieEntry(value.toFloat(),key))
            }

            return@async pieEntries
        }
        lifecycleScope.launch(Dispatchers.Main) {
            setUpChart(pieData)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        br.close()
        _binding = null
    }

    private suspend fun setUpChart(pieEntries:Deferred<MutableList<PieEntry>>) {
        val categoryPieChart = binding.categoryPieChart
        categoryPieChart.isRotationEnabled = true
        categoryPieChart.animateY(1400, Easing.EaseInOutQuad)
        categoryPieChart.setEntryLabelTextSize(12f)
        categoryPieChart.setExtraOffsets(5f, 5f, 5f, 5f)
        categoryPieChart.minAngleForSlices = 30f
        categoryPieChart.setCenterTextSize(25f)

        val pieDataSet = PieDataSet(pieEntries.await(),"expenses")
        pieDataSet.colors = ColorTemplate.PASTEL_COLORS.toList()
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.sliceSpace = 5f
        pieDataSet.valueTextSize = 25f
        pieDataSet.valueTextColor = Color.WHITE

        val pieData = PieData(pieDataSet)

        categoryPieChart.data = pieData
        categoryPieChart.description.isEnabled = false
        categoryPieChart.centerText = getString(R.string.expenses)
        categoryPieChart.invalidate()
    }
}