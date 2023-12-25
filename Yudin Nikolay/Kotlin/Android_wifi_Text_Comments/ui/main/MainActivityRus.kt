// Этот код представляет собой главную активити приложения. 
// Используются аннотации @BindView и библиотека ButterKnife для удобной работы с представлениями. 
// Dagger используется для внедрения зависимости Pref. 

// Определение пакета, к которому относится активити
package com.tofaha.Android_wifi.ui.main

// Импорт классов из Android SDK
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.annotation.RequiresApi
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tofaha.Android_wifi.Pref
import com.tofaha.Android_wifi.R
import com.tofaha.Android_wifi.app.MyData
import com.tofaha.Android_wifi.app.TofahaApplication
import com.tofaha.Android_wifi.connection.CLoseConnection
import com.tofaha.Android_wifi.connection.OpenConnection
import com.tofaha.Android_wifi.connection.SendMessages
import com.tofaha.Android_wifi.ui.FloatingMenuFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.*
import javax.inject.Inject

/**
 * Главная активити приложения
 */
class MainActivity : AppCompatActivity(), MainView {

    // Поле для чтения введенного текста
    var input: BufferedReader? = null

    // Поле для внедрения зависимости Pref
    @Inject
    lateinit var pref: Pref

    // Поле для представления ввода сообщения
    @BindView(R.id.text_message)
    lateinit var msgText: EditText

    // Поле для представления ответа сервера
    @BindView(R.id.server_response)
    lateinit var serverResponse: TextView

    /**
     * Метод, вызываемый при создании активити
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Внедрение зависимости Pref с использованием Dagger
        (this.application as TofahaApplication).getAppComponent()!!.inject(this)

        // Привязка представлений с использованием ButterKnife
        ButterKnife.bind(this)

        // Установка ActionBar и статус-бара
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue_menu)))
        window.statusBarColor = resources.getColor(R.color.blue_menu)

        // Передвижение FAB поверх других элементов
        this.fab.bringToFront()
        this.fab.parent.requestLayout()

        // Установка обработчиков для переключателей
        led1.setOnClickListener({
            var m: String
            if (led1.isChecked)
                m = "pin=11"
            else
                m = "pin=01"
            sendMessage(m)
        })

        led2.setOnClickListener({
            var m: String
            if (led2.isChecked)
                m = "pin=22"
            else
                m = "pin=02"
            sendMessage(m)
        })

        // Обработчик для кнопки "Refresh Connection"
        refresh_connection.setOnClickListener({
            openConnection()
        })
    }

    /**
     * Обработчик нажатия на FAB
     */
    @OnClick(R.id.fab)
    operator fun next() {
        var dialogFrag = FloatingMenuFragment.newInstance()
        dialogFrag.setParentFab(fab)
        dialogFrag.show(supportFragmentManager, dialogFrag.getTag())
    }

    /**
     * Обработчик нажатия на кнопку отправки сообщения
     */
    @OnClick(R.id.send_message)
    fun sendButton() {
        sendMessage(msgText.text.toString())
    }

    /**
     * Метод для отправки сообщения
     */
    override fun sendMessage(msg: String) {
        var sendMessages = SendMessages(msg)
        sendMessages!!.execute()
    }

    /**
     * Метод для открытия соединения
     */
    override fun openConnection() {
        var openConnection = OpenConnection(pref.ipAddress!!, pref.portNumber)
        openConnection!!.execute()
    }

    /**
     * Метод для приема сообщений от сервера
     */
    override fun receiveMessage() {
        doAsync {

            input = BufferedReader(InputStreamReader(MyData.socket.getInputStream()))
            var msgText = "waiting ...."
            MyData.THREAD_RUNNING = true

            while (true) {
                Thread.sleep(300)
                msgText = input?.readLine().toString()

                uiThread {
                    serverResponse.text = msgText
                }
            }
        }
    }

    /**
     * Метод для закрытия соединения
     */
    override fun closeConnection() {
        var closeConnection = CLoseConnection()
        closeConnection!!.execute()
    }

    /**
     * Метод, вызываемый при возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        openConnection()
    }

    /**
     * Метод, вызываемый при приостановке активити
     */
    override fun onPause() {
        super.onPause()
        closeConnection()
    }
}
