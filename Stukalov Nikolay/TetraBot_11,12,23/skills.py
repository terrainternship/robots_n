from word_to_number_ru import word_to_number_fuzzy_2
from fuzzywuzzy import process
import fuzzywuzzy.fuzz

move = {"Движение", "Иди"}

arm = {"Манипулятор", "Рук", "Клешн"}

move_to = {"Вперед": "0001",
           "Назад": "0002",
           "Влево": "0003",
           "Вправо": "0004",
           "Повернись": "0008"}

arm_to = {"Вверх": "0005",
          "Вниз": "0006",
          "Сжать": "0007",
          "Разжать": "0008",
          "Вправо": "0009",
          "Влево": "000A"}

dimetions = {"шаг": "00A0",
             "метр": "00B0",
             "градус": "00C0"}

stop = {"Стоп": "0000"}


"""

000B - Повернись
0000 – Стоп
0001 – Вперед 
0002 – Назад
0003 – Влево 
0004 – Вправо
0005 – Манипулятор вверх
0006 – Манипулятор вниз
0007 – Сжать клешню
0008 – Разжать клешню
0009 – Повернуть клешню по часовой стрелке
000A – Повернуть клешню против часовой стрелки

"""

def tranfer_text_to_command(text_list):
    comand = ['AAAA']
    arm_match = (None, 0)  # Инициализация arm_match

    for word in text_list:
        if isinstance(word, str):
            match = process.extractOne(word, arm, scorer=fuzzywuzzy.fuzz.token_sort_ratio)
            if match[1] > arm_match[1]:
                arm_match = match

    if arm_match[1] > 80:
        print(arm_to)
    else:
        print(move_to)

    comand.append('FFFF')
    return comand

text = "Поверни руку вправо на двадцать градусов"
comand = tranfer_text_to_command(word_to_number_fuzzy_2(text))
print(text)
print(comand)