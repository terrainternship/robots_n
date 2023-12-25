// Этот класс Pref предназначен для управления SharedPreferences в Android-приложении. 
// Он предоставляет свойства для чтения и записи IP-адреса и номера порта, 
// используя SharedPreferences для хранения этих данных. 
// Конструктор класса инициализирует объект SharedPreferences с использованием контекста приложения.

// Определение пакета, к которому относится класс
package com.tofaha.Android_wifi

// Импорт классов из Android SDK
import android.content.Context
import android.content.SharedPreferences

// Импорт интерфейса Constant из указанного пакета
import com.tofaha.Android_wifi.app.Constant

/**
 * Класс Pref для управления SharedPreferences (настроек приложения)
 * @param context Контекст приложения
 */
class Pref(private val context: Context) {
    
    // Объект для работы с SharedPreferences
    private val preferences: SharedPreferences

    // Свойство для доступа к сохраненному IP-адресу
    var ipAddress: String?
        get() = this.preferences.getString(Constant.IP_ADDRESS, "non")
        set(ipAddress) = this.preferences.edit().putString(Constant.IP_ADDRESS, ipAddress).apply()

    // Свойство для доступа к сохраненному номеру порта
    var portNumber: Int
        get() = this.preferences.getInt(Constant.PORT_NUMBER, 0)
        set(portNumber) = this.preferences.edit().putInt(Constant.PORT_NUMBER, portNumber).apply()

    /**
     * Инициализация класса, инициализация объекта SharedPreferences
     */
    init {
        this.preferences = context.getSharedPreferences(context.packageName, 0)
    }
}
