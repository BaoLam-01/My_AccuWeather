package com.lampro.myaccuweather.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.lampro.myaccuweather.R

class UnitsAdapter : ArrayAdapter<String> {
    constructor(context: Context, resource: Int, objects: MutableList<String>) : super(
        context,
        resource,
        objects
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_selected, parent, false)
        val textView = view.findViewById<TextView>(R.id.tvSelected)
        val units = this.getItem(position)
        units?.let { textView.text = it }
        return view

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.tvDropItem)
        var units = this.getItem(position)
        units?.let {

            textView.text = it
        }
        return view
    }
}