package com.example.eatswuneekotlin.community

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.eatswuneekotlin.R

class Friend_Write_Spinner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_write)
        val spinner = findViewById<Spinner>(R.id.spinner_spot)
        val kind2 = resources.getStringArray(R.array.spinner_array_place)
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            baseContext,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}