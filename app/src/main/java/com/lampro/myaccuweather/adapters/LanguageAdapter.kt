package com.lampro.myaccuweather.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.utils.PrefManager

class LanguageAdapter : ArrayAdapter<String>{
    constructor(context: Context, resource: Int, objects: MutableList<String>) : super(
        context,
        resource,
        objects
    )



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_selected,parent,false)
        val textView = convertView.findViewById<TextView>(R.id.tvSelected)
        var lang = this.getItem(position)
        if (lang != null) {
            if (lang == "Vietnamese" || lang == "Tiếng việt") {
                textView.setText("VI")
            }else{
                textView.setText("EN")
            }
        }
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.dropdown_item,parent,false)
        val textView = convertView.findViewById<TextView>(R.id.tvDropItem)
        var lang = this.getItem(position)
        if (lang != null) {
            textView.setText(lang)
        }
        return convertView
    }
}