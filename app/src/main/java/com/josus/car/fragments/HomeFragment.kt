package com.josus.car.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.josus.car.R


class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent= Intent()
        val user=intent.extras?.getString("currentUser")
        val txtUser: TextView =view.findViewById(R.id.txtUser)
        txtUser.text=user ?: "null"
    }
    /*
      override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)

    }


    override fun onOptionsItemSelected(item: MenuItem)=when(item.itemId){
        R.id.log_out ->{
            Firebase.auth.signOut()
            true
        }
        else->  super.onOptionsItemSelected(item)
    }
     */

}