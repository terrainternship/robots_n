// Этот код представляет собой активити InfoActivity, 
// отвечающую за ввод информации (IP-адреса и номера порта) перед переходом к главной активити. 
// Используются аннотации @BindView и библиотека ButterKnife для удобной работы с представлениями. 
// Dagger используется для внедрения зависимости Pref. 

// Определение пакета, к которому относится активити
package com.tofaha.Android_wifi.ui

// Импорт классов из Android SDK
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast

// Импорт классов из указанных пакетов
import com.tofaha.Android_wifi.Pref
import com.tofaha.Android_wifi.R
import com.tofaha.Android_wifi.Util
import com.tofaha.Android_wifi.app.TofahaApplication

// Импорт аннотаций и библиотеки для работы с DI (Dagger) и ButterKnife
import javax.inject.Inject
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tofaha.Android_wifi.ui.main.MainActivity

/**
 * Активити для ввода информации (IP-адреса и номера порта)
 */
class InfoActivity : AppCompatActivity() {

    // Поле для представления ввода IP-адреса
    @BindView(R.id.ip_address)
    lateinit var ipAddress: EditText

    // Поле для представления ввода номера порта
    @BindView(R.id.port_number)
    lateinit var portNumber: EditText

    // Поле для внедрения зависимости Pref
    @Inject
    lateinit var pref: Pref

    /**
     * Метод, вызываемый при создании активити
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Внедрение зависимости Pref с использованием Dagger
        (this.application as TofahaApplication).getAppComponent()!!.inject(this)

        // Привязка представлений с использованием ButterKnife
        ButterKnife.bind(this)

        // Установка цвета фона ActionBar и статус-бара
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue_menu)))
        window.statusBarColor = resources.getColor(R.color.blue_menu)

        // Проверка, есть ли сохраненный IP-адрес и номер порта
        if (Util.isValidIp(pref!!.ipAddress!!) && pref!!.portNumber != 0) {
            startMainActivity()
        }
    }

    /**
     * Обработчик нажатия на кнопку "Next"
     */
    @OnClick(R.id.login_next)
    fun next() {

        // Проверка валидности введенного IP-адреса
        if (Util.isValidIp(ipAddress!!.text.toString())) {
            pref!!.ipAddress = ipAddress!!.text.toString()
        } else {
            Toast.makeText(this, "Enter valid IP address", Toast.LENGTH_SHORT).show()
            return
        }

        // Проверка введенного номера порта
        if (portNumber!!.text.toString() != "") {
            pref!!.portNumber = Integer.parseInt(portNumber!!.text.toString())
        } else {
            Toast.makeText(this, "Enter port number", Toast.LENGTH_SHORT).show()
            return
        }

        // Переход к главной активити
        startMainActivity()
    }

    /**
     * Метод для запуска главной активити
     */
    fun startMainActivity() {
        val intent = Intent(this@InfoActivity, MainActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}