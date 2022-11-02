package com.smartherd.googlelens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import java.nio.file.Files.size


open class SearchRvAdapter() : RecyclerView.Adapter<SearchRvAdapter.ViewHolder>(), Parcelable {
    private lateinit var context:Context
    private lateinit var searchRvModel:ArrayList<SearchRvModel>

    constructor(parcel: Parcel) : this() {

    }

    constructor(context: Context, searchRvModel: ArrayList<SearchRvModel>) : this() {
        this.context = context
        this.searchRvModel = searchRvModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view:View = LayoutInflater.from(context).inflate(R.layout.search_results_rv_item,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: SearchRvAdapter.ViewHolder, position: Int) {
     val searchRvModel:SearchRvModel = searchRvModel.get(position)
        holder.title.setText(searchRvModel.title)
        holder.snippets.setText(searchRvModel.link)
        holder.desctxt.setText(searchRvModel.snippet)
        holder.itemView.setOnClickListener(){
            val i:Intent = Intent(Intent.ACTION_VIEW )
            i.setData(Uri.parse(searchRvModel.link))
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title =  itemView.findViewById<TextView>(R.id.texttitle)
        var snippets = itemView.findViewById<TextView>(R.id.textsnippet)
        var desctxt = itemView.findViewById<TextView>(R.id.textdescription)


    }

    override fun getItemCount(): Int {
        return searchRvModel.size
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchRvAdapter> {
        override fun createFromParcel(parcel: Parcel): SearchRvAdapter {
            return SearchRvAdapter(parcel)
        }

        override fun newArray(size: Int): Array<SearchRvAdapter?> {
            return arrayOfNulls(size)
        }
    }
}