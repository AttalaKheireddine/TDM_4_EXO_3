package com.example.myapplication

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val TAG = "Activity 1"
    val calendar = Calendar.getInstance();
    var taskList = ArrayList<Task>();
    var listToShow= taskList;
    var shownList = ArrayList<Task>();
    val taskAdapter = TaskAdapter(shownList,this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this);
        recycler.adapter = taskAdapter;

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("Daily", "Weekly", "All")
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    // either one will work as well
                    // val item = parent.getItemAtPosition(position) as String
                    val item = adapter.getItem(position)
                    if (item=="Daily")
                    {
                        changeShownListToDaily()
                    }
                    else if (item == "Weekly")
                    {
                        changeShownListToWeekly()
                    }
                    else
                    {
                        changeShownListToAll()
                    }
                }
            }

            dailyButton.setOnClickListener{
                changeShownListToDaily()
            }

            weeklyButton.setOnClickListener{
                changeShownListToWeekly()
            }

            allButton.setOnClickListener{
                changeShownListToAll()
            }
        }

        addTaskButton.setOnClickListener{
            DatePickerDialog(
                this@MainActivity,
                dateSetListener,
                // initialize with calendar's date for conveniece
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePicker, year: Int, monthOfYear: Int,
            dayOfMonth: Int
        ) {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            addTask()
        }
    }

    fun addTask()
    {
        taskList.add(Task(taskTextInput.text.toString(),calendar))
        updateShownList()
    }

    fun changeShownListToDaily()
    {
        val calendar = Calendar.getInstance() //a convenient way to get the current date
        var dailyList = ArrayList<Task>();
        val myFormat = "dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        taskList.forEach{
            if (sdf.format(calendar.time)==sdf.format(it.date))
            {
                dailyList.add(it)
            }
        }
        listToShow = dailyList
        updateShownList()
    }

    fun changeShownListToWeekly()
    {
        val calendar = Calendar.getInstance() //a convenient way to get the current date
        var weeklyList = ArrayList<Task>();
        val myFormat = "dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        taskList.forEach{
            if (sdf.format(calendar.time)+7>sdf.format(it.date))
            {
                if (sdf.format(calendar.time)<=sdf.format(it.date))
                weeklyList.add(it)
            }
        }
        listToShow = weeklyList
        updateShownList()
    }

    fun changeShownListToAll()
    {
        listToShow = taskList
        updateShownList()
    }

    fun updateShownList(){
        shownList.clear()
        listToShow.forEach{
            shownList.add(it)
        }
        taskAdapter.notifyDataSetChanged()
    }
}
