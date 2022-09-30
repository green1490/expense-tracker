package com.example.expensetracker.ui.expense

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetracker.R


class Expense : AppCompatActivity() {

    val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        object: ActivityResultCallback<ActivityResult> {
            override fun onActivityResult(result: ActivityResult?) {
                if (result?.resultCode == RESULT_OK) {
                    val data = result.data!!.getStringExtra("tag")
                    Toast.makeText(baseContext,data,Toast.LENGTH_SHORT).show()
                }
            }
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
    }

    override fun onStart() {
        super.onStart()

        val button = findViewById<Button>(R.id.expense_save)
        button.setOnClickListener {
            //TO DO: extract data and save it
            finish()
        }

        val addTagButton = findViewById<ImageButton>(R.id.add_tag)
        addTagButton.setOnClickListener {
                val intent = Intent(this, Tags::class.java)
                activityLauncher.launch(intent)
            }
    }

}