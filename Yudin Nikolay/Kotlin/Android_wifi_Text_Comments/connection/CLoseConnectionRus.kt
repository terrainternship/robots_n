// Класс CLoseConnection используется для асинхронного закрытия сетевого соединения, 
// предположительно представленного сокетом. Метод doInBackground выполняет закрытие сокета, 
// и при возникновении исключения IOException выводит информацию об ошибке в консоль.

// Определение пакета, к которому относится класс
package com.tofaha.Android_wifi.connection

// Импорт класса AsyncTask из Android SDK
import android.os.AsyncTask

// Импорт класса MyData из указанного пакета
import com.tofaha.Android_wifi.app.MyData

// Импорт класса IOException из стандартной библиотеки Java
import java.io.IOException

/**
 * Класс для асинхронного закрытия сетевого соединения
 */
class CLoseConnection : AsyncTask<Void, String, Void>() {

    /**
     * Метод, выполняющий фоновую работу
     */
    override fun doInBackground(vararg params: Void?): Void? {
        try {
            // Закрытие сокета (предположительно, сетевого соединения)
            MyData.socket?.close()
            // Вывод сообщения в консоль об успешном закрытии соединения
            System.out.println("connection closed")
        } catch (e: IOException) {
            // В случае возникновения исключения типа IOException
            e.printStackTrace() // Вывод информации об ошибке в консоль
        }

        return null // Возврат null, так как тип возвращаемого значения указан как Void?
    }
}
