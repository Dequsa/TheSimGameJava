package Tools;

import java.io.*;

public class ObjectManager {

    public Object loadGameState(String filename) {
        try (FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            Object loadedObject = objectIn.readObject();
            System.out.println("Game state loaded successfully.");
            return loadedObject;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game state: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public void saveGameState(String filename, Object objectToSave) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(objectToSave);
            System.out.println("Game state saved successfully.");

        } catch (IOException e) {
            System.err.println("Error saving game state: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
