from setuptools import setup

setup(
    name='VoiceCommand',
    version='0.1.0',
    packages=['vosk'],
    install_requires=[
        'pyqt5',
        'vosk',
        'pyaudio'
    ],
)


