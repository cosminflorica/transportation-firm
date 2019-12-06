package ro.mta.se.lab.exception;

import ro.mta.se.lab.factory.IException;

public class InvalidAdjacencyVector implements IException {
    @Override
    public void throwException(String exceptionMessage) {
        System.out.println(getClass().getName() + " Exception: " + exceptionMessage);
        System.exit(0);
    }
}
