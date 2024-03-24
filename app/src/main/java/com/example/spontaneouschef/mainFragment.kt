package com.example.spontaneouschef

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


const val BASE_URL = "https://www.themealdb.com/"
/**
 * A simple [Fragment] subclass.
 * Use the [mainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class mainFragment : Fragment() {
    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val detailsView = inflater.inflate(R.layout.fragment_details, container, false)

        dataBaseHelper = DataBaseHelper(context)
        preferences = dataBaseHelper.getPreferences()!!

        val menuButton = view.findViewById<ImageButton>(R.id.imageButton)
        val lockinButton = view.findViewById<Button>(R.id.button2)
        val rerollButton = view.findViewById<Button>(R.id.button)

        val image = view.findViewById<ImageView>(R.id.imageView)
        val textName = view.findViewById<TextView>(R.id.textView)
        val textParagraph = view.findViewById<TextView>(R.id.viewTextTextMultiLine)

        val ingredientsDetails = detailsView.findViewById<TextView>(R.id.textView10)
        val instructionsDetails = detailsView.findViewById<TextView>(R.id.textView12)

        menuButton.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_prefFragment)
        }

        lockinButton.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_detailsFragment)
        }

        rerollButton.setOnClickListener{
            getRandomDish(image, textName, textParagraph, ingredientsDetails, instructionsDetails)
        }


        //image.setImageURI()

        return view
    }

    private fun getRandomDish(image: ImageView, textName:TextView, textParagragh: TextView,
                              ingredientsDetails: TextView, instructionsDetails: TextView) {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealDBInterface::class.java)

        val retrofitData = retrofitBuilder.getRandomRecipe()
        retrofitData.enqueue(object: Callback<MealDBBody>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<MealDBBody>, response: Response<MealDBBody>) {
                val mealBody: MealDBBody = response.body()!!
                val dish = mealBody.meals[0]

                ImageLoadTask(URL(dish.strMealThumb).toURI().toString(), image).execute()
                textName.text = dish.strMeal
                textParagragh.text = ">${dish.strIngredient1}\n>${dish.strIngredient2}\n>" +
                                    "${dish.strIngredient3}\n>${dish.strIngredient4}\n>" +
                                    dish.strIngredient5

                ingredientsDetails.text = ">${dish.strIngredient1}\n>${dish.strIngredient2}\n>" +
                                            "${dish.strIngredient3}\n>${dish.strIngredient4}\n>" +
                                            ">${dish.strIngredient5}\n>${dish.strIngredient6}\n>" +
                                            "${dish.strIngredient7}\n>${dish.strIngredient8}\n>" +
                                            ">${dish.strIngredient9}\n>${dish.strIngredient10}\n>"

                instructionsDetails.text = dish.strInstructions
            }

            override fun onFailure(call: Call<MealDBBody>, t: Throwable) {
                Log.d("mainFragment", "Message:"+t.message)
            }
        })
    }
}