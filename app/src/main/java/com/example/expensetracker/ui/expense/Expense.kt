package com.example.expensetracker.ui.expense

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetracker.R
import com.google.android.material.switchmaterial.SwitchMaterial


class Expense : AppCompatActivity() {
    private lateinit var tag:String
    private var icon:Int = 0

    private val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result?.resultCode == RESULT_OK) {
            tag = result.data!!.getStringExtra("tag")!!
            icon = result.data!!.getIntExtra("icon", 0)
            val tagImageView = findViewById<ImageView>(R.id.tag_image)
            tagImageView.setImageResource(icon)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
    }

    override fun onStart() {
        super.onStart()

        val addTagButton = findViewById<ImageButton>(R.id.add_tag)
        addTagButton.setOnClickListener {
            val intent = Intent(this, Tags::class.java)
            activityLauncher.launch(intent)
        }

        val switch = findViewById<SwitchMaterial>(R.id.expense_switch)
        switch.setOnClickListener {
            addTagButton.isEnabled = !switch.isChecked
            val tagImage = findViewById<ImageView>(R.id.tag_image)
            tagImage.setImageDrawable(null)
        }

        val button = findViewById<Button>(R.id.expense_save)
        button.setOnClickListener {
            val sum: EditText = findViewById(R.id.editTextSum)
            val tagImage = findViewById<ImageView>(R.id.tag_image)
            val description: EditText = findViewById(R.id.editTextDescription)

            if (!switch.isChecked) {
                if (sum.text.trim().isEmpty() || tagImage.drawable == null) {
                    if (sum.text.trim().isEmpty())
                        sum.error = "Fill it out. BAKA! HMPF!"
                    Toast.makeText(
                        baseContext, "Please fill out Sum and choose a Tag", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    this.setResult(
                        Activity.RESULT_OK, Intent()
                            .putExtra("Sum", sum.text.toString())
                            .putExtra("Category", tag)
                            .putExtra("Description", description.text.toString())
                            .putExtra("Icon", icon)
                            .putExtra("Checked",switch.isChecked)
                    )
                    finish()
                }
            }
            else {
                if (sum.text.trim().isEmpty())
                    sum.error = "Fill it out. BAKA! HMPF!"
                else{
                    this.setResult(
                        Activity.RESULT_OK, Intent()
                            .putExtra("Sum", sum.text.toString())
                            .putExtra("Category", "Income")
                            .putExtra("Description", description.text.toString())
                            .putExtra("Icon", R.drawable.ic_income)
                            .putExtra("Checked",switch.isChecked)
                    )
                    finish()
                }
            }
        }
    }

}