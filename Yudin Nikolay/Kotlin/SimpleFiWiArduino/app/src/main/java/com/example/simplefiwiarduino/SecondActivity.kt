package com.example.simplefiwiarduino

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Находим кнопку по её ID
        val buttonPageLeft: ImageButton = findViewById(R.id.pageLeft)
        val buttonPageRight: ImageButton = findViewById(R.id.pageRight2)
        val buttonPageStart: ImageButton = findViewById(R.id.pageStart)

        val buttonAppQuit: ImageButton = findViewById(R.id.appQuit)

        buttonAppQuit.setOnClickListener {
            // Закрыть все активности и завершить приложение
            finishAffinity()
        }

        buttonPageLeft.setOnClickListener {
            val intent = Intent(this@SecondActivity, FirstActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        buttonPageRight.setOnClickListener {
            val intent = Intent(this@SecondActivity, VoskActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        // Устанавливаем слушатель клика на кнопку
        buttonPageStart.setOnClickListener {
            val intent = Intent(this@SecondActivity, MainActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }
    }
}