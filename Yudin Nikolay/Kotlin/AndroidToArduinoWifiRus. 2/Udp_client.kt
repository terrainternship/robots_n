// Этот класс создает UDP-сокет, отправляет данные на указанный IP-адрес и порт. 
// Он работает в бесконечном цикле, отправляя пакеты данных, основываясь на текущем направлении из MainActivity.

// Определение пакета, к которому относится класс
package com.example.robot

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * UDP-клиент для отправки данных на сервер
 */
class Udp_client : Thread() {
    // Счетчик для отслеживания состояния отправки нулевого пакета
    var i = 0

    // Байтовый массив для хранения данных, которые будут отправлены по UDP
    var data = byteArrayOf(0)

    // Порт для отправки данных
    var udp_port = 50000

    // Адрес сервера
    var addr: InetAddress? = null

    // DatagramSocket для отправки данных по UDP
    var ds: DatagramSocket? = null

    /**
     * Инициализация, создание DatagramSocket и InetAddress, запуск потока
     */
    init {
        try {
            ds = DatagramSocket()
            addr = InetAddress.getByName(HTTP_client.host_address)
        } catch (e: Exception) {
            // Обработка исключений, если возникают проблемы при создании сокета или получении адреса
        }
        start()
    }

    /**
     * Метод, выполняемый в отдельном потоке при старте
     */
    override fun run() {
        while (true) {
            // Получаем текущее направление из MainActivity
            val temp: Byte = MainActivity.direction
            var s = "" + MainActivity.direction
            // Преобразуем строку в байтовый массив
            data = s.toByteArray()

            // Если направление не равно 100, отправляем пакет с данными
            if (temp.toInt() != 100) {
                val pack = DatagramPacket(data, data.size, addr, udp_port)
                try {
                    ds!!.send(pack)
                    i = 0
                    sleep(200)
                } catch (e: Exception) {
                    // Обработка исключений, если возникают проблемы при отправке пакета
                }
            } else {
                // Если направление равно 100, отправляем пакет с нулевым значением
                if (i == 0) {
                    s = "" + 0
                    data = s.toByteArray()
                    val pack = DatagramPacket(data, data.size, addr, udp_port)
                    try {
                        ds!!.send(pack)
                        sleep(200)
                    } catch (e: Exception) {
                        // Обработка исключений, если возникают проблемы при отправке пакета с нулевым значением
                    }
                }
                // Перестаем отправлять нулевые пакеты
                i = 1
            }
        }
    }
}
