package net.data.structures;

import net.data.structures.exceptions.IntersectingIntervalException;
import net.data.structures.exceptions.InvalidIntervalException;
import net.data.structures.exceptions.NumberNotFoundException;

/**
 * A class representing the integer interval. It also serves as a tree node.
 * It implements the Comparable interface and provides a compareTo method.
 * Comparisons are made purely based on the values of the interval boundaries.
 * There are information stored concerning the interval (left  and right boundary, and the value).
 * It also holds information concerning the 'left' and 'right' child of the tree node.
 * Provides a find method which will traverse recursively all child nodes.
 */

public class Interval implements Comparable<Interval> {

    private int left;
    private int right;
    private String value;
    private Interval leftInterval;
    private Interval rightInterval;

    /**
     * Creates a new Interval instance provided the left and right integer boundaries and the assigned string value.
     * @param left the left boundary of the interval
     * @param right the right boundary of the internal
     * @param value the string value
     * @throws InvalidIntervalException if the right boundary is smaller than the left
     */
    public Interval (int left, int right, String value) throws InvalidIntervalException {

        if ( !(left < right) ) {
            throw new InvalidIntervalException();
        } else {
            this.left = left;
            this.right = right;
            this.value = value;
        }

    }

    /**
     * Check to see if the integer can be found in the interval.
     * @param number the integer that we want to see if it is contained in the interval
     * @return true if the integer is inside the interval else false
     */
    public boolean contains (int number) {

        if ( this.getLeft() < number && number < this.getRight() )
            return true;

        return false;

    }

    /**
     * Check to see if the integer is equal to the upper boundary of this interval.
     * @param number the integer that we want to check
     * @return true if the integer is equal else false
     */
    public boolean isUpperBoundary (int number) {

        if ( number == this.getRight() )
            return true;

        return false;

    }

    /**
     * Check to see if the integer is equal to the lower boundary of this interval.
     * @param number the integer that we want to check
     * @return true if the integer is equal else false
     */
    public boolean isLowerBoundary (int number) {

        if ( this.getLeft() == number )
            return true;

        return false;

    }

    /**
     * Given the integer, first we check if it is contained in the current interval or if it is equal
     * to the interval's boundaries. If not we proceed to check the intervals which are the children
     * of the given one. This process continues recursively until an appropriate interval is found.
     * If no interval is found then a new NumberNotFoundException is thrown.
     * @param number the integer that we want to check
     * @return the interval that contains this number
     * @throws NumberNotFoundException if the number can not be found
     */
    public Interval find(int number) throws NumberNotFoundException {

        if (this.contains(number))
            return this;

        if (this.isLowerBoundary(number) )
            return this;

        if (this.isUpperBoundary(number) )
            return this;

        if ( number < this.getLeft() && this.getLeftInterval()!=null )
            return this.getLeftInterval().find(number);

        if ( number > this.getRight() && this.getRightInterval()!=null )
            return this.getRightInterval().find(number);

        throw new NumberNotFoundException(number);

    }

    /**
     * This is the compareTo method of the Comparable interface.
     * Extra caution must be taken because two different exceptions can be thrown.
     * A NullPointerException is thrown if the parameter is null.
     * An IntersectingIntervalException is thrown if the two intervals are found to be intersecting.
     * We work under the assumption that our intervals are not intersecting therefore this is considered to be an illegal state.
     * @param interval the interval to compare with
     * @return -1, 0, 1 if the interval is smaller, equal or larger to the one in the parameter
     * @throws NullPointerException if the parameter is null
     * @throws IntersectingIntervalException if the intervals are intersecting
     */
    public int compareTo(Interval interval) throws NullPointerException, IntersectingIntervalException{

        if ( null == interval )
            throw new NullPointerException();

        if ( this.getLeft() == interval.getLeft() && this.getRight() == interval.getRight() )
            return 0;

        if ( this.getRight() <= interval.getLeft() )
            return -1;

        if ( this.getLeft() >= interval.getRight() )
            return 1;

        throw new IntersectingIntervalException();

    }

    /**
     * Getters and Setters used for the variables.
     */
    public int getLeft() {

        return left;

    }

    public void setLeft(int left) {

        this.left = left;

    }

    public int getRight() {

        return right;

    }

    public void setRight(int right) {

        this.right = right;

    }

    public String getValue() {

        return value;

    }

    public Interval getRightInterval() {

        return rightInterval;

    }

    public void setRightInterval(Interval rightInterval) {

        this.rightInterval = rightInterval;

    }

    public Interval getLeftInterval() {

        return leftInterval;

    }

    public void setLeftInterval(Interval leftInterval) {

        this.leftInterval = leftInterval;

    }


}
