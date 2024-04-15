package com.example.sciflareapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sciflareapplication.R
import com.example.sciflareapplication.room.UserEntity

class UserlistAdapter(val userlist: List<UserEntity>,var OnMenuClick: (String,String) -> Unit) :
    RecyclerView.Adapter<UserlistAdapter.viewHolder>() {
    class viewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val setname = itemView.findViewById<TextView>(R.id.setname)
        val setemail = itemView.findViewById<TextView>(R.id.setemail)
        val setmobile = itemView.findViewById<TextView>(R.id.setmobile)
        val setgender = itemView.findViewById<TextView>(R.id.setgender)
        var edituser=itemView.findViewById<View>(R.id.edituser) as ImageView
        var deleteuser=itemView.findViewById<View>(R.id.deleteuser) as ImageView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.userlist_layout, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val userDetails=userlist[position]
        holder.setname.text = userlist.get(position).name
        holder.setemail.text = userlist.get(position).email
        holder.setmobile.text = userlist.get(position).mobile
        holder.setgender.text = userlist.get(position).gender

        holder.edituser.setOnClickListener {
            OnMenuClick.invoke(userDetails._id,"Edit")
        }
        holder.deleteuser.setOnClickListener {
            OnMenuClick.invoke(userDetails._id,"Delete")
        }

    }
}