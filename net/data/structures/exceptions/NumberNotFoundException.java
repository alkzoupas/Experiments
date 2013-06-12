package net.data.structures.exceptions;

/**
 * An exception that is thrown when the given number is not found in any interval in the tree.
 */
public class NumberNotFoundException extends RuntimeException {

    private int number;

    /**
     * Creates a new NumberNotFoundException passing in the constructor the number that was not found.
     * @param number the number that was not found
     */
    public NumberNotFoundException (int number) {

        this.number = number;

    }

    /**
     * Returns the number that was not found.
     * @return the number that was not found
     */
    public int getNumber() {

        return number;

    }

}
