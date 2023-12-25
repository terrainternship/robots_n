package com.example.simplefiwiarduino

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        // Находим кнопку по её ID
        val buttonPageStart: ImageButton = findViewById(R.id.pageStart)

        val buttonAppQuit: ImageButton = findViewById(R.id.appQuit)

        buttonAppQuit.setOnClickListener {
            // Закрыть все активности и завершить приложение
            finishAffinity()
        }

        buttonPageStart.setOnClickListener {
            val intent = Intent(this@ThirdActivity, MainActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }
    }

}