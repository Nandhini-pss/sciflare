package com.example.sciflareapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sciflareapplication.R
import com.example.sciflareapplication.room.AppDatabase
import com.example.sciflareapplication.room.UserEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {
    private var adduserbtn: FloatingActionButton? = null
    private var userRecyclerview: RecyclerView? = null
    private var txt_nodata: TextView? = null
    var userlistAdapter:UserlistAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        adduserbtn = findViewById(R.id.adduserbtn)
        userRecyclerview=findViewById(R.id.rv_user)
        txt_nodata=findViewById(R.id.txt_nodata)

        userRecyclerview?.layoutManager= LinearLayoutManager(this)

        adduserbtn?.setOnClickListener {
            val intent = Intent(this, InsertUserActivity::class.java)
            startActivity(intent)
        }
        loaduser()
    }

    private fun loaduser() {
        val database=AppDatabase.getDatabase(this)
        val userDao= database.userDao()
        GlobalScope.launch {
            val userList=userDao?.getUsers()
            withContext(Dispatchers.Main){
                if (userList != null) {
                    loadUserListAdapter(userList)
                }
            }
        }
    }

    private fun loadUserListAdapter(userList: List<UserEntity>) {
        if(userList.size>0){
            userRecyclerview?.visibility= View.VISIBLE
            txt_nodata?.visibility= View.GONE

            userlistAdapter= UserlistAdapter(userList){ id, type->
                if (type.equals("Delete")){
                    removeUserListDetails(id)
                }else{
                    updateItem(id)
                }


            }
            userRecyclerview?.adapter= userlistAdapter

        }else{
            userRecyclerview?.visibility= View.GONE
            txt_nodata?.visibility= View.VISIBLE
        }
    }

    private fun updateItem(id: String) {
        val intent = Intent(this, InsertUserActivity::class.java)
        intent.putExtra("ComingFrom", "Update")
        intent.putExtra("SelectValue", id)
        startActivity(intent)
    }


    private fun removeUserListDetails(id: String) {
        val database=AppDatabase.getDatabase(this)
        val userDao=database?.userDao()
        GlobalScope.launch {
            userDao?.deleteById(id)
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "user removed", Toast.LENGTH_SHORT).show()
                loaduser()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loaduser()
    }
    override fun onStart() {
        super.onStart()
        loaduser()
    }

}