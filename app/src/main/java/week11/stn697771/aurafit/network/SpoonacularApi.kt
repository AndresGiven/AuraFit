package week11.stn697771.aurafit.network

import okhttp3.ResponseBody
import week11.stn697771.aurafit.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query
import week11.stn697771.aurafit.data.NutritionGuess

interface SpoonacularApi {
    @GET("recipes/guessNutrition")
    suspend fun guessNutrition(
        @Query("title") dishName: String,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_KEY
    ): NutritionGuess

    @GET("recipes/guessNutrition")
    suspend fun guessNutritionRaw(
        @Query("title") dishName: String,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_KEY
    ): ResponseBody
}
