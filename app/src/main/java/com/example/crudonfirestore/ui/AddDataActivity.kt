package com.example.crudonfirestore.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.crudonfirestore.R
import com.example.crudonfirestore.databinding.ActivityAddDataBinding
import com.example.crudonfirestore.dialog.ConfirmationDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddDataActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityAddDataBinding
    // database
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back button
        binding.btnBackArrow.setOnClickListener {
            // confirmation dialog
            val confirmTitle = getString(R.string.question_T3)
            val confirmText = getString(R.string.question_3)

            val confirmationDialog = ConfirmationDialog(this, confirmTitle, confirmText) {
                finish()
            }
            confirmationDialog.show()
        }

        binding.edtBirthDate.setOnClickListener {
            showDatePicker()
        }

        binding.saveBtn.setOnClickListener {
            val sNIS = binding.edtNIS.text.toString().trim()
            val sName = binding.edtName.text.toString().trim()
            val sGender = binding.edtGender.text.toString().trim()
            val sReligion = binding.edtReligion.text.toString().trim()
            val sBirthDate = binding.edtBirthDate.text.toString().trim()

            val studentMap = hashMapOf(
                "NIS" to sNIS,
                "name" to sName,
                "gender" to sGender,
                "religion" to sReligion,
                "birthDate" to sBirthDate
            )

            db.collection("student").document(sNIS).set(studentMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Successfully Added", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed to Added", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Set the selected date to the EditText
                binding.edtBirthDate.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}