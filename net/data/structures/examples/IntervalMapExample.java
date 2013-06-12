package net.data.structures.examples;

import net.data.structures.Interval;
import net.data.structures.IntervalTree;
import net.data.structures.exceptions.NumberNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class IntervalMapExample {

    public static void main(String [ ] args) {

        problemDescriptionExample();

        advanced();

    }

    /*
    * A simple method which recreates the example provided in the problem description.
     */
    public static void problemDescriptionExample () {

        List<Interval> intervals = new ArrayList<Interval>();

        intervals.add(new Interval(1, 10, "a"));
        intervals.add(new Interval(10, 20, "b"));
        intervals.add(new Interval(20, 30, "c"));

        IntervalTree tree = new IntervalTree(intervals, true);

        try {

            System.out.println("Number 1 was found and the value is '" + tree.find(1) + "' !");
            System.out.println("Number 15 was found and the value is '" + tree.find(15) + "' !");
            System.out.println("Number 20 was found and the value is '" + tree.find(20) + "' !");
            System.out.println("Number 30 was found and the value is '" + tree.find(30) + "' !");

        } catch (NumberNotFoundException e){
            System.out.println("Number " + e.getNumber() + " was not found!");
        }

    }

    /*
    * A more advanced method.
     */
    public static void advanced () {

        List<Interval> intervals = new ArrayList<Interval>();

        intervals.add(new Interval(-10, -5, "r"));
        intervals.add(new Interval(-5, -2, "q"));
        intervals.add(new Interval(-2, 1, "z"));
        intervals.add(new Interval(1, 4, "x"));
        intervals.add(new Interval(4, 7, "f"));
        intervals.add(new Interval(7, 10, "a"));
        intervals.add(new Interval(10, 16, "e"));
        intervals.add(new Interval(16, 20, "t"));

        IntervalTree tree = new IntervalTree(intervals, true);

        try {

            System.out.println("Number 2 was found and the value is '" + tree.find(2) + "' !");
            System.out.println("Number -9 was found and the value is '" + tree.find(-9) + "' !");
            System.out.println("Number -2 was found and the value is '" + tree.find(-2) + "' !");
            System.out.println("Number 10 was found and the value is '" + tree.find(10) + "' !");
            System.out.println("Number 17 was found and the value is '" + tree.find(17) + "' !");

        } catch (NumberNotFoundException e){
            System.out.println("Number " + e.getNumber() + " was not found!");
        }

    }

}
