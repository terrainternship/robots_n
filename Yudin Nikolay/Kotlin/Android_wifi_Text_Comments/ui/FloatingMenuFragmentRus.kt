// Этот код представляет собой фрагмент FloatingMenuFragment, отображающий плавающее меню с различными опциями. 
// Внутри фрагмента используются аннотации @BindView, а также библиотека ButterKnife для удобной работы с представлениями. 
// Dagger используется для внедрения зависимости Pref. 

// Определение пакета, к которому относится фрагмент
package com.tofaha.Android_wifi.ui

// Импорт классов и интерфейсов из Android SDK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast

// Импорт библиотеки для работы с FloatingActionButton в Android
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment

// Импорт классов из указанных пакетов
import com.tofaha.Android_wifi.Pref
import com.tofaha.Android_wifi.R
import com.tofaha.Android_wifi.app.TofahaApplication
import com.tofaha.Android_wifi.Util

// Импорт аннотаций и библиотеки для работы с DI (Dagger)
import javax.inject.Inject
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.ip_address_dialog.view.*
import kotlinx.android.synthetic.main.port_number_dialog.view.*

/**
 * Фрагмент для отображения плавающего меню
 */
class FloatingMenuFragment : AAH_FabulousFragment() {

    // Поле для представления меню
    @BindView(R.id.content_menu)
    lateinit var contentMenu: View

    // Поле для представления кнопки закрытия меню
    @BindView(R.id.close_menu)
    lateinit var closeMenu: View

    // Поле для представления текста IP-адреса
    @BindView(R.id.menu_ip_address_text)
    lateinit var ipAddress: TextView

    // Поле для представления текста номера порта
    @BindView(R.id.menu_port_number_text)
    lateinit var portNumber: TextView

    // Поле для внедрения зависимости Pref
    @Inject
    lateinit var pref: Pref

    /**
     * Метод, настраивающий диалог фрагмента
     */
    override fun setupDialog(dialog: Dialog, style: Int) {
        val myView = View.inflate(context, R.layout.float_menu, null)
        (activity.application as TofahaApplication).getAppComponent()!!.inject(this)
        ButterKnife.bind(this, myView)

        updateInfo()

        setAnimationDuration(400) // Настройка длительности анимации (опционально; по умолчанию 500 мс)
        setPeekHeight(400) // Настройка высоты меню в слайд-позиции (опционально; по умолчанию 400dp)
        setViewgroupStatic(closeMenu) // Опционально; установка layout, прикрепленного к нижней части слайда
        setViewMain(contentMenu) // Обязательно; установка основного представления меню
        setMainContentView(myView) // Обязательно; вызов в конце перед super
        super.setupDialog(dialog, style) // Вызов super в конце
    }

    /**
     * Обработчик закрытия меню
     */
    @OnClick(R.id.close_menu)
    fun close() {
        closeFilter(this)
    }

    /**
     * Обработчик нажатия на меню IP-адреса
     */
    @OnClick(R.id.menu_ip_address)
    fun ipAddress() {
        showIpDialog()
    }

    /**
     * Обработчик нажатия на меню номера порта
     */
    @OnClick(R.id.menu_port_number)
    fun portNumber() {
        showPortDialog()
    }

    /**
     * Обработчик нажатия на меню "Share"
     */
    @OnClick(R.id.menu_share)
    fun shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.tofaha.Android_wifi")
        startActivity(Intent.createChooser(sharingIntent, "Share with"))
    }

    /**
     * Обработчик нажатия на меню "App Code Source"
     */
    @OnClick(R.id.menu_code_source)
    fun appCodeSource() {
        startActivity(Intent(context , AppSourceCode::class.java))
    }

    /**
     * Метод для отображения диалога смены IP-адреса
     */
    private fun showIpDialog() {
        val alertDialog = AlertDialog.Builder(context).create()
        val myView = layoutInflater.inflate(R.layout.ip_address_dialog, null)
        alertDialog.setView(myView)
        myView.ip_dialog.hint = pref.ipAddress
        myView.ip_dialog_change.setOnClickListener({
            if (Util.isValidIp(myView.ip_dialog.text.toString())){
                pref.ipAddress = myView.ip_dialog.text.toString()
                alertDialog.dismiss()
                updateInfo()
            }else
                Toast.makeText(context , "Enter Valid IP" , Toast.LENGTH_SHORT).show()
        })
        myView.ip_dialog_cancel.setOnClickListener({
            alertDialog.dismiss()
        })
        alertDialog.show()
    }

    /**
     * Метод для отображения диалога смены номера порта
     */
    private fun showPortDialog() {
        val alertDialog = AlertDialog.Builder(context).create()
        val myView = layoutInflater.inflate(R.layout.port_number_dialog, null)
        alertDialog.setView(myView)
        myView.port_dialog.hint = pref.portNumber.toString()
        myView.port_dialog_change.setOnClickListener({
            if (!myView.port_dialog.text.toString().equals("")){
                pref.portNumber = Integer.parseInt(myView.port_dialog.text.toString())
                alertDialog.dismiss()
                updateInfo()
            }else {
                Toast.makeText(context , "Enter port number" , Toast.LENGTH_SHORT).show()
            }
        })
        myView.port_dialog_cancel.setOnClickListener({
            alertDialog.dismiss()
        })
        alertDialog.show()
    }

    /**
     * Метод для обновления информации в меню
     */
    private fun updateInfo(){
        ipAddress.text = pref.ipAddress
        portNumber.text = pref.portNumber.toString()
    }

    /**
     * Статический метод для создания нового экземпляра фрагмента
     */
    companion object {
        fun newInstance(): FloatingMenuFragment {
            return FloatingMenuFragment()
        }
    }
}
