# pip install sounddevice
# pip install vosk

import threading
import queue
import sounddevice as sd
import vosk
import json

from word_to_number_ru import word_to_number_fuzzy_2

from math import sqrt

from kivy.app import App
from kivy.lang import Builder
from kivy.metrics import dp
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.gridlayout import GridLayout
from kivy.uix.anchorlayout import AnchorLayout
from kivy.properties import StringProperty
from kivy.uix.button import Button
from kivy.uix.textinput import TextInput
from kivy.utils import get_color_from_hex
from kivy.core.window import Window
from kivy.clock import Clock

# from sounddevice import query_devices as qd
# from sounddevice import default as d
# from sounddevice import RawInputStream as ris

# from microphone import testing

KV = """

TetraBotBL:
    orientation: "vertical"
    padding: "10dp"
    spacing: "10dp"
    
    canvas.before:
        Color:
            rgba: root.background_color
        Rectangle:
            pos: self.pos
            size: self.size
            
    AnchorLayout:
        anchor_x: "center"
        anchor_y: "top"
        
        Label:
            id: output_label
            text: root.data_label
            color: root.text_color
        
    TextInput:
        id: Inp
        multiline: True
    
    GridLayout:
        cols: 2
        spacing: '10dp'
        size_hint: None, None
        width: self.minimum_width
        pos_hint: {'center_x': 0.5}

        Button:
            text: "Запись с микрофона"
            color: root.text_color
            on_press: root.microphone()
            size_hint: None, None
            size: "150dp", "50dp"
            background_color: (0, 0, 0, 0)
            canvas.before:
                Color:
                    rgba: root.start_button_color if self.state == 'normal' else root.pressed_start_button_color
                Rectangle:
                    pos: self.pos
                    size: self.size

        Button:
            text: "Отправить команду"
            color: root.text_color
            on_press: root.send_command()
            size_hint: None, None
            size: "150dp", "50dp"
            background_color: (0, 0, 0, 0)
            canvas.before:
                Color:
                    rgba: root.start_button_color if self.state == 'normal' else root.pressed_start_button_color
                Rectangle:
                    pos: self.pos
                    size: self.size

        Widget:
            # Пространство между кнопками
            size_hint_x: None
            width: dp(20)

    GridLayout:
        cols: 1
        size_hint: None, None
        width: self.minimum_width
        pos_hint: {'center_x': 0.5}
        spacing: '0dp'

        Button:
            text: "Вперед"
            color: root.text_color
            on_press: root.move_forward()
            on_release: root.stop()
            size_hint: None, None
            size: "100dp", "86dp"
            background_color: (0, 0, 0, 0)
            canvas.before:
                Color:
                    rgba: root.triangle_buttons_color if self.state == 'normal' else root.pressed_triangle_button_color
                Triangle:
                    points: [self.x, self.y, self.right, self.y, self.center_x, self.top]

    GridLayout:
        cols: 3
        size_hint: None, None
        width: self.minimum_width
        pos_hint: {'center_x': 0.5}
        spacing: '10dp'
 
        Button:
            text: "Влево"
            color: root.text_color
            on_press: root.move_left()
            on_release: root.stop()
            size_hint: None, None
            size: "86dp", "100dp"
            background_color: (0, 0, 0, 0)
            canvas.before:
                Color:
                    rgba: root.triangle_buttons_color if self.state == 'normal' else root.pressed_triangle_button_color
                Triangle:
                    points: [self.right, self.y, self.right, self.top, self.x, self.center_y]
        Button:
            text: "Стоп"
            color: root.text_color
            on_press: root.stop()
            size_hint: None, None
            size: "100dp", "100dp"
            background_color: (0, 0, 0, 0)
            canvas.before:
                Color:
                    rgba: root.stop_button_color if self.state == 'normal' else root.pressed_stop_button_color
                Rectangle:
                    pos: self.pos
                    size: self.size
        Button:
            text: "Вправо"
            color: root.text_color
            on_press: root.move_right()
            on_release: root.stop()
            size_hint: None, None
            size: "86dp", "100dp"
            background_color: (0, 0, 0, 0)
            canvas.before:
                Color:
                    rgba: root.triangle_buttons_color if self.state == 'normal' else root.pressed_triangle_button_color
                Triangle:
                    points: [self.x, self.y, self.x, self.top, self.right, self.center_y]

    GridLayout:
        cols: 1
        size_hint: None, None
        width: self.minimum_width
        pos_hint: {'center_x': 0.5}
        spacing: '10dp'

        Button:
            text: "Назад"
            color: root.text_color
            on_press: root.move_back()
            on_release: root.stop()
            size_hint: None, None
            size: "100dp", "86dp"
            background_color: (0, 0, 0, 0)
            canvas.before:
                Color:
                    rgba: root.triangle_buttons_color if self.state == 'normal' else root.pressed_triangle_button_color
                Triangle:
                    points: [self.x, self.top, self.right, self.top, self.center_x, self.y]



"""

class TetraBotBL(BoxLayout):

    Window.size = (450, 800)
    data_label = StringProperty("Управление ботом")
    text_color = get_color_from_hex('#000000')
    background_color = get_color_from_hex('#F2F2F2')

    triangle_buttons_color = get_color_from_hex('#5B9BD5')
    pressed_triangle_button_color = get_color_from_hex('#4472C4')

    start_button_color = get_color_from_hex('#00B050')
    pressed_start_button_color = get_color_from_hex('#00873D')

    stop_button_color = get_color_from_hex('#FF0000')
    pressed_stop_button_color = get_color_from_hex('#C00000')

    def __init__(self, **kwargs):
        super(TetraBotBL, self).__init__(**kwargs)
        self.model = vosk.Model('model_small')
        self.samplerate = int(sd.query_devices(sd.default.device, 'input')['default_samplerate'])

    def microphone(self):
        threading.Thread(target=self.run_microphone).start()

    def run_microphone(self):
        q = queue.Queue()

        def callback(indata, frames, time, status):
            q.put(bytes(indata))

        with sd.RawInputStream(samplerate=self.samplerate, blocksize=16000, device=sd.default.device, dtype='int16', channels=1, callback=callback):
            rec = vosk.KaldiRecognizer(self.model, self.samplerate)
            while True:
                data = q.get()
                if rec.AcceptWaveform(data):
                    result = json.loads(rec.Result())
                    self.update_text_input(result['text'])
                    break

    def update_text_input(self, text):
        # Обновление текстового поля должно происходить в основном потоке
        def update_text():
            self.ids.Inp.text = text

        Clock.schedule_once(lambda dt: update_text(), 0)

    def send_command(self):
        command_list = word_to_number_fuzzy_2(self.ids.Inp.text)
        command_str = ' '.join(str(item) for item in command_list)
        self.ids.output_label.text = f"Выполняется команда: {command_str}"

    def move_left(self):
        self.ids.output_label.text = "Выполняется команда: Влево"

    def move_right(self):
        self.ids.output_label.text = "Выполняется команда: Вправо"

    def move_forward(self):
        self.ids.output_label.text = "Выполняется команда: Вперед"

    def move_back(self):
        self.ids.output_label.text = "Выполняется команда: Назад"

    def stop(self):
        self.ids.output_label.text = "Выполняется команда: Стоп"
    

class MyApp(App):
    def build(self):
        return Builder.load_string(KV)

    def on_stop(self):
        # Очистить ресурсы, если это необходимо
        pass

MyApp().run()