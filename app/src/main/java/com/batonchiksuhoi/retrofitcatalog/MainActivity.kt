package com.batonchiksuhoi.retrofitcatalog

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.batonchiksuhoi.retrofitcatalog.databinding.ActivityMainBinding
import com.batonchiksuhoi.retrofitcatalog.retrofit.AuthRequest
import com.batonchiksuhoi.retrofitcatalog.retrofit.MainApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //val tv = findViewById<TextView>()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)

        binding.showProductsButton.setOnClickListener{
            val intent = Intent(this, ProductsListActivity::class.java)
            //intent.putExtra("controlState",controlState)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = mainApi.auth(
                    AuthRequest(
                        binding.usernameField.text.toString(),
                        binding.passwordField.text.toString(),
                    )
                )
                runOnUiThread{
                    binding.apply {
                        Picasso.get().load(user.image).into(imageView)
                        firstNameText.text = user.firstName
                        lastNameText.text = user.lastName
                    }
                }
            }
        }
    }
}