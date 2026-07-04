package com.wsy.wdict

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wsy.wdict.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var apiKeyStorage: ApiKeyStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        apiKeyStorage = ApiKeyStorage(this)

        binding.editTextApiKey.setText(apiKeyStorage.apiKey)
        binding.editTextModel.setText(apiKeyStorage.model)

        binding.buttonSave.setOnClickListener {
            saveSettings()
        }
    }

    private fun saveSettings() {
        val apiKey = binding.editTextApiKey.text?.toString()?.trim() ?: ""
        val model = binding.editTextModel.text?.toString()?.trim() ?: ""

        apiKeyStorage.apiKey = apiKey
        apiKeyStorage.model = model.ifEmpty { ApiKeyStorage.DEFAULT_MODEL }

        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
