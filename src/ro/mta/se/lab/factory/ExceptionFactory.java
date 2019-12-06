package ro.mta.se.lab.factory;

import ro.mta.se.lab.exception.*;

public class ExceptionFactory {
    public static IException getException(String exceptionType) {
        if (exceptionType == null) {
            return null;
        }
        if (exceptionType.equalsIgnoreCase("FileEmpty")) {
            return new FileEmpty();
        }
        if (exceptionType.equalsIgnoreCase("InvalidAdjacencyVector")) {
            return new InvalidAdjacencyVector();
        }
        if (exceptionType.equalsIgnoreCase("InvalidInput")) {
            return new InvalidInput();
        }
        if (exceptionType.equalsIgnoreCase("InvalidKeyboardInput")) {
            return new InvalidKeyboardInput();
        }
        if (exceptionType.equalsIgnoreCase("InvalidRouteException")) {
            return new InvalidRouteException();
        }
        if (exceptionType.equalsIgnoreCase("InvalidTownException")) {
            return new InvalidTownException();
        }
        if (exceptionType.equalsIgnoreCase("NoRoutesException")) {
            return new NoRoutesException();
        }
        if (exceptionType.equalsIgnoreCase("NullException")) {
            return new NullException();
        }
        return null;
    }
}
