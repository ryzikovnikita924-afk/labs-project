package com.example.parser;

import com.example.model.Character;
import com.example.parser.CollectionParser;
import com.example.parser.LineCSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArrayListCSVParser extends LineCSVParser implements CollectionParser {

    private final String fileName;

    public ArrayListCSVParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Character> parseAll() {
        List<Character> collection = new ArrayList<>();

        try (
                InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(resourceAsStream));
                BufferedReader br = new BufferedReader(inputStreamReader)
        ) {

            String line = br.readLine();
            while (line != null) {
                Character character = parseLine(line);
                if (character != null) {
                    collection.add(character);
                }
                line = br.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Возникла проблема при работе с файлом: " + fileName, e);
        }

        return collection;
    }

    @Override
    public Character parse() {
        return parseAll().getFirst();
    }
}

