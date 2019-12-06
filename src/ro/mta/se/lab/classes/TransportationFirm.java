package ro.mta.se.lab.classes;

import ro.mta.se.lab.factory.ExceptionFactory;
import ro.mta.se.lab.factory.IException;
import ro.mta.se.lab.paths.FindPaths;
import ro.mta.se.lab.city.City;

import java.util.ArrayList;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TransportationFirm {

    private static TransportationFirm INSTANCE = null;
    private ArrayList<Route> routesList;
    private ArrayList<City> citiesList;
    private ArrayList<Integer> adjacencyMatrix;
    private FindPaths paths;

    private TransportationFirm(String fileName) {
        try {
            readFile(fileName);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static TransportationFirm getInstance(String fileName) {

        if (INSTANCE == null) {
            INSTANCE = new TransportationFirm(fileName);
        }
        return INSTANCE;
    }

    private void readFile(String file) throws IOException {
        try {
            ArrayList<String> fileLines = new ArrayList<String>();
            String line;

            FileInputStream fis = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            while ((line = br.readLine()) != null) {
                fileLines.add(line);
            }

            parseFile(fileLines);
            br.close();


        } catch (IOException exception) {
            System.out.println("Error while reading file: " + exception.getMessage() + ";");
        }

    }

    private void parseFile(ArrayList<String> fileLines) {
        if (fileLines == null) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("File is empty!");
        }
        if (fileLines.isEmpty()) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("File is empty!");
        }

        parseFirstLine(fileLines.get(0));
        parseSecondLine(fileLines.get(1));

        parseRoutes(fileLines);
    }


    private void parseFirstLine(String firstLine) {
        if (firstLine == null) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("Null exception!");
        }
        if (firstLine.isEmpty()) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("Null exception");
        }

        String validPattern = ".[^, \\n]*([ ]*,[ ]*.[^, \\n]*)+";
        Pattern pattern = Pattern.compile(validPattern);
        Matcher matcher = pattern.matcher(firstLine);

        if (!matcher.find()) {
            IException exception = ExceptionFactory.getException("InvalidInput");
            exception.throwException("First line of the input file doesn't match given pattern");
        } else {
            if (!matcher.group().equals(firstLine)) {
                IException exception = ExceptionFactory.getException("InvalidInput");
                exception.throwException("First line of the input file doesn't match given pattern");
            }
        }
        firstLine = firstLine.replaceAll(" ", "");
        String[] cities = firstLine.split("[,]");

        populateCitiesList(cities);


    }

    private void parseSecondLine(String secondLine) {
        //get the string between [ ]
        if (secondLine == null) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("Second line null");
        }
        if (secondLine.isEmpty()) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("Second line empty");
        }

        String validPattern = "[\\[][ ]*[01]([ ]*[01])*[\\]]";
        Pattern pattern = Pattern.compile(validPattern);
        Matcher matcher = pattern.matcher(secondLine);
        if (!matcher.find()) {
            IException exception = ExceptionFactory.getException("InvalidAdjacencyVector");
            exception.throwException("Second line of the input file doesn't match given pattern");
        }
        String matrixLine = secondLine.substring(secondLine.indexOf("[") + 1, secondLine.indexOf("]"));

        String[] matrix = matrixLine.split(" +");
        if (matrix.length != Math.pow(citiesList.size(), 2)) {
            IException exception = ExceptionFactory.getException("InvalidAdjacencyVector");
            exception.throwException("Second line has worng number of elements");
        }
        this.adjacencyMatrix = new ArrayList<>();
        for (String matrixElement : matrix) {
            if (Integer.parseInt(matrixElement) != 1 && Integer.parseInt(matrixElement) != 0) {
                IException exception = ExceptionFactory.getException("InvalidAdjacencyVector");
                exception.throwException("Incorrect value");
            }
            this.adjacencyMatrix.add(Integer.parseInt(matrixElement));
        }
    }

    private void parseRoutes(ArrayList<String> fileLines) {
        if (fileLines == null) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("fileLines null");
        }
        if (fileLines.isEmpty()) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("fileLines empty");
        }
        if (fileLines.size() > citiesList.size() + 2) {
            IException exception = ExceptionFactory.getException("InvalidInput");
            exception.throwException("Incorrect number of lines");
        }
        if (fileLines.size() < citiesList.size() + 2) {
            IException exception = ExceptionFactory.getException("InvalidInput");
            exception.throwException("Incorrect number of lines");
        }
        routesList = new ArrayList<>();

        for (int i = 2; i < fileLines.size(); i++) {

            String line = fileLines.get(i);
            if (line == null) {
                IException exception = ExceptionFactory.getException("NullException");
                exception.throwException("line null");
            }

            String[] routes = line.split("[,]");

            for (int j = 0; j < routes.length; j++) {
                String routeString = routes[j].substring(routes[j].indexOf("[") + 1, routes[j].indexOf("]"));
                String[] routeDetails = routeString.split(" +");
                if (routeDetails.length != 3) {
                    IException exception = ExceptionFactory.getException("InvalidRouteException");
                    exception.throwException("Line " + ++i + " contains too many route parameters");
                }
                boolean checker = false;
                for (City testCity : citiesList) {
                    if (testCity.getCityName().equals(routeDetails[0])) {
                        checker = true;
                    }
                }
                if (!checker) {
                    IException exception = ExceptionFactory.getException("InvalidRouteException");
                    exception.throwException("Line " + ++i + " contains an invalid city name !");
                }

                City destinationCity = new City(routeDetails[0]);
                City sourceCity = new City(citiesList.get(i - 2).getCityName());

                Route route = new Route(sourceCity, destinationCity, Integer.parseInt(routeDetails[1]), Integer.parseInt(routeDetails[2]));
                route.getRouteState().setAvailable();
                routesList.add(route);
            }
        }
    }

    public void findPaths() {
        this.paths = new FindPaths(this);
        int i = 0;
        System.out.println("\nRoutes available for today: ");
        paths.backTracking(1, i);

        if (paths.getPathChecker().equals(-1)) {
            System.out.println("Sorry, but there are no available routes for today! Here you have the usual ones: ");
            System.out.println("\nRoutes available daily: ");
            paths.printAllPaths(i);
        } else {
            System.out.println("\nRoutes available daily: ");
            paths.printAllPaths(i);
        }
    }

    private void populateCitiesList(String[] cities) {

        if (cities == null) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("populateCitiesList receive null cities ");
        }

        citiesList = new ArrayList<>();
        for (String city : cities) {
            City newCity = new City(city);
            citiesList.add(newCity);
        }
    }

    public ArrayList<Route> getRoutesList() {
        return routesList;
    }

    public ArrayList<City> getCitiesList() {
        return citiesList;
    }

    public ArrayList<Integer> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }


}
