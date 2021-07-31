package com.josus.car.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.josus.car.R
import com.josus.car.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)

        val user=intent.extras?.getString("currentUser")
       binding.txtUser.text=user
       // val bottomNav=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
       // val navHost=findViewById<NavHostFragment>(R.id.mainNavHostFragment)
     //   bottomNav.setupWithNavController()

      //  binding.bottomNavigationView.setupWithNavController(binding.mainNavHostFragment.findNavController())
       // bottomNavigationView.setupWithNavController(mainNavHostFragment.findNavController())
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem)=when(item.itemId){
        R.id.log_out ->{
            Firebase.auth.signOut()
            finishAffinity()
            true
        }
        else->  super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        finishAffinity()
    }



}