// Этот объект MyData служит для хранения общих данных для приложения. 
// Включает в себя переменные для сокета (socket), экземпляра MainActivity (mainActivity), 
// и флага THREAD_RUNNING, который отслеживает состояние работы потока. 
// Переменные socket и mainActivity инициализируются позже в коде (вероятно, в другом месте приложения).

// Определение пакета, к которому относится объект MyData
package com.tofaha.Android_wifi.app

// Импорт класса MainActivity из указанного пакета
import com.tofaha.Android_wifi.ui.main.MainActivity

// Импорт класса Socket из стандартной библиотеки Java
import java.net.Socket

/**
 * Объект MyData, содержащий общие данные для приложения
 */
object MyData {

    // Переменная для хранения объекта сокета
    lateinit var socket: Socket

    // Переменная для хранения объекта MainActivity
    lateinit var mainActivity: MainActivity

    // Переменная для отслеживания состояния работы потока
    var THREAD_RUNNING = false
}
