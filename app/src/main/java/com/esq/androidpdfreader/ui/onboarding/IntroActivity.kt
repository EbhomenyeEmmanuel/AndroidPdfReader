package com.esq.androidpdfreader.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.esq.androidpdfreader.R

class IntroActivity : AppCompatActivity() {
    private val navController: NavController by lazy { findNavController(R.id.nav_host) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }

    private fun handlerMethod() {
        val SPLASH_TIME = 2000
        Handler().postDelayed({
            navController.navigate(R.id.action_introFragment_to_mainActivity)
            Log.i(this.javaClass.simpleName, "Navigation delayed for 4 secs")
        }, SPLASH_TIME.toLong())
    }

    override fun onResume() {
        super.onResume()
        handlerMethod()
    }
}