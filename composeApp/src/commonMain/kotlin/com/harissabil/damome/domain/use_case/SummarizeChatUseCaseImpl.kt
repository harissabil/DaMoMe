package com.harissabil.damome.domain.use_case

import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.data.remote.gemini.GeminiClient
import com.harissabil.damome.domain.model.ChatTitleSummary
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.serialization.json.Json

class SummarizeChatUseCaseImpl(
    private val geminiClient: GeminiClient,
) : SummarizeChatUseCase {
    override suspend fun invoke(firstQuestion: String): Result<String> {
        val inputContent = content {
            text(firstQuestion)
        }

        val response = geminiClient.chatSummaryModel.generateContent(inputContent)

        try {
            val data = Json.decodeFromString<ChatTitleSummary>(response.text!!)
            if (data.error) {
                return Result.Error(message = "No summary found!")
            }
            return Result.Success(data.summary!!)
        } catch (e: Exception) {
            return Result.Error(message = e.message ?: "Something went wrong!")
        }
    }
}