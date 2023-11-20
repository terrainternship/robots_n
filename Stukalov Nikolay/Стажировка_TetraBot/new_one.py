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
from math import sqrt

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
        multiline: False
    
    GridLayout:
        cols: 2
        spacing: self.width * 0.05, "10dp"
        
        Button:
            text: "Отправить команду"
            color: root.text_color
            on_press: root.send_command()
            background_color: (0, 0, 0, 0)
        
            canvas.before:
                Color:
                    rgba: root.start_button_color if self.state == 'normal' else root.pressed_button_color
                Rectangle:
                    pos: self.pos
                    size: self.size
        
        Button:
            text: "Стоп"
            color: root.text_color
            on_press: root.stop()
            background_color: (0, 0, 0, 0)
        
            canvas.before:
                Color:
                    rgba: root.stop_button_color if self.state == 'normal' else root.pressed_button_color
                Rectangle:
                    pos: self.pos
                    size: self.size
    
    AnchorLayout:
        anchor_x: 'center'
        anchor_y: 'center'
        
        GridLayout:
            width: self.minimum_width
            height: self.minimum_height
            size_hint: None, None
            cols: 4
            spacing: '50dp'
            pos_hint: {'center_x': 0.5, 'center_y': 0.5}
        
            Button:
                text: "Влево"
                color: root.text_color
                on_press: root.move_left()
                size_hint: None, None
                size: "86dp", "100dp"
                background_color: (0, 0, 0, 0)

                canvas.before:
                    Color:
                        rgba: root.triangle_buttons_color if self.state == 'normal' else root.pressed_button_color
                    Triangle:
                        points: [self.right, self.y, self.right, self.top, self.x, self.center_y]

            Button:
                text: "Вперед"
                color: root.text_color
                on_press: root.move_forward()
                size_hint: None, None
                size: "100dp", "86dp"
                background_color: (0, 0, 0, 0)

                canvas.before:
                    Color:
                        rgba: root.triangle_buttons_color if self.state == 'normal' else root.pressed_button_color
                    Triangle:
                        points: [self.x, self.y, self.right, self.y, self.center_x, self.top]

            Button:
                text: "Назад"
                color: root.text_color
                on_press: root.move_back()
                size_hint: None, None
                size: "100dp", "86dp"
                background_color: (0, 0, 0, 0)

                canvas.before:
                    Color:
                        rgba: root.triangle_buttons_color if self.state == 'normal' else root.pressed_button_color
                    Triangle:
                        points: [self.x, self.top, self.right, self.top, self.center_x, self.y]

            Button:
                text: "Вправо"
                color: root.text_color
                on_press: root.move_right()
                size_hint: None, None
                size: "86dp", "100dp"
                background_color: (0, 0, 0, 0)

                canvas.before:
                    Color:
                        rgba: root.triangle_buttons_color if self.state == 'normal' else root.pressed_button_color
                    Triangle:
                        points: [self.x, self.y, self.x, self.top, self.right, self.center_y]

"""

class TetraBotBL(BoxLayout):
    data_label = StringProperty("Управление ботом")
    background_color = get_color_from_hex('#F2F2F2')
    triangle_buttons_color = get_color_from_hex('#5B9BD5')
    text_color = get_color_from_hex('#000000')
    start_button_color = get_color_from_hex('#00B050')
    stop_button_color = get_color_from_hex('#FF0000')
    pressed_button_color = get_color_from_hex('#004F37')

    def send_command(self):
        command = self.ids.Inp.text
        self.ids.output_label.text = f"Выполняется команда: {command}"

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

    def update_triangle_vertices(self):
        # Ширина и высота кнопки
        btn_width = dp(100)
        btn_height = dp(100)

        # Расчет высоты равностороннего треугольника
        triangle_height = sqrt(3) / 2 * btn_width

        # Расчет вертикальной позиции
        vertical_pos = (btn_height - triangle_height) / 2

        # Координаты вершин
        left_vertex = (self.x, self.y + vertical_pos)
        right_vertex = (self.x, self.y + vertical_pos + triangle_height)
        top_vertex = (self.x + btn_width, self.y + btn_height / 2)

        return [left_vertex[0], left_vertex[1], right_vertex[0], right_vertex[1], top_vertex[0], top_vertex[1]]

class MyApp(App):
    def build(self):
        return Builder.load_string(KV)

    def on_stop(self):
        # Очистить ресурсы, если это необходимо
        pass

MyApp().run()