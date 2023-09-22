package com.example.crudonfirestore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudonfirestore.R
import com.example.crudonfirestore.adapter.holder.StudentsHolder
import com.example.crudonfirestore.model.Students

class StudentsAdapter(private var data: ArrayList<Students>) : RecyclerView.Adapter<StudentsHolder>() {
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(student: Students)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_students, parent, false)
        return StudentsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StudentsHolder, position: Int) {
        val student = data[position]
        holder.setData(student)
        holder.itemView.setOnClickListener {
            listener?.onItemClick(student)
        }
    }
}
