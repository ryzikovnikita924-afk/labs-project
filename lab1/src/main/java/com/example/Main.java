package com.example;

import com.example.model.Character;
import com.example.parser.ArrayListCSVParser;

import java.util.List;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        ArrayListCSVParser a = new ArrayListCSVParser("characters.csv");
        List<Character> arrayListUsers = a.parseAll();
        TreeMapServ n = new TreeMapServ();
        TreeMap<String, List<String>> FinalTreeMap = n.addTree(arrayListUsers);
        n.printSpeciesMap(FinalTreeMap);
        n.writeCSV(FinalTreeMap, "lab1/src/main/resources/output.csv");

    }
}