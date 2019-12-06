package ro.mta.se.lab.classes;


public class Main {

    private static void printAllRoutes(TransportationFirm transportationFirm) {
        if (transportationFirm.getRoutesList() != null) {
            for (Route route : transportationFirm.getRoutesList()) {
                if (route.getRouteState() == route.getRouteAvailable()) {
                    System.out.println(route.getSourceCity().getCityName() + " - " + route.getDestinationCity().getCityName() + " leaving at : " + route.getLeavingHour() + " is available ");
                } else {
                    System.out.println(route.getSourceCity().getCityName() + " - " + route.getDestinationCity().getCityName() + " leaving at : " + route.getLeavingHour() + " is NOT available, please try again tomorrow ");

                }
            }
        }
    }

    public static void main(String[] args) {

        TransportationFirm transportationFirm = TransportationFirm.getInstance(args[0]);

        transportationFirm.findPaths();

    }
}
