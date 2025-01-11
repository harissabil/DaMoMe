package com.harissabil.damome.domain.use_case

import com.harissabil.damome.core.utils.Result

interface SummarizeChatUseCase {

    suspend operator fun invoke(firstQuestion: String): Result<String>
}