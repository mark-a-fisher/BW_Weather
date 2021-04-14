package fisher.mark.powdayv2;

public class Weather {
    private String date_time;
    private String date;
    private String time;
    private String weather_type;
    private int average_temperature;
    private float snow_amount;

    Weather(String date_time, String date, String time, String weather_type, int average_temperature, float snow_amount) {
        this.date_time = date_time;
        this.date = date;
        this.time = time;
        this.weather_type = weather_type;
        this.average_temperature = average_temperature;
        this.snow_amount = snow_amount;
    }

    public String getDate_time() {
        return date_time;
    }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getWeather_type() { return weather_type; }
    public int getAverage_temperature() { return average_temperature; }
    public float getSnow_amount() {
        return snow_amount;
    }
}
