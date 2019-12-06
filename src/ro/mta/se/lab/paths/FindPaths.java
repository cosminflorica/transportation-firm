package ro.mta.se.lab.paths;

import ro.mta.se.lab.city.City;
import ro.mta.se.lab.classes.Route;
import ro.mta.se.lab.classes.TransportationFirm;
import ro.mta.se.lab.factory.ExceptionFactory;
import ro.mta.se.lab.factory.IException;

import java.text.Normalizer;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class FindPaths {

    private ArrayList<Integer> routesList;
    private ArrayList<City> citiesList;
    private ArrayList<Integer> adjacencyMatrix;
    private static Integer pathChecker;

    private ArrayList<Integer> path;
    private Integer sourceCity;
    private Integer destinationCity;
    private Integer startingHour;


    public FindPaths(TransportationFirm transportationFirm) {

        this.routesList = new ArrayList<>(40);
        this.citiesList = transportationFirm.getCitiesList();
        this.adjacencyMatrix = transportationFirm.getAdjacencyMatrix();
        this.sourceCity = -1;
        this.destinationCity = -1;
        this.pathChecker = -1;

        if (transportationFirm.getRoutesList().isEmpty()) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("Eception");
        }
        for (Route route : transportationFirm.getRoutesList()) {
            for (int i = 0; i < citiesList.size(); i++) {
                if (route.getSourceCity().getCityName().equals(citiesList.get(i).getCityName())) {
                    routesList.add(i);

                    for (int j = 0; j < citiesList.size(); j++) {
                        if (route.getDestinationCity().getCityName().equals(citiesList.get(j).getCityName())) {
                            routesList.add(j);
                        }
                    }
                    routesList.add(route.getLeavingTime());
                    routesList.add(route.getDuration());
                }
            }
        }
        this.path = new ArrayList<>();
        for (int i = 0; i < citiesList.size() + 1; i++)
            path.add(0);

        this.startingHour = initializeStartingHour();


        readRouteFromKeyboard();
    }

    private Integer initializeStartingHour() {
        String localTime = LocalTime.now().toString();
        String[] localTimeParts = localTime.split(":");
        return Integer.parseInt(localTimeParts[0]) * 60 + Integer.parseInt(localTimeParts[1]);
    }

    public String getHour(Integer hour) {


        Integer routeLeavingTime = hour;
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

    private void readRouteFromKeyboard() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose your starting point between : ");

        for (City city : citiesList)
            System.out.print(city.getCityName() + " ");
        System.out.print("\n- ");
        String startingPoint = scanner.nextLine();
        for (int i = 0; i < citiesList.size(); i++) {
            if (startingPoint.equals(stripAccents(citiesList.get(i).getCityName()))) {
                sourceCity = i;
            }
        }
        if (sourceCity.equals(-1)) {
            IException exception = ExceptionFactory.getException("InvalidKeyboardInput");
            exception.throwException("Source city unavailable ");
        }
        System.out.print("Choose your destination point between : ");
        for (City city : citiesList) {
            if (!stripAccents(city.getCityName()).equals(startingPoint))
                System.out.print(city.getCityName() + " ");
        }
        System.out.print("\n- ");
        String destinationPoint = scanner.nextLine();
        for (int i = 0; i < citiesList.size(); i++) {
            if (destinationPoint.equals(stripAccents(citiesList.get(i).getCityName()))) {
                destinationCity = i;
            }
        }
        if (destinationCity.equals(-1)) {
            IException exception = ExceptionFactory.getException("InvalidKeyboardInput");
            exception.throwException("Destination city unavailable ");
        }

        if (sourceCity.equals(destinationCity)) {
            IException exception = ExceptionFactory.getException("InvalidKeyboardInput");
            exception.throwException("Source City is the same as Destination City");
        }

        this.path.set(0, sourceCity);
    }

    private static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    private boolean solution(int index) {
        if (path.get(index).equals(destinationCity)) {
            return true;
        }
        return false;
    }

    private boolean valid(int index, int index1) {
        if (path.get(index - 1).equals(routesList.get(index1))) {
            if (routesList.get(index1 + 2) >= startingHour) {
                return true;
            }
        }
        return false;
    }

    private void printPath(int index) {
        System.out.print(" - ");
        for (int i = 0; i <= index; i++) {
            System.out.print(citiesList.get(path.get(i)).getCityName() + "  ");

            pathChecker = 1;
        }
        System.out.println("");
    }

    public void backTracking(int index, int i) {

        for (i = 0; i < routesList.size(); i += 4) {
            path.set(index, routesList.get(i + 1));
            if (valid(index, i)) {
                if (index == 1) {
                    startingHour = routesList.get(i + 2) + routesList.get(i + 3) + 15;
                } else {
                    startingHour += routesList.get(i + 3) + 15;
                }

                if (solution(index)) {
                    printPath(index);
                    System.out.println("\t Leaving at: " + getHour(routesList.get(i + 2)));
                } else {
                    backTracking(index + 1, i);
                }

                if (index == 1) {
                    startingHour = initializeStartingHour();
                } else {
                    startingHour -= (routesList.get(i + 3) + 15);
                }
            }
        }
    }

    private void backTrackingAllPaths(int index, int i) {

        for (i = 0; i < routesList.size(); i += 4) {
            path.set(index, routesList.get(i + 1));
            if (valid(index, i)) {
                if (index == 1) {
                    startingHour = routesList.get(i + 2) + routesList.get(i + 3) + 15;
                } else {
                    startingHour += routesList.get(i + 3) + 15;
                }

                if (solution(index)) {
                    printPath(index);
                    System.out.println("\t Leaving at: " + getHour(routesList.get(i + 2)));
                } else {
                    backTracking(index + 1, i);
                }

                if (index == 1) {
                    startingHour = 0;
                } else {
                    startingHour -= (routesList.get(i + 3) + 15);
                }
            }
        }
    }

    public void printAllPaths(int i) {
        this.startingHour = 0;
        backTrackingAllPaths(1, i);
    }

    public static Integer getPathChecker() {
        return pathChecker;
    }
}
