package com.harissabil.damome.domain.use_case

import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.domain.model.TransactionOcrData

interface ExtractTransactionDataUseCase {

    suspend operator fun invoke(image: ByteArray): Result<TransactionOcrData>
}