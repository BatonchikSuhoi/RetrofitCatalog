package com.batonchiksuhoi.retrofitcatalog

import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.batonchiksuhoi.retrofitcatalog.adapter.ProductAdapter
import com.batonchiksuhoi.retrofitcatalog.databinding.ActivityProductsListBinding
import com.batonchiksuhoi.retrofitcatalog.retrofit.MainApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsListActivity : ComponentActivity() {
    private lateinit var adapter: ProductAdapter
    lateinit var binding: ActivityProductsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ProductAdapter()
        binding.productsRcView.layoutManager = LinearLayoutManager(this)
        binding.productsRcView.adapter = adapter


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)

        binding.productsSearchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {

                CoroutineScope(Dispatchers.IO).launch {

                    val productsObject = text?.let { mainApi.getAllProductsByTag(it) }
                    runOnUiThread {
                        binding.apply {
                            adapter.submitList(productsObject?.products)
                        }

                    }

                }
                return true
            }

        })

    }
}