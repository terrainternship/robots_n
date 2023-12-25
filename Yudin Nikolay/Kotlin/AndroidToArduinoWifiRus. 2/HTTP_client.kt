// В этом коде создается клиент для отправки HTTP-запроса на сервер. 
// После установления соединения и отправки запроса, 
// клиент ждет ответ от сервера и, в случае получения "ready", запускает Udp_client.

// Определение пакета, к которому относится класс
package com.example.robot

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

/**
 * Клиент для HTTP-сервера
 *
 * @param port Порт, на котором работает HTTP-сервер
 */
class HTTP_client internal constructor(var port: Int) : Thread() {
    // Строка для отправки на сервер
    var s: String? = null

    // Приветствие от сервера
    var Greetings_from_S: String? = null

    // Инициализация, запускает поток
    init {
        start()
    }

    /**
     * Метод, выполняемый в отдельном потоке при старте
     */
    override fun run() {
        try {
            // Установка соединения с сервером по указанному порту
            Socket(host_address, port).use { socket ->
                // Создание PrintWriter для отправки данных на сервер
                val pw =
                    PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)
                // Создание BufferedReader для приема данных от сервера
                val br =
                    BufferedReader(InputStreamReader(socket.getInputStream()))

                // Отправка запроса на сервер ("data" - пример запроса, замените на ваш)
                pw.println("stop data\r") // на всякий случай, вдруг вышли некорректно
                pw.println("data\r") // Приветствие сервера
                Greetings_from_S = br.readLine()

                // Если сервер ответил "ready", то запускаем Udp_client
                if (Greetings_from_S == "ready") {
                    Udp_client()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        // Адрес хоста (замените на свой)
        var host_address = "192.168.1.138"
    }
}
