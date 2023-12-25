// Этот класс предназначен для управления жизненным циклом приложения и инициализации Dagger-компонента, 
// который используется для внедрения зависимостей в приложение. 
// Dagger - это инструмент для внедрения зависимостей (DI) в Android-приложения, 
// и данное приложение использует его для создания и управления компонентами.

// Определение пакета, к которому относится класс
package com.tofaha.Android_wifi.app

// Импорт базового класса Application из Android SDK
import android.app.Application

// Импорт классов для Dagger (компоненты и модули)
import com.tofaha.Android_wifi.dagger.AppComponent
import com.tofaha.Android_wifi.dagger.AppModule
import com.tofaha.Android_wifi.dagger.DaggerAppComponent

/**
 * Класс TofahaApplication, представляющий приложение
 */
class TofahaApplication : Application() {

    // Переменная для хранения компонента Dagger
    private var appComponent: AppComponent? = null

    /**
     * Метод, вызываемый при создании приложения
     */
    override fun onCreate() {
        super.onCreate()

        // Инициализация Dagger-компонента приложения
        appComponent = initDagger(this)
    }

    /**
     * Метод для инициализации Dagger-компонента
     */
    protected fun initDagger(application: TofahaApplication): AppComponent {
        // Создание и настройка Dagger-компонента с использованием AppModule
        return DaggerAppComponent.builder().appModule(AppModule(application)).build()
    }

    /**
     * Метод для получения компонента Dagger
     */
    fun getAppComponent(): AppComponent? {
        return this.appComponent
    }
}
