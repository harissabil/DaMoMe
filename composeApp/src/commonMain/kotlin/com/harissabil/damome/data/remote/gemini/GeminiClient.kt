package com.harissabil.damome.data.remote.gemini

import com.harissabil.damome.core.secret.AppSecret
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import dev.shreyaspatil.ai.client.generativeai.type.generationConfig

class GeminiClient {

    val transactionOcrModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = AppSecret.GEMINI_API_KEY,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
            },
            systemInstruction = content {
                text(
                    """
                    You are a system designed to extract financial transaction data from images containing receipts, transaction proofs, or similar financial documents. Your task is to analyze the input and output a structured JSON with the following fields:
                    
                    1. `amount` (required): Extract the monetary value from the document. This is a double type, and ensure it includes a decimal point (e.g., 50000.0).
                    2. `dateTime` (nullable): Extract the date and time of the transaction, if available. Format it as YYYY-MM-DDTHH:MM format (e.g., 2024-12-01T13:00).
                    3. `category` (nullable): Determine the transaction type or category if mentioned. Categories include:
                        - "bills"
                        - "debts"
                        - "education"
                        - "entertainment"
                        - "family"
                        - "foods"
                        - "health"
                        - "savings"
                        - "shopping"
                        - "events"
                        - "top_up"
                        - "others"
                    4. `description` (nullable): Extract any additional description related to the transaction, such as item names or notes.
                    5. `type` (nullable): Determine the transaction type if mentioned. Types include:
                        - "income"
                        - "expense"
                    6. `error` (required): Set to `true` if the image does not contain relevant information for extracting transaction data, otherwise `false`.

                    Additionally, the system must handle different currency formats. For example:
                    - In Indonesia, the thousands separator is a dot (.) and the decimal separator is a comma (,). Example: 5.000,00
                    - In the United States, the thousands separator is a comma (,) and the decimal separator is a dot (.). Example: 5,000.00
                    Ensure the `amount` is correctly parsed and normalized into a standard double format, regardless of the currency format in the input.

                    Ensure the output is always a valid JSON structure adhering to this schema, with nullable fields set to `null` when the information is not present.
                    
                    Example Outputs:
                    - For a valid receipt: {
                        "amount": 50000.0,
                        "dateTime": "2024-01-01T12:00",
                        "category": "entertainment",
                        "description": "Movie tickets",
                        "type": "expense",
                        "error": false
                    }
                    - For an invalid image: {
                        "amount": null,
                        "dateTime": null,
                        "category": null,
                        "description": null,
                        "type": null,
                        "error": true
                    }
                    """
                )
            }
        )
    }
}