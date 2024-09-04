package com.taskcli;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

enum Status {
    TODO, IN_PROGRESS, DONE
}

public class Main {
    private static final String FILE_NAME = "items.json";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final Gson GSON = new Gson();
    private static final Map<String, String> STATUS_MAP = new HashMap<>();

    static {
        for (Status status : Status.values()) {
            STATUS_MAP.put(status.name().toLowerCase(), status.name().toLowerCase());
        }
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {

        if (args.length == 0) return;

            String action = args[0];
            JsonArray itemsArray = new JsonArray();
            Integer maxId = 0;

            try (FileReader reader = new FileReader(FILE_NAME)) {
                JsonParser jsonParser = new JsonParser();
                itemsArray = (JsonArray) jsonParser.parse(reader);
                for (int i = 0; i < itemsArray.size(); i++) {
                    JsonObject item = itemsArray.get(i).getAsJsonObject();
                    int currentId = item.get("id").getAsInt();
                    if (currentId > maxId) {
                        maxId = currentId;
                    }
                }
            } catch (Exception e) {
                System.out.println("No existing file found, creating a new one.");
            }

            switch (action) {
                case "add" -> {
                    addTask(itemsArray, args[1], ++maxId);
                    saveItems(itemsArray);
                    break;
                }
                case "update" -> {
                    updateTask(itemsArray, Integer.parseInt(args[1]), args[2]);
                    saveItems(itemsArray);
                    break;
                }
                case "delete" -> {
                    deleteTask(itemsArray, Integer.parseInt(args[1]));
                    saveItems(itemsArray);
                    break;
                }
                case "mark-in-progress" ->{
                    updateTaskStatus(itemsArray, Integer.parseInt(args[1]), Status.IN_PROGRESS);
                    saveItems(itemsArray);
                    return;
                }
                case "mark-done" ->{
                    updateTaskStatus(itemsArray, Integer.parseInt(args[1]), Status.DONE);
                    saveItems(itemsArray);
                    break;
                }
                case "list" -> {
                    listTasks(itemsArray, args.length == 2 ? args[1] : null);
                    break;
                }
                default -> System.out.println("Invalid Action");
            }

    }

    private static void listTasks(JsonArray itemsArray, String status) {
        boolean taskFound = false;

        if (status == null) {
            System.out.println("All Tasks:");
            taskFound = true;
            itemsArray.forEach(item -> System.out.println("[ ] - " + 
            item.getAsJsonObject().get("description")));
        } else if ("done".equals(status) || "todo".equals(status) || "in_progress".equals(status)) {

            System.out.println(status.toUpperCase());
            for(var item: itemsArray) {
                JsonObject jsonItem = item.getAsJsonObject();
                if (jsonItem.get("status").getAsString().equals(status)) {
                    taskFound = true;
                    System.out.println("[ ] - " + jsonItem.get("description"));
                }
            }
        }

        if (!taskFound) {
            System.out.println("No Tasks Found");            
        }
    }

    private static void addTask(JsonArray itemsArray, String taskDescription, int id) {
        LocalDateTime now = LocalDateTime.now();
        JsonObject newItem = new JsonObject();
        newItem.addProperty("id", id);
        newItem.addProperty("description", taskDescription);
        newItem.addProperty("status", STATUS_MAP.get(Status.TODO.name().toLowerCase()));
        newItem.addProperty("createdAt", now.format(DATE_FORMAT));
        newItem.addProperty("updatedAt", now.format(DATE_FORMAT));
        itemsArray.add(newItem);
        System.out.println("Task added successfully ID: " + newItem.get("id"));
    }

    private static void updateTask(JsonArray itemsArray, int id, String newDescription){
        LocalDateTime now = LocalDateTime.now();
        for (var item: itemsArray) {
            JsonObject jsonItem = item.getAsJsonObject();
            if (jsonItem.get("id").getAsInt() == id) {
                jsonItem.remove("description");
                jsonItem.remove("updatedAt");
                jsonItem.addProperty("updatedAt", now.format(DATE_FORMAT));
                jsonItem.addProperty("description", newDescription);
                System.out.println("Task updated successfully ID: " + id);
                return;
            }
        }

        System.out.println("Task with ID: " + id + " not found.");
    }

    private static void deleteTask(JsonArray itemsArray, int id) {

        for (int i = 0; i < itemsArray.size(); i++) {
            JsonObject item = itemsArray.get(i).getAsJsonObject();
            if(item.get("id").getAsInt() == id) {
                itemsArray.remove(i);
                System.out.println("Task deleted successfully.");
                return;
            }
        }

        System.out.println("Task with ID: " + id + " not found.");
    }

    private static void updateTaskStatus(JsonArray itemsArray, int id, Status status) {
        LocalDateTime now = LocalDateTime.now();
        for (var item: itemsArray) {
            JsonObject jsonItem = item.getAsJsonObject();
            if (jsonItem.get("id").getAsInt() == id) {
                jsonItem.remove("status");
                jsonItem.remove("updatedAt");
                jsonItem.addProperty("status", STATUS_MAP.get(status.name().toLowerCase()));
                jsonItem.addProperty("updatedAt", now.format(DATE_FORMAT));
                System.out.println("Task " + id + " marked as " + status);
                return;
            }
        }
    
        System.out.println("Task with ID: " + id + " not found.");
    }
    

    private static void saveItems(JsonArray itemsArray) {
        System.out.println("Saving tasks to " + FILE_NAME);
        try (FileWriter file = new FileWriter(FILE_NAME)) {
            file.write(GSON.toJson(itemsArray));
            file.flush();
            System.out.println("Tasks saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
    
}