package com.ctpi.senasoftcauca

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ctpi.senasoftcauca.auth.AuthActivity

class Splassh : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splassh)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)

        }, 2500)


    }
}