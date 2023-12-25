package com.example.simplefiwiarduino

import android.os.Bundle
import android.content.Intent
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим кнопку по её ID
        //val buttonPageEnd: ImageButton = findViewById(R.id.pageEnd)
        //val buttonPageLeft: ImageButton = findViewById(R.id.pageLeft)
        //val buttonPageRight: ImageButton = findViewById(R.id.pageright)
        //val buttonPageStart: ImageButton = findViewById(R.id.pageStart)

        val buttonPageManual: ImageButton = findViewById(R.id.imageButtonManual)
        val buttonPageGiroscop: ImageButton = findViewById(R.id.imageButtonGiroscop)
        val buttonPageVoice: ImageButton = findViewById(R.id.imageButtonVoice)
        val buttonPropConnectWiFi: ImageButton = findViewById(R.id.propConnectWiFi)
        val buttonAppQuit: ImageButton = findViewById(R.id.appQuit)
        val buttonConnectWiFi: ImageButton = findViewById(R.id.connectWiFi)

        // Устанавливаем не доступность левых кнопок
        //buttonPageStart.isEnabled = false;
        //buttonPageStart.setBackgroundResource(R.drawable.leftdinactive96x86)
        //buttonPageLeft.isEnabled = false;
        //buttonPageLeft.setBackgroundResource(R.drawable.leftinactive96x86)

        // Устанавливаем слушатель клика на кнопку
        //buttonPageEnd.setOnClickListener {
            // Ваш код обработки нажатия здесь



        //}

        buttonPageManual.setOnClickListener {
            val intent = Intent(this@MainActivity, FirstActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        buttonPageGiroscop.setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        buttonPageVoice.setOnClickListener {
            val intent = Intent(this@MainActivity, VoskActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        buttonPropConnectWiFi.setOnClickListener {
            val intent = Intent(this@MainActivity, ThirdActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        buttonAppQuit.setOnClickListener {
            // Закрыть все активности и завершить приложение
            finishAffinity()
        }
    }
}
