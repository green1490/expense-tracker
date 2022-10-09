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
import com.example.expensetracker.databinding.FragmentStatisticBinding
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
    lateinit var br:BufferedReader

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val expenses:MutableList<ExpenseData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val statisticViewModel =
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
            val models: MutableList<ExpenseData> = json.fromJson(br, type)
            val pieEntries: MutableList<PieEntry> = mutableListOf()
            models.forEach { expense ->
                expenses.add(expense)
                pieEntries.add(PieEntry(expense.sum.toFloat(),expense.category))

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

        val pieDataSet = PieDataSet(pieEntries.await(),"expenses")
        pieDataSet.valueTextSize = 16f
        pieDataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        pieDataSet.valueTextColor = Color.BLACK

        val pieData = PieData(pieDataSet)

        categoryPieChart.data = pieData
        categoryPieChart.description.isEnabled = false
        categoryPieChart.centerText = "category"
        categoryPieChart.invalidate()
    }
}