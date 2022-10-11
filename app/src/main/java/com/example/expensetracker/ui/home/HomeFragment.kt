package com.example.expensetracker.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.preference.PreferenceManager
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
import java.io.FileNotFoundException
import java.lang.reflect.Type


class HomeFragment : Fragment() {
    //To DO:Sensor,layout rework,setting,localisation
    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel:HomeViewModel
    private lateinit var adapter:CustomAdapter
    private val filename:String = "history"
    private val expenses:MutableList<ExpenseData> = mutableListOf()
    private lateinit var br:BufferedReader
    private var wasRead = false




    @SuppressLint("NotifyDataSetChanged")
    private val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result?.resultCode == AppCompatActivity.RESULT_OK) {
            val sum = result.data!!.getStringExtra("Sum")!!.trim()
            val category = result.data!!.getStringExtra("Category")?.trim()
            val description = result.data!!.getStringExtra("Description")?.trim()
            val icon = result.data!!.getIntExtra("Icon",0)
            val isChecked = result.data!!.getBooleanExtra("Checked",false)

            if(!isChecked)
                homeViewModel.setBalance(homeViewModel.balance.value!!.minus(sum.toInt()))
            else
                homeViewModel.setBalance(homeViewModel.balance.value!!.plus(sum.toInt()))

            homeViewModel.setPercentage(((homeViewModel.balance.value!!.toFloat()
                    / homeViewModel.maxPayment.value!!.toFloat())*100).toInt())

            expenses.add(ExpenseData(sum, category!!,description,icon,isChecked,homeViewModel.balance.value!!))
            adapter.notifyDataSetChanged()


            val json = GsonBuilder().setPrettyPrinting().create().toJson(adapter.returnExpenses())
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

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val errNumber = 100000
        val maxPayment = sharedPreferences.getString("max_payment","$errNumber")
        val maxPaymentNumber = try {
            maxPayment?.toInt() ?: errNumber
        }
        catch(e:NumberFormatException) {
            errNumber
        }

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.setMaxPayment(maxPaymentNumber)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val textViewBalance =       binding.textViewBalance
        val progressBarPercentage = binding.progressBar
        val textViewPercentage =    binding.textPercentage

        val root:View =     binding.root
        val button =        binding.button
        val recyclerView =  binding.recyclerView
        val imageButton =   binding.imageButtonDelete


        button.setOnClickListener {newLayout()}

        imageButton.setOnClickListener {
            adapter.clearItems()
            homeViewModel.setBalance(0)
            homeViewModel.setPercentage(0)
            textViewBalance.text =              homeViewModel.balance.value!!.toString()
            progressBarPercentage.progress =    homeViewModel.percentage.value!!.toInt()
            textViewPercentage.text =           homeViewModel.percentage.value.toString()
        }

        val fileReadingJob = lifecycleScope.launch(Dispatchers.IO) {
            br = try {
                 requireContext().openFileInput(filename).bufferedReader()
            }
            catch (e:FileNotFoundException) {
                requireContext().openFileOutput(filename,Context.MODE_PRIVATE).close()
                requireContext().openFileInput(filename).bufferedReader()
            }

            val type: Type = object : TypeToken<MutableList<ExpenseData>>() {}.type
            val json = Gson()
            val models: MutableList<ExpenseData>? = json.fromJson(br, type)
            if(!wasRead) {
                wasRead = true
                expenses.addAll(models ?: mutableListOf())
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            fileReadingJob.join()
            adapter = CustomAdapter(requireContext(), expenses)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        return root
    }

    override fun onPause() {
        super.onPause()
        val json = GsonBuilder().setPrettyPrinting().create().toJson(adapter.returnExpenses())
        requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
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
        textViewPercentage.text = homeViewModel.percentage.value.toString()
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