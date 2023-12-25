// Этот код представляет собой класс OpenConnection, 
// который используется для установки сетевого соединения с сервером по указанному IP-адресу и порту. 
// Метод doInBackground выполняет эту задачу в фоновом режиме.

// Определение пакета, к которому относится класс
package com.tofaha.Android_wifi.connection

// Импорт класса AsyncTask из Android SDK
import android.os.AsyncTask

// Импорт класса MyData из указанного пакета
import com.tofaha.Android_wifi.app.MyData

// Импорт класса IOException из стандартной библиотеки Java
import java.io.IOException

// Импорт класса Socket из стандартной библиотеки Java
import java.net.Socket

/**
 * Класс для асинхронного открытия сетевого соединения
 */
class OpenConnection(private val ipAddress: String, private val portNumber: Int) : AsyncTask<Void, String, Void>() {

    /**
     * Метод, выполняющий фоновую работу
     */
    override fun doInBackground(vararg voids: Void): Void? {
        try {
            // Установка соединения с сервером по указанному IP-адресу и порту
            MyData.socket = Socket(ipAddress, portNumber)
            // Вывод сообщения в консоль об успешном открытии соединения
            System.out.println("connection opened")

            // Проверка, запущен ли уже поток для приема сообщений
            if (MyData.THREAD_RUNNING == false) {
                // Если поток не запущен, запускаем метод receiveMessage() из MainActivity
                MyData.mainActivity.receiveMessage()
            }

        } catch (e: IOException) {
            // В случае возникновения исключения типа IOException
            e.printStackTrace() // Вывод информации об ошибке в консоль
        }

        return null // Возврат null, так как тип возвращаемого значения указан как Void?
    }
}
