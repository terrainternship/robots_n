// Этот компонент Dagger, обозначенный аннотацией @Singleton, 
// предоставляет методы для внедрения зависимостей 
// в указанные классы (InfoActivity, MainActivity, FloatingMenuFragment).

// Определение пакета, к которому относится компонент Dagger
package com.tofaha.Android_wifi.dagger

// Импорт аннотации Singleton из пакета javax.inject
import javax.inject.Singleton

// Импорт аннотации Component из Dagger
import dagger.Component

/**
 * Компонент Dagger для внедрения зависимостей в приложение
 */
@Singleton
@Component(modules = [AppModule::class, PreferenceModule::class])
interface AppComponent {

    // Методы для внедрения зависимостей в указанные классы
    fun inject(infoActivity: InfoActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(target: FloatingMenuFragment)
}
