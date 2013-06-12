package net.data.structures;

import net.data.structures.exceptions.IntersectingIntervalException;
import net.data.structures.exceptions.NumberNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a simple balanced binary tree whose nodes are instances of the class Interval.
 * There are no methods provided to alter the tree after it has been created, therefor in a way this tree is static.
 * The name might be misleading because this is not a pure IntervalTree (http://en.wikipedia.org/wiki/Interval_tree).
 * Since we make the assumption that we do not have overlapping intervals then we can fall back to a binary tree,
 * which can be queried in O(log n) time.
 */
public class IntervalTree {

    private Interval root;
    private List<Interval> intervalsStoredInTree;
    private boolean leftClosed;

    /**
     * Creates a new Interval tree instance given the interval list.
     * If we want the intervals to be left or right closed can be set using the isLeftClosed parameter.
     * If they are left closed then the intervals are not right closed and vice versa.
     * The provided intervals are first validated to see if they are intersecting before the tree is created.
     * If all goes well then the balance method is called, which will eventually decide on a root node and create the tree structure.
     * @param intervals a list of the intervals that will be stored in this tree
     * @param isLeftClosed if this tree is left or right closed
     * @throws IntersectingIntervalException if there are intervals that are intersected in the interval list provided
     */
    public IntervalTree( List<Interval> intervals, boolean isLeftClosed ) throws IntersectingIntervalException {

        this.leftClosed = isLeftClosed;
        this.intervalsStoredInTree = this.manipulateBoundaries(intervals, this.leftClosed);
        this.validateIntervals(this.intervalsStoredInTree);
        this.root = null;
        this.balance();

    }

    /**
     * Same with the previous constructor only that the intervals are considered left closed by default.
     * @param intervals a list of the intervals that will be stored in this tree
     * @throws IntersectingIntervalException if there are intervals that are intersected in the interval list provided
     */
    public IntervalTree( List<Interval> intervals ) throws IntersectingIntervalException {

        this.validateIntervals(intervals);
        this.intervalsStoredInTree = intervals;
        this.root = null;
        balance();

    }

    /**
     * In order to make the balancing and the search algorithm of the tree simpler we manipulate the interval boundaries.
     * Based on if they are left or right closed we make the appropriate boundary smaller by 1.
     * For example the intervals [1,5), [5,8), [8,14) will be eventually stored in the tree as [1,4), [5,7), [8,13).
     * @param intervals a list of the intervals that will be stored in this tree
     * @param isLeftClosed if the intervals will be left or right closed
     * @return the updated list of the intervals
     */
    private List<Interval> manipulateBoundaries (List<Interval> intervals, boolean isLeftClosed) {

        List<Interval> newIntervals = new ArrayList<Interval>();

        for(Interval interval: intervals) {

            if ( isLeftClosed == true )
                interval.setRight(interval.getRight() - 1);
            else
                interval.setLeft(interval.getLeft() - 1);

            newIntervals.add(interval);

        }

        return newIntervals;

    }

    /**
     * This method validates all intervals provided for the tree to see if there are any intersecting or duplicate ones.
     * @param intervals a list of the intervals that will be stored in this tree
     * @throws IntersectingIntervalException if there are intersecting or duplicate intervals provided
     */
    private void validateIntervals (List<Interval> intervals) throws IntersectingIntervalException {

        for(int i =0; i<intervals.size()-1; i++) {
            try {

                int comparisonResult = intervals.get(i).compareTo(intervals.get(i+1));
                if ( comparisonResult == 0 )
                    throw new IntersectingIntervalException();
            } catch (IntersectingIntervalException ex) {

                System.out.println("No intersecting intervals allowed in this Map!");
                throw ex;

            }
        }

    }

    /**
     * This method creates and balances the tree. First we start by finding the median of all interval boundaries.
     * Based on that we proceed to split all intervals provided for the tree to three different categories.
     * First we identify which one is the root node of the tree. Since the intervals are not intersecting there is
     * only one node that will either contain or have boundaries equal to the median value. This is the node
     * that will be set as a root node for the tree. All other nodes are split in two main categories.
     * The nodes that their right boundary is smaller than the median value are set in a new list and the nodes that
     * their left boundary is bigger than the median value are set in an other list. Then we recursively create new
     * trees for these nodes. It is important to note that while creating these new trees we do not have to set if their
     * boundaries are left or right closed since this has been taken care already with the creation of the first tree.
     * What we end up is with a balanced binary tree which can be queried in O(log n) time.
     */
    public void balance () {

        SortedSet<Long> endpoints = new TreeSet<Long>();

        for(Interval interval: this.intervalsStoredInTree) {
            endpoints.add(new Long(interval.getLeft()));
            endpoints.add(new Long(interval.getRight()));
        }

        long median = getMedian(endpoints);

        List<Interval> left = new ArrayList<Interval>();
        List<Interval> right = new ArrayList<Interval>();

        for(Interval interval : this.intervalsStoredInTree) {

            if( (interval.contains((int)median) || interval.isLowerBoundary((int)median) || interval.isUpperBoundary((int)median)) ) {

                this.root = interval;

            } else {

                if(interval.getRight() < median) {
                    left.add(interval);
                } else {
                    if(interval.getLeft() > median)
                        right.add(interval);
                }

            }
        }

        if(left.size() > 0)
            this.root.setLeftInterval(new IntervalTree(left).getRoot());
        if(right.size() > 0)
            this.root.setRightInterval(new IntervalTree(right).getRoot());

    }

    /**
     * This method calculates the median value of all boundaries and helps us find an appropriate root node for the tree.
     * @param set the boundaries of all the intervals that will be stored in the tree
     * @return the median value
     */
    private Long getMedian(SortedSet<Long> set) {

        int i = 0;
        int middle = set.size() / 2;

        for(Long point : set) {
            if(i == middle)
                return point;
            i++;
        }

        return null;

    }

    /**
     * This method starts with the tree root node and tries to find the given number.
     * @param number the number we want to find
     * @return the string value stored in the interval containing the number
     * @throws NumberNotFoundException if the number was not found in all intervals in the tree
     */
    public String find(int number) throws NumberNotFoundException {

        if (this.root == null)
            throw new NumberNotFoundException(number);
        else
            return this.root.find(number).getValue();

    }

    /**
     * Methods that are used to return variables of the tree.
     */
    public boolean isLeftClosed() {

        return this.leftClosed;

    }

    public boolean isRightClosed() {

        return !this.leftClosed;

    }

    public Interval getRoot() {

        return this.root;

    }

}
