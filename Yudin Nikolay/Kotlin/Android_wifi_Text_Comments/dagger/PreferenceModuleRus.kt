// Этот Dagger-модуль PreferenceModule предоставляет зависимость типа Pref 
// для управления настройками (SharedPreferences) в приложении.
// Аннотации Singleton, Module, и Provides используются для указания, 
// что объект Pref будет предоставлен как синглтон и как зависимость модуля Dagger. 

// Определение пакета, к которому относится модуль Dagger
package com.tofaha.Android_wifi.dagger

// Импорт классов из Android SDK
import android.content.Context

// Импорт класса Pref из указанного пакета
import com.tofaha.Android_wifi.Pref

// Импорт аннотаций Singleton, Module и Provides из Dagger
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger-модуль для предоставления зависимости типа Pref
 */
@Module
class PreferenceModule {

    /**
     * Метод, предоставляющий зависимость типа Pref
     * @param context Объект типа Context, необходимый для создания экземпляра Pref
     * @return Экземпляр типа Pref
     */
    @Singleton
    @Provides
    internal fun providePref(context: Context): Pref {
        return Pref(context)
    }

}
