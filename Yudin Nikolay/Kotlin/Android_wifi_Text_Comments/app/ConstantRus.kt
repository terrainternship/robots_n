// Этот интерфейс Constant содержит три константы, которые представляют собой IP-адрес, 
// номер порта и ссылку на репозиторий. 
// Объект-компаньон используется для создания статического контекста, 
// который обеспечивает доступ к этим константам без необходимости создания экземпляра интерфейса.

// Определение пакета, к которому относится интерфейс
package com.tofaha.Android_wifi.app

/**
 * Интерфейс, содержащий константы для приложения
 */
interface Constant {

    // Вложенный объект-компаньон с константами
    companion object {

        // Константа для хранения IP-адреса
        val IP_ADDRESS = "ip_address"

        // Константа для хранения номера порта
        val PORT_NUMBER = "port_number"

        // Константа для хранения ссылки на репозиторий
        val REPO_LINK = "https://github.com/ayoubElhoucine/Connect-Android-to-Arduino-via-Wifi"
    }
}
