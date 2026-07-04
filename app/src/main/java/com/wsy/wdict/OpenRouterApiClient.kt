package com.wsy.wdict

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class OpenRouterApiClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    /**
     * Looks up a word or phrase using the OpenRouter API.
     * Must be called from a background thread.
     *
     * @param text  The word or phrase to look up.
     * @param apiKey  The user's OpenRouter API key.
     * @param model  The model identifier, e.g. "openai/gpt-4o-mini".
     * @return The definition text returned by the model.
     * @throws IOException on network or API errors.
     */
    fun lookup(text: String, apiKey: String, model: String): String {
        val prompt = buildPrompt(text)

        val requestBody = ChatRequest(
            model = model,
            messages = listOf(Message(role = "user", content = prompt))
        )

        val body = gson.toJson(requestBody).toRequestBody(JSON_MEDIA_TYPE)

        val request = Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .addHeader("HTTP-Referer", "https://github.com/wsy02322/WDict")
            .addHeader("X-Title", "WDict")
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val rawBody = response.body?.string() ?: throw IOException("Empty response body")
            if (!response.isSuccessful) {
                throw IOException("API error ${response.code}: $rawBody")
            }
            val chatResponse = gson.fromJson(rawBody, ChatResponse::class.java)
            return chatResponse.choices.firstOrNull()?.message?.content?.trim()
                ?: throw IOException("No content in API response")
        }
    }

    private fun buildPrompt(text: String): String =
        "Provide a concise dictionary entry for: \"$text\"\n\n" +
                "Include: pronunciation (if a single word), part of speech, definition(s), " +
                "and 1–2 short example sentences. Be brief and clear."

    // ── Data models ──────────────────────────────────────────────────────────

    data class ChatRequest(
        val model: String,
        val messages: List<Message>
    )

    data class Message(
        val role: String,
        val content: String
    )

    data class ChatResponse(
        val choices: List<Choice>
    )

    data class Choice(
        val message: Message
    )

    companion object {
        private const val API_URL = "https://openrouter.ai/api/v1/chat/completions"
        private val JSON_MEDIA_TYPE = "application/json".toMediaType()
    }
}
