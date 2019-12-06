package ro.mta.se.lab.classes;

import ro.mta.se.lab.city.City;
import ro.mta.se.lab.state.RouteState;
import ro.mta.se.lab.state.*;

public class Route {

    private City sourceCity;
    private City destinationCity;
    private Integer leavingTime;
    private Integer duration;

    private RouteState routeState;

    private RouteState routeAvailable;
    private RouteState routeNotAvailable;


    public Route() {
        this.sourceCity = null;
        this.destinationCity = null;
        this.leavingTime = null;
        this.duration = null;
        this.routeState = null;

        this.routeAvailable = null;
        this.routeNotAvailable = null;
    }

    public Route(City sourceCity, City destinationCity, Integer leaving, Integer duration) {

        this.destinationCity = destinationCity;
        this.sourceCity = sourceCity;
        this.duration = duration;
        this.leavingTime = leaving;

        routeAvailable = new RouteAvailable(this);
        routeNotAvailable = new RouteNotAvailable(this);

        routeState = routeNotAvailable;
    }

    public void setRouteState(RouteState newRouteState) {
        this.routeState = newRouteState;
    }

    public void setRouteAvailable(RouteState routeAvailable) {
        this.routeAvailable = routeAvailable;
    }

    public void setRouteNoteAvailable(RouteState routeNotAvailable) {
        this.routeNotAvailable = routeNotAvailable;
    }

    public String getLeavingHour() {

        Integer routeLeavingTime = this.getLeavingTime();
        Integer routeLeavingHour = routeLeavingTime / 60;
        Integer routeLeavingMinute = routeLeavingTime % 60;
        String leavingTime = routeLeavingHour.toString() + ":" + routeLeavingMinute.toString();
        if (leavingTime.length() == 2) {
            leavingTime = "0" + leavingTime;
        } else if (leavingTime.length() == 3) {
            leavingTime = "0" + leavingTime + "0";
        } else if (leavingTime.length() == 4) {
            leavingTime = leavingTime + "0";
        }

        return leavingTime;
    }

    public RouteState getRouteAvailable() {
        return routeAvailable;
    }

    public RouteState getRouteNotAvailable() {
        return routeNotAvailable;
    }

    public void setDestinationCity(City destinationCity) {
        this.destinationCity = destinationCity;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setSourceCity(City sourceCity) {
        this.sourceCity = sourceCity;
    }

    public void setLeavingTime(Integer leavingTime) {
        this.leavingTime = leavingTime;
    }

    public RouteState getRouteState() {
        return routeState;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public Integer getDuration() {
        return duration;
    }

    public City getSourceCity() {
        return sourceCity;
    }

    public Integer getLeavingTime() {
        return leavingTime;
    }
}
