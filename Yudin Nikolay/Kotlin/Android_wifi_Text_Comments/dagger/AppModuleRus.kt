// Этот Dagger-модуль AppModule предоставляет зависимость типа Context для приложения. 
// Аннотации Singleton, Module, и Provides используются для указания, 
// что объект Context будет предоставлен как синглтон и как зависимость модуля Dagger. 

// Определение пакета, к которому относится модуль Dagger
package com.tofaha.Android_wifi.dagger

// Импорт классов из Android SDK
import android.app.Application
import android.content.Context

// Импорт аннотаций Singleton, Module и Provides из Dagger
import javax.inject.Singleton
import dagger.Module
import dagger.Provides

/**
 * Dagger-модуль для предоставления зависимости типа Context
 */
@Module
class AppModule(private val application: Application) {

    /**
     * Метод, предоставляющий зависимость типа Context
     * @return Объект типа Context
     */
    @Singleton
    @Provides
    fun provideContext(): Context {
        return this.application
    }
}
