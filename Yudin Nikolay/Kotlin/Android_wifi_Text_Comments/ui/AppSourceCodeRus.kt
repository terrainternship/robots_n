// Этот код представляет собой активити AppSourceCode, отображающую исходный код приложения. 
// В активити устанавливаются цвет статус-бара и фон ActionBar, 
// а также добавлен обработчик клика по кнопке my_repo_link для открытия ссылки на репозиторий в браузере.

// Определение пакета, к которому относится активити
package com.tofaha.Android_wifi.ui

// Импорт классов из Android SDK
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity

// Импорт класса Constant из указанного пакета
import com.tofaha.Android_wifi.app.Constant
import com.tofaha.Android_wifi.R

// Импорт аннотации RequiresApi из Android SDK
import kotlinx.android.synthetic.main.activity_app_source_code.*

/**
 * Активити для просмотра исходного кода приложения
 */
class AppSourceCode : AppCompatActivity() {

    /**
     * Метод, вызываемый при создании активити
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_source_code)

        // Установка цвета статус-бара и фона ActionBar
        window.statusBarColor = resources.getColor(R.color.blue_menu_button)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue_menu_button)))

        // Обработчик клика по кнопке для открытия ссылки на репозиторий в браузере
        my_repo_link.setOnClickListener({
            val browser = Intent(Intent.ACTION_VIEW, Uri.parse(Constant.REPO_LINK))
            startActivity(browser)
        })
    }
}
