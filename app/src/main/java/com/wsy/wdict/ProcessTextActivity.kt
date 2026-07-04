package com.wsy.wdict

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wsy.wdict.databinding.ActivityProcessTextBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Handles text selected in other apps via the floating text-selection toolbar
 * (android.intent.action.PROCESS_TEXT).  Appears as a compact dialog.
 */
class ProcessTextActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProcessTextBinding
    private lateinit var apiKeyStorage: ApiKeyStorage
    private val apiClient = OpenRouterApiClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcessTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiKeyStorage = ApiKeyStorage(this)

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            ?.toString()
            ?.trim()

        if (text.isNullOrBlank()) {
            finish()
            return
        }

        binding.textViewWord.text = text

        binding.buttonClose.setOnClickListener { finish() }

        binding.buttonOpenInApp.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    putExtra(MainActivity.EXTRA_LOOKUP_WORD, text)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
            )
            finish()
        }

        lookupWord(text)
    }

    private fun lookupWord(word: String) {
        val apiKey = apiKeyStorage.apiKey
        if (apiKey.isEmpty()) {
            binding.textViewResult.text = getString(R.string.error_no_api_key)
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.textViewResult.text = ""

        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    apiClient.lookup(word, apiKey, apiKeyStorage.model)
                }
                binding.textViewResult.text = result
            } catch (e: Exception) {
                binding.textViewResult.text =
                    getString(R.string.error_lookup_failed, e.message)
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
