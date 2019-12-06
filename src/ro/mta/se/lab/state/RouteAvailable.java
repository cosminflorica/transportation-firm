package ro.mta.se.lab.state;

import ro.mta.se.lab.classes.Route;

import java.time.LocalTime;

public class RouteAvailable implements RouteState {

    private Route route;

    public RouteAvailable(Route newRoute) {
        this.route = newRoute;
    }

    @Override
    public void setAvailable() {
        if (LocalTime.now().isBefore(LocalTime.parse(route.getLeavingHour()))) {
            route.setRouteState(route.getRouteAvailable());
        }
        System.out.println("Route set available");
    }

    @Override
    public void setNotAvailable() {

        if (LocalTime.now().isAfter(LocalTime.parse(route.getLeavingHour()))) {
            route.setRouteState(route.getRouteNotAvailable());
        }
        System.out.println("Route set to not available");
        route.setRouteState(route.getRouteNotAvailable());
    }
}
