package com.example.simplefiwiarduino

import com.google.gson.JsonParser

// Объявим класс для возврата четырех значений из функции
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

object UtilsKot {

    // Извлекаем по ключу "text" значение строки
    fun getTextFromJson(jsonString: String): String? {
        try {
            // Парсинг JSON-строки
            val json = JsonParser.parseString(jsonString).asJsonObject

            // Извлечение значения по ключу "text"
            return json.getAsJsonPrimitive("text")?.asString
        } catch (e: Exception) {
            // Обработка возможных ошибок при парсинге JSON
            e.printStackTrace()
            return ""
        }
    }

    // Преобразование части числа или всего числа в целое представление
    //@JvmStatic
    private fun wordsToInt(aWords: List<String>): Triple<Int, String, String> {
        // Словарь числовых представлений прописи числа до 999
        val numeric = mapOf(
            "ноль" to 0, "один" to 1, "два" to 2, "три" to 3,
            "четыре" to 4, "пять" to 5, "шесть" to 6,
            "семь" to 7, "восемь" to 8, "девять" to 9,
            "десять" to 10, "одиннадцать" to 11, "двенадцать" to 12,
            "тринадцать" to 13, "четырнадцать" to 14, "пятнадцать" to 15,
            "шестнадцать" to 16, "семнадцать" to 17, "восемнадцать" to 18,
            "девятнадцать" to 19, "двадцать" to 20, "тридцать" to 30,
            "сорок" to 40, "пятьдесят" to 50, "шестьдесят" to 60,
            "семьдесят" to 70, "восемьдесят" to 80, "девяносто" to 90,
            "сто" to 100, "двести" to 200, "триста" to 300,
            "четыреста" to 400, "пятьсот" to 500, "шестьсот" to 600,
            "семьсот" to 700, "восемьсот" to 800, "девятьсот" to 900
        )

        val wordsLower = aWords.map { it.lowercase() }
        var totalNumber = 0
        var currentNumber = 0
        var prefDigit = 1
        var prefDigitStr = ""
        var sufDigitStr = ""

        for (word in wordsLower) {
            if (numeric.containsKey(word)) {
                currentNumber += numeric[word]!!
                prefDigit = 0
            } else {
                totalNumber += currentNumber
                currentNumber = 0

                if (prefDigit == 1) {
                    prefDigitStr = "$prefDigitStr $word"
                } else {
                    sufDigitStr = "$sufDigitStr $word"
                }
            }
        }

        totalNumber += currentNumber
        return Triple(totalNumber, prefDigitStr.trim(), sufDigitStr.trim())
    }

    // Переводим число прописью в цифровое представление
    //@JvmStatic
    private fun wordsToNumber(words: String): Quadruple<Int, Int, String, String>  {
        // Разделяем число на целую и дробную часть
        val wordsLower = words.lowercase()
        val wordsList = wordsLower.split(" ")

        val indexCel = wordsList.indexOf("целых")
        val integerPartWords = if (indexCel != -1) wordsList.subList(0, indexCel) else wordsList
        val decimalPartWords =
            if (indexCel != -1) wordsList.subList(indexCel + 1, wordsList.size) else emptyList()

        // Получаем целую и дробную часть числа
        val (integerPart, prefPart, sufPart) = wordsToInt(integerPartWords)
        val (decimal, _, sufPartDecimal) = wordsToInt(decimalPartWords)

        // В зависимости, какое значение непустое
        val sufPartEnd = if (sufPart != "") sufPart else sufPartDecimal

        return Quadruple(integerPart, decimal,  prefPart, sufPartEnd)
    }

    // Переводим число прописью в цифровое представление
    //@JvmStatic
    fun russianWordsToNumber(words: String): String {

        // Получаем целую и дробную часть числа
        val (integerPart, decimal, prefPart, sufPartEnd) = wordsToNumber(words)

        return if (decimal != 0) {
            "$prefPart $integerPart.$decimal $sufPartEnd".trim()
        } else {
            if (integerPart != 0) {
                "$prefPart $integerPart $sufPartEnd".trim()
            } else {
                prefPart.trim()
            }
        }
    }

    // Переводим число прописью в цифровое представление
    //@JvmStatic
    fun russianWordsToNumberCode(words: String): String {

        // Получаем целую и дробную часть числа
        val (integerPart, decimal, prefPart, sufPartEnd) = wordsToNumber(words)
        val inDigit = selectDigit[0]
        val outDigit = selectDigit[1]

        return if (decimal != 0) {
            "$prefPart $inDigit $integerPart.$decimal $outDigit $sufPartEnd".trim()
        } else {
            if (integerPart != 0) {
                "$prefPart $inDigit $integerPart $outDigit $sufPartEnd".trim()
            } else {
                prefPart.trim()
            }
        }
    }

    // Преобразуем строку в список возможных команд
    fun getListCommands(text: String): List<List<String>> {
        //val startWords = listOf("робот", "бот", "тетработ")    // Список начала команды роботу
        //val stopWords = listOf("стоп")             // Список стоп слов
        val commands = mutableListOf<List<String>>()  // Инициализируем список для хранения команд

        var inCommand = false  // Флаг, указывающий, что мы находимся внутри команды
        var currentCommand = mutableListOf<String>()  // Список для хранения текущей команды

        // Пройдемся по словам в тексте
        val words = text.split(" ")
        for (word in words) {
            // Если слово является стартовым словом
            if (word in startWords) {
                inCommand = true
                currentCommand = mutableListOf(word)
            }
            // Если слово является стоп-словом и мы находимся внутри команды
            else if (inCommand && word in stopWords) {
                currentCommand.add(word)
                inCommand = false
                commands.add(currentCommand.toList())
            }
            // Если мы находимся внутри команды
            else if (inCommand) {
                currentCommand.add(word)
            }
        }

        // Если не было найдено ни одного стартового слова, вернем пустой список
        if (inCommand) {
            // Если команда не заканчивается стоп-словом, добавим стоп-слово
            currentCommand.add(stopWords[0])
            commands.add(currentCommand.toList())
        }

        return commands
    }

    // Форматируем текущую команду
    fun getCurrentCommand(text: String): String {
        //val startWords = listOf("робот", "бот", "тетработ", "тэтработ")    // Список начала команды роботу
        //val stopWords = listOf("стоп", "стой", "отставить", "прекратить")  // Список стоп слов

        var inCommand = false  // Флаг, указывающий, что мы находимся внутри команды
        var currentCommand = mutableListOf<String>()  // Список для хранения текущей команды

        // Пройдемся по словам в тексте
        val words = text.split(" ")
        for (word in words) {
            // Если слово является стартовым словом
            if (word in startWords) {
                inCommand = true
                currentCommand = mutableListOf(word)
            }
            // Если слово является стоп-словом и мы находимся внутри команды
            else if (inCommand && word in stopWords) {
                currentCommand.add(word)
                inCommand = false
                return currentCommand.joinToString(" ")
            }
            // Если мы находимся внутри команды
            else if (inCommand) {
                currentCommand.add(word)
            }
        }

        // Если команда не закончена стоп-словом, добавим стоп-слово в конец текущей команды
        if (inCommand) {
            currentCommand.add(stopWords[0])
            return currentCommand.joinToString(" ")
        }

        // Если не было найдено ни одного стартового слова, вернем пустую строку
        return ""
    }

    fun replaceCommands(inputStr: String): String {
        val words = inputStr.split(" ")
        val replacedWords = words.mapNotNull { word ->
            when {
                word in commandMapping || word in listOf("BB00", "B00B") || word.toIntOrNull() != null || word.toDoubleOrNull() != null ->
                    commandMapping[word] ?: word
                else -> null
            }
        }
        return replacedWords.joinToString(" ")
    }

    // При работе с микрофоном отбираем текст по ключу "text"
    //fun extractText(inputJson: String): String {
        // Удаляем лишние символы и приводим к нижнему регистру для обработки нечувствительной к регистру строки
    //    val cleanedJson = inputJson.replace("[{}\"]".toRegex(), "").toLowerCase()

        // Разделяем по запятой
    //    val keyValuePairs = cleanedJson.split(",")

        // Ищем значение по ключу "text"
    //    for (pair in keyValuePairs) {
    //        val entry = pair.split(":")
    //        if (entry.size == 2 && entry[0].trim() == "text") {
    //            return entry[1].trim()
    //        }
    //    }

    //    return ""
    //}

}