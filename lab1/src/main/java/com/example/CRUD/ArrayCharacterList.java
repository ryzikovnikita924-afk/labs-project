package com.example.CRUD;

import com.example.model.Character;
import java.util.ArrayList;
import java.util.List;

public class ArrayCharacterList {
    List<Character> characters;
    int nextId = 1;


    public void InitializeList(List<Character> initialUsers) {
        this.characters = new ArrayList<>(initialUsers);
        this.nextId = initialUsers.stream()
                .mapToInt(Character::getID)
                .max()
                .orElse(0) + 1;
    }

    public void AddCharacter(Character NewCharacter) {
        NewCharacter.setId(nextId);
        this.nextId = nextId + 1;
        this.characters.add(NewCharacter);
    }

    public Character findById(int id) {
        for (Character character : characters) {
            if (character.getID() == id) {
                return character;
            }
        }
        return null;
    }

    public boolean update(Character updatedCharacter) {
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getID() == updatedCharacter.getID()) {
                characters.set(i, updatedCharacter);
                return true;
            }
        }
        return false;
    }


    public boolean updateName(int id, String newName) {
        Character character = findById(id);
        if (character != null) {
            character.setName(newName);
            return true;
        }
        return false;
    }

    public boolean updateStatus(int id, String newStatus) {
        Character character = findById(id);
        if (character != null) {
            character.setStatus(newStatus);
            return true;
        }
        return false;
    }

    public boolean updateSpecies(int id, String newSpecies) {
        Character character = findById(id);
        if (character != null) {
            character.setSpecies(newSpecies);
            return true;
        }
        return false;
    }

    public boolean updateType(int id, String newType) {
        Character character = findById(id);
        if (character != null) {
            character.setType(newType);
            return true;
        }
        return false;
    }

    public boolean updateGender(int id, String newGender) {
        Character character = findById(id);
        if (character != null) {
            character.setGender(newGender);
            return true;
        }
        return false;
    }

    public boolean updateOrigin(int id, String newOrigin) {
        Character character = findById(id);
        if (character != null) {
            character.setOrigin(newOrigin);
            return true;
        }
        return false;
    }

    public boolean updateLocation(int id, String newLocation) {
        Character character = findById(id);
        if (character != null) {
            character.setLocation(newLocation);
            return true;
        }
        return false;
    }

    public boolean updateCreated(int id, String newCreated) {
        Character character = findById(id);
        if (character != null) {
            character.setCreated(newCreated);
            return true;
        }
        return false;
    }

    public boolean deleteCharacter(int id){
        Character character = findById(id);
        if(character != null) {
            characters.remove(character);
            nextId = nextId - 1;
            for (int i = id + 1; id < nextId; i++) {
                Character c = findById(i);
                c.setId(i-1);
            }
        }
        return false;
    }
    public void printCRUD(){
        for (Character character : characters){
            System.out.println(character);
        }
    }
}