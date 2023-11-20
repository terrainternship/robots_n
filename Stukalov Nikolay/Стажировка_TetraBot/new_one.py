from kivy.app import App
from kivy.lang import Builder
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.gridlayout import GridLayout
from kivy.uix.anchorlayout import AnchorLayout
from kivy.properties import StringProperty
from kivy.uix.button import Button
from kivy.uix.textinput import TextInput

KV = """

MyBL:
    orientation: "vertical"
    padding: "10dp"
    spacing: "10dp"
    
    AnchorLayout:
        anchor_x: "center"
        anchor_y: "top"
        
        Label:
            id: output_label
            text: root.data_label
        
    TextInput:
        id: Inp
        multiline: False
        
    Button:
        text: "Отправить команду"
        size_hint: 1, None
        text_size: self.width, None
        halign: "center"
        valign: "middle"
        on_press: root.send_command()
        
    Button:
        text: "Стоп"
        on_press: root.stop()
        
    GridLayout:
        cols: 4
        spacing: self.width * 0.05, "10dp"
        
        Button:
            text: "Влево"
            on_press: root.move_left()
            size_hint: None, None
            size: "100dp", "100dp"
            background_color: (0, 0, 0, 0)  # Make background transparent

            canvas.before:
                Color:
                    rgba: (0, 0, 1, 1) if self.state == 'normal' else (0, 0, 0.8, 1)  # Blue color, darker when pressed
                Triangle:
                    points: [self.right, self.y, self.right, self.top, self.x, self.center_y]

        Button:
            text: "Вперед"
            on_press: root.move_forward()
            size_hint: None, None
            size: "100dp", "100dp"
            background_color: (0, 0, 0, 0)  # Make background transparent

            canvas.before:
                Color:
                    rgba: (0, 0, 1, 1) if self.state == 'normal' else self.background_down
                Triangle:
                    points: [self.x, self.y, self.right, self.y, self.center_x, self.top]

        Button:
            text: "Назад"
            on_press: root.move_back()
            size_hint: None, None
            size: "100dp", "100dp"
            background_color: (0, 0, 0, 0)  # Make background transparent

            canvas.before:
                Color:
                    rgba: (0, 0, 1, 1) if self.state == 'normal' else self.background_down
                Triangle:
                    points: [self.x, self.top, self.right, self.top, self.center_x, self.y]

        Button:
            text: "Вправо"
            on_press: root.move_right()
            size_hint: None, None
            size: "100dp", "100dp"
            background_color: (0, 0, 0, 0)  # Make background transparent

            canvas.before:
                Color:
                    rgba: (0, 0, 1, 1) if self.state == 'normal' else self.background_down
                Triangle:
                    points: [self.x, self.y, self.x, self.top, self.right, self.center_y]

"""

class MyBL(BoxLayout):
    data_label = StringProperty("Управление ботом")

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

class MyApp(App):
    def build(self):
        return Builder.load_string(KV)

    def on_stop(self):
        # Очистить ресурсы, если это необходимо
        pass

MyApp().run()