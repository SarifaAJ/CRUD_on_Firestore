package com.example.crudonfirestore.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.crudonfirestore.databinding.ItemListStudentsBinding
import com.example.crudonfirestore.model.Students

class StudentsHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
    private val binding = ItemListStudentsBinding.bind(itemView)

    fun setData(student: Students) {
        itemView.apply {
            //get data users
            val nis = "NIS : ${student.NIS.toString()}"
            val name = "Nama : ${student.name}"
            val gender = "Gender : ${student.gender}"
            val religion = "Agama : ${student.religion}"
            val birthDate = "Tanggal Lahir : ${student.birthDate}"
            //set view
            binding.NIS.text = nis
            binding.name.text = name
            binding.gender.text = gender
            binding.religion.text = religion
            binding.birthDate.text = birthDate
        }
    }
}
