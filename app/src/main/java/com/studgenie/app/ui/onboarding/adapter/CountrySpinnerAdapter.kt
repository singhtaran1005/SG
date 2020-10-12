package com.studgenie.app.ui.onboarding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.studgenie.app.R
import com.studgenie.app.data.model.CountryItem
import de.hdodenhof.circleimageview.CircleImageView

//class CountrySpinnerAdapter {
//}

class CountrySpinnerAdapter(val mContext: Context, val mData: ArrayList<CountryItem>) : BaseAdapter() {
    private val mInflate: LayoutInflater

    init {
        mInflate = LayoutInflater.from(mContext)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var view = convertView

        if (view == null) {
            view = mInflate.inflate(R.layout.item_country_code, parent, false)
            viewHolder = ViewHolder(view)
        } else {
            viewHolder = view.tag as ViewHolder
        }

        view?.tag = viewHolder

        viewHolder.selectedCountryName.text = mData[position].countryName
        viewHolder.selectedCountryFlag.setImageResource(mData[position].flagImage)

        return view!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: DropdownViewHolder
        var view = convertView

        if (view == null) {
            view = mInflate.inflate(R.layout.country_item, parent, false)
            viewHolder = DropdownViewHolder(view)
        } else {
            viewHolder = view.tag as DropdownViewHolder
        }

        view?.tag = viewHolder

        viewHolder.countryname.text = mData[position].countryName
        viewHolder.flag.setImageResource(mData[position].flagImage)

        return view!!
    }

    override fun getItem(position: Int): Any = mData[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = mData.size

    class ViewHolder(view: View) {
        var selectedCountryName: TextView = view.findViewById(R.id.selected_country_name)
        var selectedCountryFlag: ImageView = view.findViewById(R.id.selected_flag)

    }

    class DropdownViewHolder(view: View) {
        var countryname: TextView = view.findViewById(R.id.textView_country_name)
        var flag: CircleImageView = view.findViewById(R.id.imageView_flag)
    }
}