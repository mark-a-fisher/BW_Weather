package fisher.mark.powdayv2;

public class Weather {
    private final String date;
    private final String time;
    private final String weather_type;
    private final int average_temperature;
    private final float snow_amount;

    // Setter method
    Weather(String date, String time, String weather_type, int average_temperature, float snow_amount) {
        this.date = date;
        this.time = time;
        this.weather_type = weather_type;
        this.average_temperature = average_temperature;
        this.snow_amount = snow_amount;
    }

    // Getter methods
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getWeather_type() { return weather_type; }
    public int getAverage_temperature() { return average_temperature; }
    public float getSnow_amount() { return snow_amount;
    }
}
