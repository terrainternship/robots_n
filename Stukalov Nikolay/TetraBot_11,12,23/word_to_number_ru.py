# pip install python-Levenshtein
# pip install fuzzywuzzy



# Преобразование числительных из текстовой записи в числовую для русского языка

from fuzzywuzzy import process, fuzz

number_words_base = {
    'нл': 0,
    'одн': 1,
    'дв': 2,
    'тр': 3,
    'четр': 4,
    'пят': 5,
    'шест': 6,
    'сем': 7,
    'восм': 8,
    'девят': 9,
    'десят': 10,
    'одиннадцат': 11,
    'двенадцат': 12,
    'тринадцат': 13,
    'четырнадцат': 14,
    'пятнадцат': 15,
    'шестнадцат': 16,
    'семнадцат': 17,
    'восемнадцат': 18,
    'девятнадцат': 19,
    'двадцат': 20,
    'тридцат': 30,
    'сорок': 40,
    'пятдесят': 50,
    'шестдесят': 60,
    'семдесят': 70,
    'восемдесят': 80,
    'девяност': 90,
    'ст': 100,
    'двст': 200,
    'трст': 300,
    'четыреста': 400,
    'пятст': 500,
    'шестст': 600,
    'семст': 700,
    'восемст': 800,
    'девятст': 900
}

# Второй словарь для масштабирующих чисел (1000, 1000000 и т.д.)
number_words_scale = {
    'тысяч': 1000,
    'миллион': 1000000
    # ... и так далее для больших чисел, если нужно ...
}
# Алгоритмы fuzzywuzzy необходимы для отбрасывания окончаний
# Функция для преобразования числительного в число с помощью fuzzy matching
# Функция преобразования слов в число с учетом различных масштабов
def word_to_number_fuzzy(russian_number):
    words = russian_number.split()
    number = 0
    current_number = 0

    # Итерация по каждому слову во входной строке
    for word in words:
        # Использование FuzzyWuzzy для поиска наиболее подходящего масштабирующего числа
        scale_match = process.extractOne(word, number_words_scale.keys(), scorer=fuzz.token_set_ratio)
        if scale_match and scale_match[1] > 80:
            if number == 0 and current_number == 0:
                # Если это первое слово и оно масштабирующее, добавляем его значение к number
                number += number_words_scale[scale_match[0]]
            else:
                # Умножаем текущее число на масштаб и добавляем к общему числу
                current_number *= number_words_scale[scale_match[0]]
                number += current_number
                current_number = 0
        else:
            # Использование FuzzyWuzzy для поиска наиболее подходящего основного числа
            base_match = process.extractOne(word, number_words_base.keys(), scorer=fuzz.token_set_ratio)
            if base_match and base_match[1] > 75:  
                # Используется 60%, потому что алгоритмы fuzzywuzzy исчитают количество совпадающих букв из общего числа, так для "два" и "две" совпадение только 66%
                # Поднял до 75% путем убирания чередующихся гласных
                # Добавляем значение основного числа к текущему числу
                current_number += number_words_base[base_match[0]]

    # Добавляем последнее текущее число к общему числу
    number += current_number

    return number

def word_to_number_fuzzy_2(russian_number):
    words = russian_number.split()
    results = []
    number = 0
    current_number = 0

    # Итерация по каждому слову во входной строке
    for word in words:
        # Поиск масштабирующих чисел
        scale_match = process.extractOne(word, number_words_scale.keys(), scorer=fuzz.token_set_ratio)
        base_match = process.extractOne(word, number_words_base.keys(), scorer=fuzz.token_set_ratio)
        
        if scale_match and scale_match[1] > 80:
            if number == 0 and current_number == 0:
                # Если это первое слово и оно масштабирующее, добавляем его значение к number
                number += number_words_scale[scale_match[0]]
            else:
                # Обработка масштабирующего числа
                number += current_number * number_words_scale[scale_match[0]]
                current_number = 0

        # Поиск основных чисел
        elif base_match and base_match[1] > 75:
            # Обработка основного числа
            current_number += number_words_base[base_match[0]]

        elif current_number > 0:
            # Если есть число, добавляем его в список
            results.append(number + current_number)
            number = 0
            current_number = 0
            results.append(word)
        
        else:
            # Обработка не распознанных слов
            results.append(word)

    return results


# # Тестирование функции
# test_numbers = [
#     'одна тысяча двести тридцать четыре',
#     'две тысячи пятьсот шестьдесят семь',
#     'три тысячи четыреста восемьдесят восемь',
#     'четыре тысячи девятьсот девяносто девять',
#     'пять тысяч', 
#     'шесть тысяч триста двенадцать',
#     'семь тысяч двести двадцать один',
#     'восемь тысяч четыреста три',
#     'девять тысяч восемьсот семьдесят шесть',
#     'миллион',
#     'миллион одна тысяча',
#     'миллион двести',
#     'миллион триста тридцать три тысячи четыреста пятьдесят шесть',
#     'два миллиона',
#     'два миллиона пятьсот тысяч',
#     'три миллиона четыреста тысяч',
#     'четыре миллиона восемьсот тысяч',
#     'пять миллионов девятьсот тысяч',
#     'шесть миллионов семьсот тысяч',
#     'семь миллионов одна тысяча двести тридцать четыре'
# ]
# 
# converted_numbers = [word_to_number_fuzzy(num) for num in test_numbers]
# print(converted_numbers)