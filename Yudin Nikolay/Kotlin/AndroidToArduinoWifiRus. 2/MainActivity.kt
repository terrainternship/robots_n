// Этот код создает интерфейс с четырьмя кнопками для управления роботом. 
// Кнопки управления назначают значения переменной direction в соответствии с направлением движения робота. 
// Нажатие и отпускание кнопок обрабатываются через события OnTouchListener.

// Определение пакета, к которому относится класс
package com.example.robot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.Toast

/**
 * Главная активность приложения для управления роботом
 */
class MainActivity : AppCompatActivity() {

    // Кнопки для управления направлением робота
    private lateinit var mButtonUP: ImageButton
    private lateinit var mButtonDOWN: ImageButton
    private lateinit var mButtonLEFT: ImageButton
    private lateinit var mButtonRIGHT: ImageButton

    // Статическая переменная для передачи направления в Udp_client
    companion object {
        var direction: Byte = 100
    }

    /**
     * Метод, вызываемый при создании активности
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация HTTP-клиента и UDP-клиента
        HTTP_client(40000)
        Udp_client()

        // Инициализация кнопок управления
        mButtonUP = findViewById(R.id.imageButtonUP)
        mButtonUP.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                direction = 3
            }
            if (event.action == MotionEvent.ACTION_UP) {
                direction = 100
            }
            false
        }

        mButtonDOWN = findViewById(R.id.imageButtonDown)
        mButtonDOWN.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                direction = 4
                Toast.makeText(this@MainActivity, "вниз $direction", Toast.LENGTH_SHORT).show()
            }
            if (event.action == MotionEvent.ACTION_UP) {
                direction = 100
            }
            false
        }

        mButtonLEFT = findViewById(R.id.imageButtonLeft)
        mButtonLEFT.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                direction = 1
                Toast.makeText(this@MainActivity, "влево $direction", Toast.LENGTH_SHORT).show()
            }
            if (event.action == MotionEvent.ACTION_UP) {
                direction = 100
            }
            false
        }

        mButtonRIGHT = findViewById(R.id.imageButtonRight)
        mButtonRIGHT.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                direction = 2
                Toast.makeText(this@MainActivity, "вправо $direction", Toast.LENGTH_SHORT).show()
            }
            if (event.action == MotionEvent.ACTION_UP) {
                direction = 100
            }
            false
        }
    }
}
