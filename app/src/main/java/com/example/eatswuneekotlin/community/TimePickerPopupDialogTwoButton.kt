package com.example.eatswuneekotlin.community

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import com.example.eatswuneekotlin.R

class TimePickerPopupDialogTwoButton(
    private val context: Context,
    private val TimePickerPopupDialogClickListener: TimePickerPopupDialogClickListener
) : Dialog(
    context
) {
    private var timePicker: TimePicker? = null
    private val tvTitle: TextView? = null
    private var tvNegative: TextView? = null
    private var tvPositive: TextView? = null
    private var text: String? = null
    private val title: String? = null
    private var setHourValue = 0
    private var setMinuteValue = 0
    fun setText(text: String?) {
        this.text = text
    }

    fun setHourValue(setHourValue: Int) {
        this.setHourValue = setHourValue
    }

    fun setMinuteValue(setMinuteValue: Int) {
        this.setMinuteValue = setMinuteValue
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timepicker_alert_dialog_two)
        timePicker = findViewById<View>(R.id.timepicker_alert_two) as TimePicker
        timePicker!!.setIs24HourView(true)
        timePicker!!.hour = setHourValue
        timePicker!!.minute = setMinuteValue
        timePicker!!.setOnTimeChangedListener { view, hourOfDay, minute ->
            setHourValue = hourOfDay
            setMinuteValue = minute
        }

        // popup dialog button event
        tvPositive = findViewById(R.id.time_btn_yes)
        tvPositive.setOnClickListener(View.OnClickListener { v: View? ->
            TimePickerPopupDialogClickListener.onPositiveClick(setHourValue, setMinuteValue)
            dismiss()
        })
        tvNegative = findViewById(R.id.time_btn_no)
        tvNegative.setOnClickListener(View.OnClickListener { v: View? ->
            TimePickerPopupDialogClickListener.onNegativeClick()
            dismiss()
        })
    }

    companion object {
        private val TAG: String? = friend_writeActivity.Companion.TAG
    }
}