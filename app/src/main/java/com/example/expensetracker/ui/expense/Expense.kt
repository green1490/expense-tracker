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
    private var sum = "0"

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

        val numberPicker = findViewById<NumberPicker>(R.id.picker)
        val sumTextView = findViewById<TextView>(R.id.sum)

        val minValue = 0
        val maxValue = 1_000
        val step = 200

        val numbers = Array(maxValue+1) { "0" }
        for (i:Int in minValue..maxValue) {
            numbers[i] = (i*step).toString()
        }

        numberPicker.minValue = minValue
        numberPicker.maxValue = maxValue

        numberPicker.wrapSelectorWheel = false
        numberPicker.displayedValues = numbers

        numberPicker.setOnScrollListener { picker, number ->
            sumTextView.setText((picker.value*200).toString())
        }

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            sumTextView.setText((picker.value*200).toString())
        }


//        val editTextSum = findViewById<EditText>(R.id.editTextNumber)
//
//        editTextSum.setOnClickListener {
//            openDialog()
//        }

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
            val tagImage = findViewById<ImageView>(R.id.tag_image)
            val description: EditText = findViewById(R.id.editTextDescription)

            sum = (numberPicker.value *200).toString()

            if (!switch.isChecked) {
                if (sum.toInt() == 0 || tagImage.drawable == null) {
                    Toast.makeText(
                        baseContext, "Please fill out Sum and choose a Tag", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    this.setResult(
                        Activity.RESULT_OK, Intent()
                            .putExtra("Sum", sum)
                            .putExtra("Category", tag)
                            .putExtra("Description", description.text.toString())
                            .putExtra("Icon", icon)
                            .putExtra("Checked",switch.isChecked)
                    )
                    finish()
                }
            }
            else {
                if (sum.toInt() == 0)
                    Toast.makeText(
                        baseContext, "Please fill out Sum", Toast.LENGTH_SHORT
                    ).show()
                else{
                    this.setResult(
                        Activity.RESULT_OK, Intent()
                            .putExtra("Sum", sum)
                            .putExtra("Category", getString(R.string.income))
                            .putExtra("Description", description.text.toString())
                            .putExtra("Icon", R.drawable.ic_income)
                            .putExtra("Checked",switch.isChecked)
                    )
                    finish()
                }
            }
        }
    }

//    private fun openDialog() {
//
//        val linearLayout = layoutInflater.inflate(R.layout.activity_view_number,null) as LinearLayout
//        val numberPicker = linearLayout.findViewById<NumberPicker>(R.id.picker)
//
//        val minValue = 0
//        val maxValue = 1_000
//        val step = 200
//
//        val numbers = Array(maxValue+1) { "0" }
//        for (i:Int in minValue..maxValue) {
//            numbers[i] = (i*step).toString()
//        }
//
//        numberPicker.minValue = minValue
//        numberPicker.maxValue = maxValue
//
//        numberPicker.wrapSelectorWheel = false
//        numberPicker.displayedValues = numbers
//
//
//        //Finally building an AlertDialog
//        val builder = AlertDialog.Builder(this)
//            .setPositiveButton("Submit", null)
//            .setNegativeButton("Cancel", null)
//            .setView(linearLayout)
//            .setCancelable(false)
//            .create()
//        builder.show()
//        //Setting up OnClickListener on positive button of AlertDialog
//        //Setting up OnClickListener on positive button of AlertDialog
//        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
//            sum = (numberPicker.value*200).toString()
//            findViewById<EditText>(R.id.editTextNumber).setText(sum)
//            builder.cancel()
//        }
//
//    }

}