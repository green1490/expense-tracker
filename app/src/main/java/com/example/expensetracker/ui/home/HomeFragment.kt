package com.example.expensetracker.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.ExpenseData
import com.example.expensetracker.databinding.FragmentHomeBinding
import com.example.expensetracker.ui.expense.Expense

class HomeFragment : Fragment() {
    //kiírás onStop new item flag-el
    private var _binding: FragmentHomeBinding? = null
    private val _fileName:String = "history"
    private lateinit var homeViewModel:HomeViewModel
    lateinit var adapter:CustomAdapter

    private val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result?.resultCode == AppCompatActivity.RESULT_OK) {
            val sum = result.data!!.getStringExtra("Sum")?.trim()
            val category = result.data!!.getStringExtra("Category")?.trim()
            val description = result.data!!.getStringExtra("Description")?.trim()
            val icon = result.data!!.getIntExtra("Icon",0)
            adapter.addItem(ExpenseData(sum!!,category!!,description, icon))

            homeViewModel.setBalance(homeViewModel.balance.value!!.minus(sum.toUInt()))

            val textView: TextView = binding.textHome
            textView.text = ((homeViewModel.balance.value!!.toFloat()
                    / homeViewModel.maxPayment.value!!.toFloat())*100).toInt().toString()
            val progressBar: ProgressBar = binding.progressBar
            progressBar.progress = ((homeViewModel.balance.value!!.toFloat()
                    / homeViewModel.maxPayment.value!!.toFloat())*100).toInt()
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        context.openFileOutput(_fileName, Context.MODE_PRIVATER).use {
//          it.write()
//        }
        _binding = null
    }


    private fun newLayout() {
        val intent = Intent(context, Expense::class.java)
        activityLauncher.launch(intent)
    }

}