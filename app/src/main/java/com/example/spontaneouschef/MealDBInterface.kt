package com.example.spontaneouschef

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface MealDBInterface {
    @GET("api/json/v1/1/random.php")
    fun getRandomRecipe(): Call<MealDBBody>
}