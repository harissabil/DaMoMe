package com.harissabil.damome.domain.use_case

import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.data.remote.gemini.GeminiClient
import com.harissabil.damome.domain.model.TransactionOcrData
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.serialization.json.Json

class ExtractTransactionDataUseCaseImpl(
    private val geminiClient: GeminiClient,
) : ExtractTransactionDataUseCase {
    override suspend fun invoke(
        image: ByteArray,
    ): Result<TransactionOcrData> {
        val inputPhoto = content {
            image(image)
            text("Extract this image")
        }

        val response = geminiClient.transactionOcrModel.generateContent(inputPhoto)

        try {
            val data = Json.decodeFromString<TransactionOcrData>(response.text!!)
            if (data.error) {
                return Result.Error(message = "No transaction data found!")
            }
            return Result.Success(data)
        } catch (e: Exception) {
            return Result.Error(message = e.message ?: "Something went wrong!")
        }
    }
}