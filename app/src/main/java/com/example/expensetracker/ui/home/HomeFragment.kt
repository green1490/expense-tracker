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
import java.io.FileNotFoundException
import java.lang.reflect.Type


class HomeFragment : Fragment() {
    //kiírás onStop new item flag-el
    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel:HomeViewModel
    lateinit var adapter:CustomAdapter
    val filename:String = "history"
    val expenses:MutableList<ExpenseData> = mutableListOf()



    private val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result?.resultCode == AppCompatActivity.RESULT_OK) {
            val sum = result.data!!.getStringExtra("Sum")!!.trim()
            val category = result.data!!.getStringExtra("Category")?.trim()
            val description = result.data!!.getStringExtra("Description")?.trim()
            val icon = result.data!!.getIntExtra("Icon",0)
            val isChecked = result.data!!.getBooleanExtra("Checked",false)




            expenses.add(ExpenseData(sum, category!!,description,icon,isChecked))
            val json = GsonBuilder().setPrettyPrinting().create().toJson(expenses)
            lifecycleScope.launch(Dispatchers.IO) {
                    requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
                        it.write(json.toByteArray())
                }
            }


            // ez majd a onstartba hozzáadni
            adapter.addItem(ExpenseData(sum, category,description, icon,isChecked))

            if(!isChecked)
                homeViewModel.setBalance(homeViewModel.balance.value!!.minus(sum.toInt()))
            else
                homeViewModel.setBalance(homeViewModel.balance.value!!.plus(sum.toInt()))

            val textView: TextView = binding.textHome
            textView.text = ((homeViewModel.balance.value!!.toFloat()
                    / homeViewModel.maxPayment.value!!.toFloat())*100).toInt().toString()
            val progressBar: ProgressBar = binding.progressBar
            progressBar.progress = ((homeViewModel.balance.value!!.toFloat()
                    / homeViewModel.maxPayment.value!!.toFloat())*100).toInt()

            if(homeViewModel.balance.value!! < 0)
                textView.setTextColor(Color.RED)
            else
                textView.setTextColor(Color.WHITE)
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
        val root: View = binding.root

        val textView: TextView = binding.textHome
        textView.text = ((homeViewModel.balance.value!!.toFloat()
                / homeViewModel.maxPayment.value!!.toFloat())*100).toInt().toString()

        val progressBar: ProgressBar = binding.progressBar
        progressBar.progress = ((homeViewModel.balance.value!!.toFloat()
                / homeViewModel.maxPayment.value!!.toFloat())*100).toInt()

        val button = binding.button
        button.setOnClickListener {newLayout()}

        val recyclerView = binding.recyclerView
        adapter = CustomAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch(Dispatchers.IO) {
            val br:BufferedReader? = try {
                 requireContext().openFileInput(filename).bufferedReader()
            }
            catch (e:FileNotFoundException) {
                null
            }
            if (br != null) {
                val json = Gson()
                val type: Type = object : TypeToken<MutableList<ExpenseData?>?>() {}.type
                val models: MutableList<ExpenseData> = json.fromJson(br, type)
                models.forEach { expense ->
                    adapter.addItem(expense)
                }
            }
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        for (expense:ExpenseData in expenses) {
            if(expense.isChecked) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun newLayout() {
        val intent = Intent(context, Expense::class.java)
        activityLauncher.launch(intent)
    }

}