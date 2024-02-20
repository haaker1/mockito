/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Coverage {
    /**
     * Store the branches reached for each specific function (identified by name, or other consistent method)
     */
    private static final HashMap<String, HashSet<Integer>> branchesReached = new HashMap<>();
    private static final HashMap<String, Integer> branchesTotal = new HashMap<>();
    public Coverage() {}

    /**
     * Set the total amount of branches for a given function identifier.
     * @param function The function identifier.
     * @param totalBranches The total amount of branches for that function.
     */
    public static void setTotalBranches(String function, int totalBranches) {
        branchesTotal.put(function, totalBranches);
    }

    /**
     * Marks the branch as having been reached at some point.
     * @param function The function in which the branch was reached.
     * @param branchID The unique identifier for the branch that was reached.
     */
    public static void reached(String function, int branchID) {
        if (!branchesReached.containsKey(function)) branchesReached.put(function, new HashSet<>());
        branchesReached.get(function).add(branchID);
        writeCoverage(function);
    }

    /**
     * Prints the branch coverage for a specific function. Undefined behaviour if totalBranches
     * is not positive.
     * @param function The name of the function which was previously used when denoting the branches as having been reached.
     */
    public static void printCoverage(String function) {
        System.out.println(getPrintCoverage(function));
    }

    /**
     * Write the current branch coverage to disk.
     * @param function The function identifier.
     */
    public static void writeCoverage(String function) {
        File f = new File("./build/coverage/" + function + ".txt");
        f.getParentFile().mkdirs();
        try {
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(getPrintCoverage(function));
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getTotalBranches(String function) {
        int totalBranches = 1;
        if (branchesTotal.get(function) != null) totalBranches = branchesTotal.get(function);
        if (totalBranches == 0) totalBranches = 1;
        return totalBranches;
    }

    private static String getPrintCoverage(String function) {
        String str = "============================\n";
        int totalBranches = getTotalBranches(function);
        int branches = branchesReached.get(function) != null ? branchesReached.get(function).size() : 0;
        str += String.format("Coverage for %s: %.1f%%\n", function, ((double) branches * 100) / totalBranches);
        str += "Taken branchIDs:";
        for (Integer branch : branchesReached.get(function)) {
            str += " " + branch;
        }
        str += "\n============================\n";
        return str;
    }
}
