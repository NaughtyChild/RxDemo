package com.company.prodemo

class WeatherEntity {
    var data: DataBean? = null
    var status: Int = 0
    var desc: String? = null
}

data class DataBean(var yesterday: YesterdayBean, var city: String, var aqi: String? = null, var ganmao: String? = null, var wendu: String? = null
                    , var forecast: List<ForecastBean>? = null)
class YesterdayBean {
    var date: String? = null
    var high: String? = null
    var fx: String? = null
    var low: String? = null
    var fl: String? = null
    var type: String? = null
}

class ForecastBean {
    var date: String? = null
    var high: String? = null
    var fengli: String? = null
    var low: String? = null
    var fengxiang: String? = null
    var type: String? = null
}