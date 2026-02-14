package com.example.parser;

import com.example.model.Character;
import org.xml.sax.Parser;

import java.util.Collection;

public interface CollectionParser extends parser {
    Collection<Character> parseAll();
}
