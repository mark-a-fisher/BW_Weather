package fisher.mark.powdayv2;

public class WeatherAverage {
    private String date;
    private String weather_type;
    private float average_temp;
    private float total_snowfall;

    WeatherAverage(String date, String weather_type, float average_temp, float total_snowfall) {
        this.date = date;
        this.weather_type = weather_type;
        this.average_temp = average_temp;
        this.total_snowfall = total_snowfall;
    }

    public String getDate() {return date;}
    public String getWeather_type() { return weather_type;}
    public float getAverage_temp() {return average_temp;}
    public float getTotal_snowfall() {return total_snowfall;}
}
