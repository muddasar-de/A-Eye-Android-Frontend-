package com.muddasarajmal.a_eye_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdapter (var mCtx: Context, var resources:Int, var items:List<Model>): ArrayAdapter<Model>(mCtx,resources,items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources,null)

        val iconView: ImageView = view.findViewById(R.id.listIcon)
        val headingView: TextView = view.findViewById(R.id.listHeading)
        val desView: TextView = view.findViewById(R.id.listDescription)

        var mItem:Model= items[position]
        iconView.setImageDrawable(mCtx.resources.getDrawable(mItem.icon))
        headingView.text = mItem.listItemHeading
        desView.text = mItem.listItemDescription

        return view
    }
}