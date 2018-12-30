package data_io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PossiblesSumCombinations {
    private static ArrayList<ArrayList<List<int[]>>> possiblesSums = null;
    private static List<int[]> emptyList = new ArrayList<>();

    public static List<int[]> getPossiblesSumsCombinations(int sum, int fieldsCount) {
        if (possiblesSums == null)
            loadCombinationsFromFile("examples/sums.txt");

        final List<int[]> ints = Collections.unmodifiableList(possiblesSums.get(fieldsCount - 1).get(sum - 1));
        if (ints != null) {
            return ints;
        }
        return emptyList;
    }

    public static boolean[] getPossibilities(int sum, int fieldsCount) {
        boolean[] possibilities = new boolean[9];

        List<int[]> possibleCombinations = getPossiblesSumsCombinations(sum, fieldsCount);

        for (int[] combination : possibleCombinations)
            for (int digit : combination)
                possibilities[digit - 1] = true;

        return possibilities;
    }

    private static void loadCombinationsFromFile(String filename) {
        possiblesSums = new ArrayList<>();
        for (int i = 0; i != 9; ++i) {
            possiblesSums.add(new ArrayList<>());
            for (int j = 0; j != 45; ++j) {
                possiblesSums.get(i).add(new ArrayList<>());
            }
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = br.readLine()) != null) {
                String[] splited_line = line.split(";");
                String key = splited_line[0];
                String[] key1 = key.split("in");
                int sum = new Integer(key1[0]);
                int length = new Integer(key1[1]);
                System.out.println(sum + " " + length);

                splited_line = splited_line[1].split("-");
                List<int[]> combinations = new ArrayList<>();

                for (String combination : splited_line) {
                    int[] possibleSum = new int[combination.length()];
                    for (int i = 0; i < combination.length(); ++i) {
                        possibleSum[i] = Character.getNumericValue(combination.charAt(i));
                    }
                    combinations.add(possibleSum);
                }
                possiblesSums.get(length - 1).set(sum-1,combinations);
            }
            br.close();
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
