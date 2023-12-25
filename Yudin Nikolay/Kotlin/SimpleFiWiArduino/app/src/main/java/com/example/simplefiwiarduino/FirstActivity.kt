package com.example.simplefiwiarduino

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        // Находим кнопку по её ID
        val buttonPageEnd: ImageButton = findViewById(R.id.pageEnd)
        val buttonPageLeft: ImageButton = findViewById(R.id.pageLeft)
        val buttonPageRight: ImageButton = findViewById(R.id.pageRight2)
        //val buttonPageStart: ImageButton = findViewById(R.id.pageStart)

        val buttonAppQuit: ImageButton = findViewById(R.id.appQuit)

        // Устанавливаем не доступность левых кнопок
        //buttonPageStart.isEnabled = false;
        //buttonPageStart.setBackgroundResource(R.drawable.leftdinactive96x86)
        //buttonPageLeft.isEnabled = false;
        //buttonPageLeft.setBackgroundResource(R.drawable.leftinactive96x86)

        buttonAppQuit.setOnClickListener {
            // Закрыть все активности и завершить приложение
            finishAffinity()
        }

        buttonPageLeft.setOnClickListener {
            val intent = Intent(this@FirstActivity, MainActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        buttonPageRight.setOnClickListener {
            val intent = Intent(this@FirstActivity, SecondActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        // Устанавливаем слушатель клика на кнопку
        buttonPageEnd.setOnClickListener {
            val intent = Intent(this@FirstActivity, VoskActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }
    }
}