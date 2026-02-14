package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.example.model.Character;
public class TreeMapServ {
    protected TreeMap<String, List<String>> addTree(List<Character> characterList){

        TreeMap<String, List<String>> origin_species = new TreeMap<>();
        for (Character character : characterList){
            String origin = character.getOrigin();
            String species = character.getSpecies();
            origin_species.computeIfAbsent(origin, k -> new ArrayList<>()).add(species);
        }
        return origin_species;
    }

    public void printSpeciesMap(TreeMap<String, List<String>> map) {
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public void writeCSV(TreeMap<String, List<String>> map, String filename){

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Map.Entry<String,List<String>> entry : map.entrySet()){

                    String origin = entry.getKey();
                    List<String> species = entry.getValue();
                    String speciesList = String.join(";", species);

                    writer.write(String.format("\"%s\",\"%s\"",
                            escapeCSV(origin),
                            escapeCSV(speciesList)
                    ));
                    writer.newLine();
            }

        }catch (IOException e){
                System.err.println("Ошибка" + e.getLocalizedMessage());
            }
    }
    private String escapeCSV(String value) {
        if (value == null) return "";
        // Экранируем кавычки
        return value.replace("\"", "\"\"");
    }
}


