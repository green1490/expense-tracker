package com.example.expensetracker.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.ExpenseData
import com.example.expensetracker.databinding.FragmentHomeBinding
import com.example.expensetracker.ui.expense.Expense
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.lang.reflect.Type


class HomeFragment : Fragment() {
    //To DO:Sensor,layout rework,setting,localisation
    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel:HomeViewModel
    lateinit var adapter:CustomAdapter
    val filename:String = "history"
    val expenses:MutableList<ExpenseData> = mutableListOf()
    lateinit var br:BufferedReader




    private val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result?.resultCode == AppCompatActivity.RESULT_OK) {
            val sum = result.data!!.getStringExtra("Sum")!!.trim()
            val category = result.data!!.getStringExtra("Category")?.trim()
            val description = result.data!!.getStringExtra("Description")?.trim()
            val icon = result.data!!.getIntExtra("Icon",0)
            val isChecked = result.data!!.getBooleanExtra("Checked",false)

            adapter.addItem(ExpenseData(
                sum, category!!,description, icon,isChecked,homeViewModel.balance.value!!
            ))


            if(!isChecked)
                homeViewModel.setBalance(homeViewModel.balance.value!!.minus(sum.toInt()))
            else
                homeViewModel.setBalance(homeViewModel.balance.value!!.plus(sum.toInt()))

            homeViewModel.setPercentage(((homeViewModel.balance.value!!.toFloat()
                    / homeViewModel.maxPayment.value!!.toFloat())*100).toInt())

            expenses.add(ExpenseData(sum, category,description,icon,isChecked,homeViewModel.balance.value!!))

            val json = GsonBuilder().setPrettyPrinting().create().toJson(expenses)
            lifecycleScope.launch(Dispatchers.IO) {
                requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
                    it.write(json.toByteArray())
                }
            }

            val textViewPercentage: TextView = binding.textPercentage
            textViewPercentage.text = homeViewModel.percentage.value.toString()

            val progressBar: ProgressBar = binding.progressBar
            progressBar.progress = homeViewModel.percentage.value!!

            val textViewBalance:TextView = binding.textViewBalance
            textViewBalance.text = homeViewModel.balance.value!!.toString()

            if(homeViewModel.balance.value!! < 0)
                textViewPercentage.setTextColor(Color.RED)
            else
                textViewPercentage.setTextColor(Color.WHITE)
        }
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {




        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root:View =     binding.root
        val button =        binding.button
        val recyclerView =  binding.recyclerView


        button.setOnClickListener {newLayout()}

        adapter = CustomAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch(Dispatchers.IO) {
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
            models.forEach { expense ->
                adapter.addItem(expense)
                expenses.add(expense)

            }
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        //start a fragment to ask for current balance
        homeViewModel.setBalance(if(expenses.size == 0)  0  else expenses.last().balance)
        homeViewModel.setPercentage(((homeViewModel.balance.value!!.toFloat()
                / homeViewModel.maxPayment.value!!.toFloat())*100).toInt())

        val textViewBalance =       binding.textViewBalance
        val progressBarPercentage = binding.progressBar
        val textViewPercentage =    binding.textPercentage

        progressBarPercentage.progress = homeViewModel.percentage.value!!
        textViewBalance.text = homeViewModel.balance.value.toString()
        textViewPercentage.text = homeViewModel.percentage.value!!.toString()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        br.close()
        _binding = null
    }


    private fun newLayout() {
        val intent = Intent(context, Expense::class.java)
        activityLauncher.launch(intent)
    }

}