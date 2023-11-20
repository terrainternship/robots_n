import sounddevice as sd
import vosk
import json
import queue
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.linear_model import LogisticRegression
import words
from skills import *

q = queue.Queue()

model = vosk.Model('model_small')
samplerate = int(sd.query_devices(sd.default.device, 'input')['default_samplerate'])

def callback(indata, frames, time, status):
    q.put(bytes(indata))

def recognize(data, vectorizer, clf):
    text_vector = vectorizer.transform([data]).toarray()[0]
    probabilities = clf.predict_proba([text_vector])[0]  # Получаем вероятности
    max_prob_index = probabilities.argmax()  # Находим индекс максимальной вероятности
    answer = clf.classes_[max_prob_index]  # Получаем ответ с максимальной вероятностью
    probability = probabilities[max_prob_index]  # Вероятность для выбранного ответа
    func_name = answer.split()[0]

    print(f"Answer: {answer.replace(func_name, '')}")
    print(f"Probability: {probability:.2f}")

    exec(func_name + '()')

#def recognize(data, vectorizer, clf):
#    text_vector = vectorizer.transform([data]).toarray()[0]
#    answer = clf.predict([text_vector])[0]
#    func_name = answer.split()[0]
#    print(answer.replace(func_name, ''))
#    exec(func_name + '()')

def main():
    vectorizer = CountVectorizer()
    vectors = vectorizer.fit_transform(list(words.data_set.keys()))
    clf = LogisticRegression()
    clf.fit(vectors, list(words.data_set.values()))
    del words.data_set

    with sd.RawInputStream(samplerate=samplerate, blocksize=16000, device=sd.default.device, dtype='int16', channels=1, callback=callback):
        rec = vosk.KaldiRecognizer(model, samplerate)
        pause_limit = 1.0  # one second pause
        pause_time = 0
        while True:
            data = q.get()
            if rec.AcceptWaveform(data):
                result = json.loads(rec.Result())
                data_text = result['text']
                recognize(data_text, vectorizer, clf)
            else:
                print(rec.PartialResult())
                partial = json.loads(rec.PartialResult())
                if partial.get('partial') == '':
                    pause_time += 0.2  # молчание в 2-3 секунды
                    if pause_time >= pause_limit:
                        break
                else:
                    pause_time = 0

if __name__ == '__main__':
    main()
