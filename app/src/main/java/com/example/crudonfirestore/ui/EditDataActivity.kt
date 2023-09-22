package com.example.crudonfirestore.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.crudonfirestore.R
import com.example.crudonfirestore.databinding.ActivityEditDataBinding
import com.example.crudonfirestore.dialog.ConfirmationDialog
import com.example.crudonfirestore.model.Students
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditDataActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityEditDataBinding
    // data
    private var db = Firebase.firestore
    private lateinit var documentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataBinding.inflate(layoutInflater)
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

        // Get document ID from intent (make sure you send it from MainActivity)
        val student = intent.getParcelableExtra<Students>("studentData")

        if (student != null) {
            documentId = student.NIS ?: ""
            setFireStoreData(student)
        }

        binding.updateBtn.setOnClickListener {
            val sNIS = binding.edtNIS.text.toString().trim()
            val sName = binding.edtName.text.toString().trim()
            val sGender = binding.edtGender.text.toString().trim()
            val sReligion = binding.edtReligion.text.toString().trim()
            val sBirthDate = binding.edtBirthDate.text.toString().trim()

            val updateStudentMap = hashMapOf(
                "NIS" to sNIS,
                "name" to sName,
                "gender" to sGender,
                "religion" to sReligion,
                "birthDate" to sBirthDate
            )

            db.collection("student").document(documentId).set(updateStudentMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Successfully Updated", Toast.LENGTH_SHORT).show()
                    // Send the updated data back to the MainActivity
                    val updatedStudent = Students(sNIS, sName, sGender, sReligion, sBirthDate)
                    val resultIntent = Intent()
                    resultIntent.putExtra("updatedStudent", updatedStudent)
                    setResult(Activity.RESULT_OK, resultIntent)

                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed to Updated", Toast.LENGTH_SHORT).show()
                }
        }

        binding.deleteBtn.setOnClickListener {
            // confirmation dialog
            val confirmTitle = getString(R.string.question_T2)
            val confirmText = getString(R.string.question_2)

            val confirmationDialog = ConfirmationDialog(this, confirmTitle, confirmText) {
                // Using the documentId you have set before
                db.collection("student").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show()
                        // Send an indicator that the item has been removed
                        val resultIntent = Intent()
                        resultIntent.putExtra("deletedStudentId", documentId)
                        setResult(Activity.RESULT_OK, resultIntent)

                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to Delete", Toast.LENGTH_SHORT).show()
                    }
            }
            confirmationDialog.show()
        }
    }

    private fun setFireStoreData(student: Students) {
        binding.edtNIS.setText(student.NIS)
        binding.edtName.setText(student.name)
        binding.edtGender.setText(student.gender)
        binding.edtReligion.setText(student.religion)
        binding.edtBirthDate.setText(student.birthDate)
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
