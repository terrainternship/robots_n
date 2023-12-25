// Авторское право 2019 Alpha Cephei Inc.
//
// Лицензировано по Apache License, версия 2.0 («Лицензия»);
// использование этого файла разрешено только в соответствии с условиями Лицензии.
// Вы можете получить копию Лицензии на
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Если это необходимо в соответствии с применимым законодательством или согласно договору в письменной форме, программное обеспечение,
// распространяемое по Лицензии, распространяется на условиях "КАК ЕСТЬ",
// БЕЗ ГАРАНТИЙ И УСЛОВИЙ ЛЮБОГО РОДА, явных или подразумеваемых.
// См. Лицензию для конкретного языка обеспечения и
// ограничения под Лицензией.
package com.example.simplefiwiarduino

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService
import org.vosk.android.StorageService

import java.io.IOException

class VoskActivity : Activity(), RecognitionListener {
    private var model: Model? = null
    private var speechService: SpeechService? = null
    private var speechStreamService: SpeechStreamService? = null
    private var resultView: TextView? = null
    private var resultViewCode: TextView? = null

    private var recognizefiles: Button? = null
    private var recognizemic: Button? = null
    private var recognizepause: ToggleButton? = null


    //private var hypothesisTrue:String = ""
    //private var hypothesisTrueCode:String = ""

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.main)

        // Настройка макета
        resultView = findViewById(R.id.result_text)
        resultViewCode = findViewById(R.id.result_text_code)
        recognizefiles = findViewById(R.id.recognize_file)
        recognizemic = findViewById(R.id.recognize_mic)
        recognizepause = findViewById(R.id.pause)

        // Находим кнопку по её ID
        val buttonPageLeft: ImageButton = findViewById(R.id.pageLeft)
        val buttonPageStart: ImageButton = findViewById(R.id.pageStart)

        val buttonAppQuit: ImageButton = findViewById(R.id.appQuit)

        setUiState(STATE_START)
        //view
        recognizefiles!!.setOnClickListener { _: View? -> recognizeFile() }
        recognizemic!!.setOnClickListener { _: View? -> recognizeMicrophone() }
        recognizepause!!.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            pause(
                isChecked
            )
        }
        LibVosk.setLogLevel(LogLevel.INFO)

        // Проверьте, предоставил ли пользователь разрешение на запись звука, инициализируйте модель после предоставления разрешения
        val permissionCheck =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        } else {
            initModel()
        }

        buttonAppQuit.setOnClickListener {
            // Закрыть все активности и завершить приложение
            finishAffinity()
        }

        buttonPageLeft.setOnClickListener {
            val intent = Intent(this@VoskActivity, SecondActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }

        buttonPageStart.setOnClickListener {
            val intent = Intent(this@VoskActivity, MainActivity::class.java)
            startActivity(intent)

            // Закрыть текущую активность
            finish()
        }
    }

    private fun initModel() {
        StorageService.unpack(this, "model-rus", "model",
            { model: Model? ->
                this.model = model
                setUiState(STATE_READY)
            }
        ) { exception: IOException ->
            setErrorState(
                "Ошибка при распаковке модели" + exception.message
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Инициализация распознавателя - это трудоемкий процесс, включающий в себя ввод-вывод,
                // поэтому мы выполняем его в задаче асинхронно
                initModel()
            } else {
                finish()
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (speechService != null) {
            speechService!!.stop()
            speechService!!.shutdown()
        }
        if (speechStreamService != null) {
            speechStreamService!!.stop()
        }
    }

    override fun onResult(hypothesis: String) {
        //resultView.append(hypothesisTrue + "\n");
        val (hypothesisTrue, hypothesisTrueCode) = extractText(hypothesis)
        if (hypothesisTrue !== "") {
            resultView!!.append(
                """
                    $hypothesisTrue
                    
                    """.trimIndent()
            )
        }
        if (hypothesisTrueCode !== "") {
            resultViewCode!!.append(
                """
                    $hypothesisTrueCode
                    
                    """.trimIndent()
            )
        }
    }

    override fun onFinalResult(hypothesis: String) {
        //resultView.append(hypothesis + "\n");
        val (hypothesisTrue, hypothesisTrueCode) = extractText(hypothesis)
        if (hypothesisTrue !== "") {
            resultView!!.append(
                """
                    $hypothesisTrue
                    
                    """.trimIndent()
            )
        }
        if (hypothesisTrueCode !== "") {
            resultViewCode!!.append(
                """
                    $hypothesisTrueCode
                    
                    """.trimIndent()
            )
        }
        setUiState(STATE_DONE)
        if (speechStreamService != null) {
            speechStreamService = null
        }
    }

    override fun onPartialResult(hypothesis: String) {
        //resultView.append(hypothesis + "\n");
        val (hypothesisTrue, hypothesisTrueCode) = extractText(hypothesis)
        if (hypothesisTrue !== "") {
            resultView!!.append(
                """
                    $hypothesisTrue
                    
                    """.trimIndent()
            )
        }
        if (hypothesisTrueCode !== "") {
            resultViewCode!!.append(
                """
                    $hypothesisTrueCode
                    
                    """.trimIndent()
            )
        }
    }

    override fun onError(e: Exception) {
        setErrorState(e.message)
    }

    override fun onTimeout() {
        setUiState(STATE_DONE)
    }

    private fun setUiState(state: Int) {
        when (state) {
            STATE_START -> {
                resultView!!.setText(R.string.preparing)
                resultView!!.movementMethod = ScrollingMovementMethod()
                recognizefiles!!.isEnabled = false
                recognizemic!!.isEnabled = false
                recognizepause!!.isEnabled = false
            }

            STATE_READY -> {
                resultView!!.setText(R.string.ready)
                recognizemic!!.setText(R.string.recognize_microphone)
                recognizefiles!!.isEnabled = true
                recognizemic!!.isEnabled = true
                recognizepause!!.isEnabled = false
            }

            STATE_DONE -> {
                recognizefiles!!.setText(R.string.recognize_file)
                recognizemic!!.setText(R.string.recognize_microphone)
                recognizefiles!!.isEnabled = true
                recognizemic!!.isEnabled = true
                recognizepause!!.isEnabled = false
                recognizepause!!.isChecked = false
            }

            STATE_FILE -> {
                recognizefiles!!.setText(R.string.stop_file)
                resultView!!.text = getString(R.string.starting)
                recognizemic!!.isEnabled = false
                recognizefiles!!.isEnabled = true
                recognizepause!!.isEnabled = false
            }

            STATE_MIC -> {
                recognizemic!!.setText(R.string.stop_microphone)
                resultView!!.text = getString(R.string.say_something)
                recognizefiles!!.isEnabled = false
                recognizemic!!.isEnabled = true
                recognizepause!!.isEnabled = true
            }

            else -> throw IllegalStateException("Неожиданное значение: $state")
        }
    }

    private fun setErrorState(message: String?) {
        resultView!!.text = message
        recognizemic!!.setText(R.string.recognize_microphone)
        recognizefiles!!.isEnabled = false
        recognizemic!!.isEnabled = false
    }

    private fun recognizeFile() {
        if (speechStreamService != null) {
            setUiState(STATE_DONE)
            speechStreamService!!.stop()
            speechStreamService = null
        } else {
            setUiState(STATE_FILE)
            try {
                val rec = Recognizer(
                    model, 16000f, "[\"один ноль ноль ноль один\", " +
                            "\"ноль один два три четыре пять шесть семь восемь девять\", \"[неизвестно]\"]"
                )
                val ais = assets.open(
                    "10001-90210-01803.wav"
                )
                if (ais.skip(44) != 44L) throw IOException("Файл слишком короткий")
                speechStreamService = SpeechStreamService(rec, ais, 16000f)
                speechStreamService!!.start(this)
            } catch (e: IOException) {
                setErrorState(e.message)
            }
        }
    }

    private fun recognizeMicrophone() {
        if (speechService != null) {
            setUiState(STATE_DONE)
            speechService!!.stop()
            speechService = null
        } else {
            setUiState(STATE_MIC)
            try {
                val rec = Recognizer(model, 16000.0f)
                speechService = SpeechService(rec, 16000.0f)
                speechService!!.startListening(this)
            } catch (e: IOException) {
                setErrorState(e.message)
            }
        }
    }

    private fun pause(checked: Boolean) {
        if (speechService != null) {
            speechService!!.setPause(checked)
        }
    }

    companion object {
        private const val STATE_START = 0
        private const val STATE_READY = 1
        private const val STATE_DONE = 2
        private const val STATE_FILE = 3
        private const val STATE_MIC = 4

        /* Используется для обработки запросов на разрешение */
        private const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1

        // Предварительно обрабатываем текст
        fun extractText(inputJson: String): Pair<String, String> {

            val outputJson = UtilsKot.getTextFromJson(inputJson).toString()
            var prepareText = UtilsKot.russianWordsToNumber(outputJson)
            var prepareTextCode = UtilsKot.russianWordsToNumberCode(outputJson)
            prepareText = UtilsKot.getCurrentCommand(prepareText)
            prepareTextCode = UtilsKot.getCurrentCommand(prepareTextCode)

            val replacedText = UtilsKot.replaceCommands(prepareTextCode)

            return Pair(prepareText, replacedText)

            //return UtilsKot.getListCommands(prepareText).toString()
            //return UtilsKot.getTextFromJson(inputJson).toString()
        }
    }
}