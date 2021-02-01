package com.example.practicakotlinfirebase

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.activity_loggeado.*
import kotlinx.android.synthetic.main.activity_main.*

class loggeado : AppCompatActivity() {
    companion object {
        private const val COLOR_SELECTED = "selectedColor"
        private const val NO_COLOR_OPTION = "noColorOption"
    }
    private var selectedColor: Int = ColorSheet.NO_COLOR
    private var noColorOption = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loggeado)


        title = "Sesión Iniciada"
        val bundle = intent.extras
        val email = bundle?.getString("email",null)
        val provider = bundle?.getString("provider",null)

        //Con las SharedPreferences podemos guardar datos aunque la aplicacion se cierre
        //en este caso guardamos las variable de email y provider que es lo que nos interesa
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()

        tvEmail.setText(email)
        tvProvider.setText(provider)

        val colors = resources.getIntArray(R.array.colors)

        selectedColor = savedInstanceState?.getInt(COLOR_SELECTED) ?: colors.first()

        noColorOption = savedInstanceState?.getBoolean(NO_COLOR_OPTION) ?: false


        btColorPicker.setOnClickListener {
            ColorSheet().cornerRadius(8).colorPicker(
                colors = colors,
                noColorOption = noColorOption,
                selectedColor = selectedColor,
                listener = {
                    color -> selectedColor = color
                    textView2.setTextColor(color)
                    textView4.setTextColor(color)
                    Toast.makeText(this, selectedColor.toString(), Toast.LENGTH_LONG).show()
                }
            ).show(supportFragmentManager)
        }
        btColorPicker2.setOnClickListener {
            ColorSheet().cornerRadius(8).colorPicker(
                colors = colors,
                noColorOption = noColorOption,
                selectedColor = selectedColor,
                listener = {
                        color -> selectedColor = color
                    tvEmail.setTextColor(color)
                    tvProvider.setTextColor(color)
                    Toast.makeText(this, selectedColor.toString(), Toast.LENGTH_LONG).show()
                }
            ).show(supportFragmentManager)
        }
        btLogOut.setOnClickListener {
            //aqui limpiamos las preferences ya que estamos cerrando sesion.
            val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            if(provider == "FACEBOOK"){
                LoginManager.getInstance().logOut()
            }

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }

}