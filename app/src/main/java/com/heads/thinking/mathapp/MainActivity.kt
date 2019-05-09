package com.heads.thinking.mathapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), OnFragmentListener, DiffursFragment.PlotListener {

    var currentFragment : String = "MainFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        when(savedInstanceState?.getString("fragment", "MainFragment") ?: "MainFragment") {
            "MainFragment" -> {
                changeFragment(MainFragment())
                currentFragment = "MainFragment"
            }
            "IntegralsFragment" -> {
                changeFragment(IntegralsFragment())
                currentFragment = "IntegralsFragment"
            }
            "DiffursFragment" -> {
                changeFragment(DiffursFragment())
                currentFragment = "DiffursFragment"
            }
        }
    }

    override fun changeFragment(fragment: Fragment) {
        if(fragment is MainFragment) currentFragment = "MainFragment"
        if(fragment is IntegralsFragment) currentFragment = "IntegralsFragment"
        if(fragment is DiffursFragment) currentFragment = "DiffursFragment"
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame, fragment)
        ft.commit()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString("fragment", currentFragment)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun startGraphActivity(data: String, startPoint : String) {
        startActivity(Intent(this, GraphActivity::class.java).apply {
            this.putExtra("function", data)
            this.putExtra("startPoint", startPoint)
        })
    }
}
