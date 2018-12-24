package data_io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PossiblesSumCombinations {
    private static Map<String, List<int[]>> possiblesSums = null;
    private static List<int[]> emptyList = new ArrayList<>();

    public static List<int[]> getPossiblesSumsCombinations(int sum, int fieldsCount){
        if (possiblesSums == null)
            loadCombinationsFromFile("examples/sums.txt");

        String key = String.format("%din%d", sum, fieldsCount);
        return Collections.unmodifiableList(possiblesSums.getOrDefault(key, emptyList));
    }

    private static void loadCombinationsFromFile(String filename){
        possiblesSums = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = br.readLine()) != null) {
                String[] splited_line = line.split(";");
                String key = splited_line[0];

                splited_line = splited_line[1].split("-");
                List<int[]> combinations = new ArrayList<>();

                for (String combination : splited_line){
                    int[] possibleSum = new int[combination.length()];
                    for (int i = 0; i < combination.length(); ++i){
                        possibleSum[i] = Character.getNumericValue(combination.charAt(i));
                    }
                    combinations.add(possibleSum);
                }
                possiblesSums.put(key, combinations);
            }
            br.close();
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
