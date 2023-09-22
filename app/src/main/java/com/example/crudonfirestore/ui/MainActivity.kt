package com.example.crudonfirestore.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudonfirestore.adapter.StudentsAdapter
import com.example.crudonfirestore.databinding.ActivityMainBinding
import com.example.crudonfirestore.model.Students
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityMainBinding
    // recyclerview
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentsAdapter
    // data
    private lateinit var studentList: ArrayList<Students>
    private var db = Firebase.firestore

    companion object {
        const val EDIT_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // floating action button
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddDataActivity::class.java)
            startActivity(intent)
        }

        // recyclerview
        recyclerView = binding.listRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        studentList = arrayListOf()
        studentAdapter = StudentsAdapter(studentList)
        recyclerView.adapter = studentAdapter

        eventChangeListener()
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()

        db.collection("student")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val student = dc.document.toObject(Students::class.java)
                        studentList.add(student)
                    }
                }
                studentAdapter.notifyDataSetChanged()
            }

        // Add a listener to the adapter to handle clicks on RecyclerView items
        studentAdapter.setOnItemClickListener(object : StudentsAdapter.OnItemClickListener {
            override fun onItemClick(student: Students) {
                val intent = Intent(this@MainActivity, EditDataActivity::class.java)
                intent.putExtra("studentData", student)
                startActivityForResult(intent, EDIT_REQUEST_CODE)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Receive updated data from EditDataActivity
            val updatedStudent = data?.getParcelableExtra<Students>("updatedStudent")

            if (updatedStudent != null) {
                // Update data in studentList
                val position = studentList.indexOfFirst { it.NIS == updatedStudent.NIS }
                if (position != -1) {
                    studentList[position] = updatedStudent
                    studentAdapter.notifyItemChanged(position)
                }
            }
        }

        // Receive an indicator that the item has been removed
        val deletedStudentId = data?.getStringExtra("deletedStudentId")

        if (deletedStudentId != null) {
            // Delete data from studentList
            val position = studentList.indexOfFirst { it.NIS == deletedStudentId }
            if (position != -1) {
                studentList.removeAt(position)
                studentAdapter.notifyItemRemoved(position)
            }
        }
    }
}
