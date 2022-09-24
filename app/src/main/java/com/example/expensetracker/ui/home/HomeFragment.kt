package com.example.expensetracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val _fileName:String = "history"
    private lateinit var homeViewModel:HomeViewModel

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
        homeViewModel.balancePercentage.observe(viewLifecycleOwner) {
            textView.text = it.toString()
        }

        val progressBar: ProgressBar = binding.progressBar
        progressBar.progress = homeViewModel.balancePercentage.value!!

        val recyclerView = binding.recyclerView
        val adapter = CustomAdapter(this.requireContext(), listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        context.openFileOutput(_fileName, Context.MODE_PRIVATER).use {
//          it.write()
//        }
        _binding = null
    }
}