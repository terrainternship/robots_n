from fuzzywuzzy import process
import fuzzywuzzy.fuzz

# move = {"Движение", "Иди"}

arm = {"манипулятор", "рук", "клешн", "лап", "перемес", "предмет", "объект"}

move_to = {"перед": "0001",
           "назад": "0002",
           "лево": "0003",
           "право": "0004"}

arm_to = {"верх": "0005",
          "подн": "0005",
          "опуст": "0006",
          "низ": "0006",
          "сожм": "0007",
          "сжат": "0007",
          "взят": "0007",
          "возм": "0007",
          "разж": "0008",
          "отпуст": "0008",
          "освобод": "0008",
          "право": "0009",
          "лево": "000A"}

dimetions = {"шаг": "00A0",
             "метр": "00B0",
             "градус": "00C0"}

stop = {"сто": "0000",
        "останов": "0000",
        "прекра": "0000"}

def tranfer_text_to_command(text_list):
    command = ['AAAA']

    # Variable to hold possible number, default is 1
    possible_number = 1

    # Check for a number in the text list and update possible_number if found
    for word in text_list:
        if isinstance(word, int):
            possible_number = word
            text_list.remove(word)
            break

    # First, check if any word in the list matches a 'stop' word
    stop_match_score = 0
    for key in stop.keys():
        for word in text_list:
            if isinstance(word, str):
                match = process.extractOne(word, [key], scorer=fuzzywuzzy.fuzz.token_sort_ratio)
                if match[1] > stop_match_score:
                    stop_match_score = match[1]

    # If a 'stop' word is found with high confidence, return the stop command immediately
    if stop_match_score > 80:
        command.append(stop[match[0]])
    else:
        arm_match_score = 0
        best_match_value = None

        # Check for 'arm' related words and find the best match
        for word in text_list:
            match = process.extractOne(word, arm, scorer=fuzzywuzzy.fuzz.token_sort_ratio)
            if match[1] > arm_match_score:
                arm_match_score = match[1]
        
        # Determine which dictionary to use based on the best match
        best_match_score = 0
        if arm_match_score > 80:
            for key in arm_to.keys():
                for word in text_list:
                    match = process.extractOne(word, [key], scorer=fuzzywuzzy.fuzz.token_sort_ratio)
                    if match[1] > best_match_score:
                        best_match_score = match[1]
                        best_match_value = arm_to[key]
        else:
            for key in move_to.keys():
                for word in text_list:
                    match = process.extractOne(word, [key], scorer=fuzzywuzzy.fuzz.token_sort_ratio)
                    if match[1] > best_match_score:
                        best_match_score = match[1]
                        best_match_value = move_to[key]

        # Append the best match value to the command if found
        if best_match_value:
            command.append(best_match_value)

        # Check for dimension words
        dimension_match_value = None
        for key in dimetions.keys():
            for word in text_list:
                match = process.extractOne(word, [key], scorer=fuzzywuzzy.fuzz.token_sort_ratio)
                if match[1] > 80:
                    dimension_match_value = dimetions[key]

        # Append the dimension structure if found
        if dimension_match_value:
            command.extend(['BB00', str(possible_number), 'B00B', dimension_match_value])
        else:
            command.append('A000')

    command.append('FFFF')
    return ' '.join(command)

# commands = {
#     "Подними манипулятор": "AAAA 0005 FFFF",
#     "Опусти манипулятор": "AAAA 0006 FFFF",
#     "Сожми клешню": "AAAA 0007 FFFF",
#     "Разожми клешню": "AAAA 0008 FFFF",
#     "Вращай клешню вправо": "AAAA 0009 FFFF",
#     "Поверни клешню влево": "AAAA 000A FFFF",
#     "Двигайся вперед": "AAAA 0001 FFFF",
#     "Отступи назад": "AAAA 0002 FFFF",
#     "Поворот налево": "AAAA 0003 FFFF",
#     "Поверни направо": "AAAA 0004 FFFF",
#     "Возьми предмет": "AAAA 0007 FFFF",
#     "Отпусти предмет": "AAAA 0008 FFFF",
#     "Передвигайся назад": "AAAA 0002 FFFF",
#     "Наклони манипулятор вниз": "AAAA 0006 FFFF",
#     "Подъем манипулятора": "AAAA 0005 FFFF",
#     "Поворачивай влево": "AAAA 0003 FFFF",
#     "Поворачивай вправо": "AAAA 0004 FFFF",
#     "Перемещайся влево": "AAAA 0003 FFFF",
#     "Перемещайся вправо": "AAAA 0004 FFFF",
#     "Возьми объект клешней": "AAAA 0007 FFFF",
#     "Освободи объект из клешни": "AAAA 0008 FFFF",
#     "Подними объект манипулятором": "AAAA 0005 FFFF",
#     "Опусти объект манипулятором": "AAAA 0006 FFFF",
#     "Движение вперед на пять шагов": "AAAA BB00 5 B00B 00A0 FFFF",
#     "Движение назад на три шага": "AAAA BB00 3 B00B 00A0 FFFF",
#     "Поворот на девяноста градусов влево": "AAAA BB00 90 B00B 00C0 FFFF",
#     "Поворот на сто восемьдесят градусов вправо": "AAAA BB00 180 B00B 00C0 FFFF",
#     "Подъем руки на десять градусов": "AAAA BB00 10 B00B 00C0 FFFF",
#     "Опускание руки на пятнадцать градусов": "AAAA BB00 15 B00B 00C0 FFFF",
#     "Подвинься вперед на метр": "AAAA BB00 1 B00B 00B0 FFFF",
#     "Отступи назад на метр": "AAAA BB00 1 B00B 00B0 FFFF",
#     "Поворот влево на сорок пять градусов": "AAAA BB00 45 B00B 00C0 FFFF",
#     "Поворот вправо на сорок пять градусов": "AAAA BB00 45 B00B 00C0 FFFF",
#     "Перемести объект влево": "AAAA 000A FFFF",
#     "Перемести объект вправо": "AAAA 0009 FFFF",
#     "Подними клешню выше": "AAAA 0005 FFFF",
#     "Опусти клешню ниже": "AAAA 0006 FFFF",
#     "Поверни клешню налево": "AAAA 000A FFFF",
#     "Поверни клешню направо": "AAAA 0009 FFFF",
#     "Прекрати движение": "AAAA 0000 FFFF"
# }
# 
# # Process each command key and display results
# processed_commands = {}
# for key in commands:
#     command = tranfer_text_to_command(turn_numbers_in_text_into_int(key))
#     processed_commands[key] = (commands[key], command)
# 
# processed_commands_string = str(processed_commands)
# formatted_string = "\n".join(processed_commands_string.split(", "))
# print(formatted_string)