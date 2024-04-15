package com.example.sciflareapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sciflareapplication.R
import com.example.sciflareapplication.model.UserReq
import com.example.sciflareapplication.room.AppDatabase
import com.example.sciflareapplication.room.UserEntity
import com.example.sciflareapplication.utils.ApiService
import com.example.sciflareapplication.utils.UserRepository
import com.example.sciflareapplication.viewmodel.UserViewModel
import com.example.sciflareapplication.viewmodel.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// InsertUserActivity.kt
class InsertUserActivity : AppCompatActivity() {

    private lateinit var edt_name: EditText
    private lateinit var edt_email: EditText
    private lateinit var edt_mobile: EditText
    private lateinit var btnSave: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var radio_male: RadioButton
    private lateinit var radio_female: RadioButton
    private lateinit var viewModel: UserViewModel
    private var lastCheckedId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_user)

        val comingFrom = intent.getStringExtra("ComingFrom")
        val id = intent.getStringExtra("SelectValue")


        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://crudcrud.com/api/772f3128d88e4edfbe4460fc67e59eeb/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val appDatabase = AppDatabase.getDatabase(this)
        val userDao = appDatabase.userDao()

        val userRepository = UserRepository(apiService, userDao)
        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        ).get(UserViewModel::class.java)

        viewModel.userData.observe(this, Observer { users ->
            // Update UI or perform any actions with the list of users
            // For example, you can log the list of users
            Log.d("InsertUserActivity", "Users from database: $users")
        })

        radioGroup = findViewById(R.id.radio_group_gender)
        edt_email = findViewById(R.id.edt_email)
        edt_name = findViewById(R.id.edt_name)
        edt_mobile = findViewById(R.id.edt_moile)
        btnSave = findViewById(R.id.btnSave)
        radio_male = findViewById(R.id.radio_male)
        radio_female = findViewById(R.id.radio_female)


        if (comingFrom.equals("Update")) {
            btnSave.text = "Update"
            loadUserDetails(id.toString())
            println("hellodata"+id.toString())

        } else {
            btnSave.text = "Save"
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == lastCheckedId) {
                radioGroup.clearCheck()
            } else {
                lastCheckedId = checkedId
            }
        }

        btnSave.setOnClickListener {
            if (comingFrom.equals("Update")) {
                val name = edt_name.text.toString().trim()
                val email = edt_email.text.toString().trim()
                val mobile = edt_mobile.text.toString().trim()
                val gender = when (lastCheckedId) {
                    R.id.radio_male -> "Male"
                    R.id.radio_female -> "Female"
                    else -> ""
                }
                val idString = id.toString()
                val database = AppDatabase.getDatabase(this)
                val userDao = database.userDao()
                GlobalScope.launch {
                    userDao.updateUserData(
                        name,
                        email,
                        mobile,
                        gender,
                        idString
                    )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "User Details Update Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }


                }
            } else {
                btnSave.isEnabled = false

                val name = edt_name.text.toString().trim()
                val email = edt_email.text.toString().trim()
                val mobile = edt_mobile.text.toString().trim()
                val gender = when (lastCheckedId) {
                    R.id.radio_male -> "Male"
                    R.id.radio_female -> "Female"
                    else -> ""
                }

                var isValid = true

                if (name.isEmpty()) {
                    edt_name.error = "Name cannot be empty"
                    isValid = false
                }

                if (email.isEmpty()) {
                    edt_email.error = "Email cannot be empty"
                    isValid = false
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edt_email.error = "Invalid email format"
                    isValid = false
                }

                if (mobile.isEmpty()) {
                    edt_mobile.error = "Mobile number cannot be empty"
                    isValid = false
                } else if (!Patterns.PHONE.matcher(mobile).matches()) {
                    edt_mobile.error = "Invalid mobile number"
                    isValid = false
                }

                if (gender.isEmpty()) {
                    isValid = false
                }

                if (isValid) {
                    val user = UserReq(name, email, mobile, gender)
                    viewModel.postDataAndStoreInDatabase(user)
                    Toast.makeText(this, "User Details Added Successfully", Toast.LENGTH_SHORT)
                        .show()

                    Log.d("InsertUserActivity", "Sending user data to API: $user")

                    edt_name.setText("")
                    edt_email.setText("")
                    edt_mobile.setText("")
                   // startActivity(Intent(this, DashboardActivity::class.java))

                }

                btnSave.isEnabled = true
            }

        }
    }

    private fun loadUserDetails(id: String) {
        val database = AppDatabase.getDatabase(this)
        val userDao = database?.userDao()
        GlobalScope.launch {
            val userData = userDao?.getParticularUserDetails(id)
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "DATA LOADED", Toast.LENGTH_SHORT).show()
                // Toast.makeText(this, "DATA LOADED", Toast.LENGTH_SHORT).show()
                bindUserItem(userData)
            }
        }

    }

    private fun bindUserItem(userData: UserEntity?) {
        edt_name.setText(userData?.name)
        edt_email.setText(userData?.email)
        edt_mobile.setText(userData?.mobile)
        when (userData?.gender) {
            "Male" -> radio_male.isChecked = true
            "Female" -> radio_female.isChecked = true
        }
    }


}

