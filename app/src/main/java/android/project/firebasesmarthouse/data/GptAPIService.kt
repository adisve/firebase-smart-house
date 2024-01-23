package android.project.firebasesmarthouse.data

import android.project.firebasesmarthouse.interfaces.GptAPI
import android.project.firebasesmarthouse.models.network.GptRequest
import android.project.firebasesmarthouse.models.network.GptResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GptAPIService {
    private val gptApi: GptAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GptAPI::class.java)
    }

    suspend fun createCompletion(request: GptRequest): Response<GptResponse> {
        return gptApi.createCompletion(request)
    }
}