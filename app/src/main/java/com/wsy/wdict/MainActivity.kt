package com.wsy.wdict

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wsy.wdict.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var apiKeyStorage: ApiKeyStorage
    private val apiClient = OpenRouterApiClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        apiKeyStorage = ApiKeyStorage(this)

        binding.buttonLookup.setOnClickListener {
            performLookup()
        }

        binding.editTextWord.setOnEditorActionListener { _, _, _ ->
            performLookup()
            true
        }

        // Handle word pre-populated from ProcessTextActivity
        handleIncomingIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        val word = intent?.getStringExtra(EXTRA_LOOKUP_WORD) ?: return
        if (word.isNotBlank()) {
            binding.editTextWord.setText(word)
            lookupWord(word)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performLookup() {
        val word = binding.editTextWord.text?.toString()?.trim() ?: return
        if (word.isNotEmpty()) lookupWord(word)
    }

    private fun lookupWord(word: String) {
        val apiKey = apiKeyStorage.apiKey
        if (apiKey.isEmpty()) {
            binding.textViewResult.text = getString(R.string.error_no_api_key)
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.buttonLookup.isEnabled = false
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
                binding.buttonLookup.isEnabled = true
            }
        }
    }

    companion object {
        const val EXTRA_LOOKUP_WORD = "extra_lookup_word"
    }
}
