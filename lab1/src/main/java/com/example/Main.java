package com.example;

import com.example.CRUD.ArrayCharacterList;
import com.example.model.Character;
import com.example.parser.ArrayListCSVParser;
import com.example.CRUD.ArrayCharacterList;
import java.util.List;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        ArrayListCSVParser a = new ArrayListCSVParser("characters.csv");
        List<Character> arrayListUsers = a.parseAll();
        ArrayCharacterList CrudList = new ArrayCharacterList();
        CrudList.InitializeList(arrayListUsers);
        TreeMapServ n = new TreeMapServ();
        TreeMap<String, List<String>> FinalTreeMap = n.addTree(arrayListUsers);
        n.printSpeciesMap(FinalTreeMap);
        n.writeCSV(FinalTreeMap, "lab1/src/main/resources/output.csv");
        Character newCharacter = new Character(
                0,
                "Custom Character",
                "Alive",
                "Human",
                "Test",
                "Male",
                "Earth",
                "Earth",
                "2024-01-01"
        );
        CrudList.AddCharacter(newCharacter);
        CrudList.updateName(21, "Nikita");
        CrudList.printCRUD();
    }
}