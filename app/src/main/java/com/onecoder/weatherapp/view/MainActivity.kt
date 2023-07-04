package com.onecoder.weatherapp5.view

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onecoder.weatherapp5.databinding.ActivityMainBinding
import com.onecoder.weatherapp5.viewModel.MainViewModel
import java.text.DateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    private lateinit var viewModel:MainViewModel
    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GET=getSharedPreferences(packageName, MODE_PRIVATE)
        SET=GET.edit()

        viewModel=ViewModelProvider(this).get(MainViewModel::class.java)

        var cName=GET.getString("cityName","ankara")
        binding.cityNameEditText.setText(cName)
        viewModel.refreshData(cName!!)

        getLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.linearDataView.visibility=View.GONE
            binding.errorText.visibility=View.GONE
            binding.progressBar.visibility=View.GONE

            var cityName=GET.getString("cityName",cName)
            binding.cityNameEditText.setText(cityName)

            viewModel.refreshData(cityName!!)
            binding.swipeRefreshLayout.isRefreshing=false
        }

        binding.imgSearchIcon.setOnClickListener {
            val cityName=binding.cityNameEditText.text.toString()
            SET.putString("cityName",cityName)
            SET.apply()

            viewModel.refreshData(cityName)

            getLiveData()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLiveData() {
        viewModel.weather_data.observe(this, Observer { data->
            data?.let {
                binding.linearDataView.visibility= View.VISIBLE
                binding.progressBar.visibility=View.GONE

                binding.degreeText.text=data.main.temp.roundToInt().toString() + "°C"
                binding.countryCodeText.text=data.sys.country.toString()
                binding.cityNameText.text=data.name.toString()
                binding.maxTempText.text=data.main.tempMax.toString() + "°C"
                binding.minTempText.text=data.main.tempMin.toString() + "°C"
                binding.feltText.text=data.main.feelsLike.toString() + "°C"
                binding.windText.text=data.wind.deg.toString() + "/km"
                binding.humidityText.text=data.main.humidity.toString() + "%"
                binding.descriptionText.text=data.weather.get(0).description.capitalize().toString()



                val calendar = DateFormat.getDateInstance().format(data.dt.toLong()*1000);
                binding.dateText.text=calendar.toString()




                val iD=data.weather.get(0).id


                if(iD in 200..232){
                    refreshImage()
                    binding.thunderstorm.visibility=View.VISIBLE
                }else if(iD in 300..321){
                    refreshImage()
                    binding.rain.visibility=View.VISIBLE
                }else if(iD in 500..531){
                    refreshImage()
                    binding.showerRain.visibility=View.VISIBLE
                }else if(iD in 600..622){
                    refreshImage()
                    binding.snow.visibility=View.VISIBLE
                }else if(iD in 701..781){
                    refreshImage()
                    binding.mist.visibility=View.VISIBLE
                }else if(iD == 800){
                    refreshImage()
                    binding.clearSky.visibility=View.VISIBLE
                }else if(iD == 801){
                    refreshImage()
                    binding.fewClouds.visibility=View.VISIBLE
                }else if(iD == 802){
                    refreshImage()
                    binding.scatteredClouds.visibility=View.VISIBLE
                }else if(iD in 803..804){
                    refreshImage()
                    binding.brokenClouds.visibility=View.VISIBLE
                }else{
                    Toast.makeText(applicationContext,"Error. Try Again Later !",Toast.LENGTH_LONG).show()
                }


               // Glide.with(this).load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    //.into(binding.animationView)
            }
        })

        viewModel.weather_loading.observe(this, Observer { loading->
            loading?.let {
                if(it){
                    binding.progressBar.visibility=View.VISIBLE
                    binding.errorText.visibility=View.GONE
                    binding.linearDataView.visibility=View.GONE
                }else{
                    binding.progressBar.visibility=View.GONE
                }
            }
        })

        viewModel.weather_error.observe(this, Observer { error->
            error?.let {
                if (it){
                    binding.errorText.visibility=View.VISIBLE
                    binding.linearDataView.visibility=View.GONE
                    binding.progressBar.visibility=View.GONE
                }else{
                    binding.errorText.visibility=View.GONE
                }
            }
        })
    }

    private fun refreshImage() {
        binding.thunderstorm.visibility=View.GONE
        binding.showerRain.visibility=View.GONE
        binding.rain.visibility=View.GONE
        binding.snow.visibility=View.GONE
        binding.mist.visibility=View.GONE
        binding.clearSky.visibility=View.GONE
        binding.fewClouds.visibility=View.GONE
        binding.scatteredClouds.visibility=View.GONE
        binding.brokenClouds.visibility=View.GONE
    }
}