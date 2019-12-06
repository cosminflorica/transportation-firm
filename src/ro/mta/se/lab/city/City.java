package ro.mta.se.lab.city;

public class City {
    private String cityName;

    public City() {
        cityName = null;
    }

    public City(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

}