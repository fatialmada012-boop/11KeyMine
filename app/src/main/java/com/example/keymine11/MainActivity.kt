package com.example.keymine11

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var toggleButton: Button
    private lateinit var permButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        toggleButton = findViewById(R.id.toggleButton)
        permButton = findViewById(R.id.permButton)

        permButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        toggleButton.setOnClickListener {
            val isEnabled = getSharedPreferences("11keymine_prefs", Context.MODE_PRIVATE)
                .getBoolean("service_enabled", true)
            
            val newState = !isEnabled
            getSharedPreferences("11keymine_prefs", Context.MODE_PRIVATE)
                .edit().putBoolean("service_enabled", newState).apply()

            updateUI()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val isAccessibilityActive = isAccessibilityServiceEnabled(this, KeyMapperService::class.java)
        val isServiceEnabled = getSharedPreferences("11keymine_prefs", Context.MODE_PRIVATE)
            .getBoolean("service_enabled", true)

        if (!isAccessibilityActive) {
            statusText.text = "Estado: PERMISO REQUERIDO\n(Activa 11keymine en Accesibilidad)"
            toggleButton.isEnabled = false
            permButton.isEnabled = true
        } else {
            permButton.isEnabled = false
            toggleButton.isEnabled = true

            if (isServiceEnabled) {
                statusText.text = "Estado: ACTIVO (Escuchando 11 teclas)"
                toggleButton.text = "APAGAR"
            } else {
                statusText.text = "Estado: PAUSADO"
                toggleButton.text = "ENCENDER"
            }
        }
    }

    private fun isAccessibilityServiceEnabled(context: Context, service: Class<*>): Boolean {
        val expectedComponentName = "${context.packageName}/${service.canonicalName}"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val splitter = TextUtils.SimpleStringSplitter(':')
        splitter.setString(enabledServices)

        while (splitter.hasNext()) {
            if (splitter.next().equals(expectedComponentName, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}
