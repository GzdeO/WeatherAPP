package com.onecoder.weatherapp5.viewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onecoder.weatherapp5.model.WeatherModel
import com.onecoder.weatherapp5.service.WeatherAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel(){

    private val weatherAPIService= WeatherAPIService()
    private val disposable=CompositeDisposable()

    val weather_data=MutableLiveData<WeatherModel>()
    val weather_error=MutableLiveData<Boolean>()
    val weather_loading=MutableLiveData<Boolean>()

    fun refreshData(cityName:String){
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName:String) {
        weather_loading.value=true
        disposable.add(
            weatherAPIService.getDataService(cityName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>(){
                    override fun onSuccess(t: WeatherModel) {
                        weather_data.value=t
                        weather_loading.value=false
                        weather_error.value=false
                    }

                    override fun onError(e: Throwable) {
                        weather_error.value=true
                        weather_loading.value=false

                    }

                })
        )
    }
}