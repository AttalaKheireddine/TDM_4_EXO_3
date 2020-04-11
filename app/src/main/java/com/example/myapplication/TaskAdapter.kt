package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.one_task.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskAdapter(ls : ArrayList<Task>, cont : Context) : RecyclerView.Adapter<ViewHolder>() {
    var context:MainActivity = cont as MainActivity
    private var taskList =ls
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val ite = layoutInflater.inflate(R.layout.one_task, parent, false)
        return ViewHolder(ite)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        holder.itemView.taskText.text = "You must do ${taskList[position].taskText} at ${sdf.format(taskList[position].date.getTime())}";
        holder.itemView.setOnClickListener{
           try {
               context.taskList.remove(taskList[position])
               taskList.remove(taskList[position])
               notifyItemRemoved(position)
           }catch (e :Exception){}
        }
    }
}