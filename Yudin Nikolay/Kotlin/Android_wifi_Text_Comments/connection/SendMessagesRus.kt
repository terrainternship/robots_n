// Этот код представляет собой класс SendMessages, 
// который используется для асинхронной отправки сообщений по уже установленному сетевому соединению. 
// Метод doInBackground выполняет эту задачу в фоновом режиме.

// Определение пакета, к которому относится класс
package com.tofaha.Android_wifi.connection

// Импорт класса AsyncTask из Android SDK
import android.os.AsyncTask

// Импорт класса MyData из указанного пакета
import com.tofaha.Android_wifi.app.MyData

// Импорт класса BufferedWriter из стандартной библиотеки Java
import java.io.BufferedWriter

// Импорт класса IOException из стандартной библиотеки Java
import java.io.IOException

// Импорт класса OutputStreamWriter из стандартной библиотеки Java
import java.io.OutputStreamWriter

// Импорт класса PrintWriter из стандартной библиотеки Java
import java.io.PrintWriter

// Импорт класса Socket из стандартной библиотеки Java
import java.net.Socket

/**
 * Класс для асинхронной отправки сообщений по установленному сетевому соединению
 */
class SendMessages(msg: String) : AsyncTask<Void, String, Void>() {

    // Поле для хранения отправляемого сообщения
    internal var msg = ""

    // Конструктор класса, принимающий сообщение и инициализирующий поле
    init {
        this.msg = msg
    }

    /**
     * Метод, выполняющий фоновую работу
     */
    override fun doInBackground(vararg voids: Void): Void? {
        try {
            // Создание объекта PrintWriter для отправки данных через сетевое соединение
            val out = PrintWriter(BufferedWriter(OutputStreamWriter(MyData.socket
                    .getOutputStream())), true)

            // Отправка сообщения по сетевому соединению
            out.println(msg)
            
            // Вывод сообщения в консоль об успешной отправке сообщения
            println("message send")

        } catch (e: IOException) {
            // В случае возникновения исключения типа IOException
            e.printStackTrace() // Вывод информации об ошибке в консоль
        }

        return null // Возврат null, так как тип возвращаемого значения указан как Void?
    }
}
