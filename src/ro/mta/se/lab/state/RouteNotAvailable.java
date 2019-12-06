package ro.mta.se.lab.state;

import ro.mta.se.lab.classes.Route;

import java.time.LocalTime;

public class RouteNotAvailable implements RouteState {

    private Route route;

    public RouteNotAvailable(Route newRoute) {
        route = newRoute;
    }

    @Override
    public void setAvailable() {
        if (LocalTime.now().isBefore(LocalTime.parse(route.getLeavingHour()))) {
            route.setRouteState(route.getRouteAvailable());
        }
    }

    @Override
    public void setNotAvailable() {
        if (LocalTime.now().isAfter(LocalTime.parse(route.getLeavingHour()))) {
            route.setRouteState(route.getRouteNotAvailable());
        }
    }
}
