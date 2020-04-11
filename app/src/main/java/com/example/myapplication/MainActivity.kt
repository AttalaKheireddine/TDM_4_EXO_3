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
    var calendar = Calendar.getInstance();
    var taskList = ArrayList<Task>();
    var listToShow= taskList;
    var shownList = ArrayList<Task>();
    val taskAdapter = TaskAdapter(shownList,this);
    var spinnerItem = "Daily"
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
                    changeShownListToDaily()
                }

                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val item = adapter.getItem(position)
                    if (item!=null)
                    {
                       updateItem(item)
                        spinnerItem = item
                    }
                }
            }
            if (dailyButton!=null)
            {
                dailyButton.setOnClickListener{
                changeShownListToDaily()
                }
            }
            if (weeklyButton!=null){
                weeklyButton.setOnClickListener{
                changeShownListToWeekly()
                }
            }

            if (allButton!=null)
            {
                allButton.setOnClickListener{
                changeShownListToAll()
                }
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
            calendar = Calendar.getInstance()
        }
    }

    fun updateItem(item:String)
    {
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

    fun addTask()
    {
        taskList.add(Task(taskTextInput.text.toString(),calendar))
        updateItem(spinnerItem)
        updateShownList()
    }

    fun changeShownListToDaily()
    {
        val calendar = Calendar.getInstance() //a convenient way to get the current date
        var dailyList = ArrayList<Task>();
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        taskList.forEach{
            if (sdf.format(calendar.time).equals(sdf.format(it.date.time)))
            {
                dailyList.add(it)
            }
        }
        listToShow = dailyList
        updateShownList()
    }

    fun changeShownListToWeekly()
    {
        val calendarNow = Calendar.getInstance() //a convenient way to get the current date
        val calendarAfterAWeek = Calendar.getInstance()
        calendarAfterAWeek.add(Calendar.DATE,7)
        var weeklyList = ArrayList<Task>();
        val myFormat = "dd-MM-yyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        taskList.forEach{
            if (sdf.format(calendarAfterAWeek.time)>sdf.format(it.date.time))
            {
                if (sdf.format(calendarNow.time)<=sdf.format(it.date.time))
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
