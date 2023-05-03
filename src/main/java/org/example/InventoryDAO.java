package org.example;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class InventoryDAO {
    private final String fileName;
    private ArrayList<InventoryItem> inventory = new ArrayList<>();
    private ObjectMapper mapper;

    public InventoryDAO(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void readInventoryFromJson() throws JsonParseException, JsonMappingException, IOException {
        File file = new File(this.fileName);
        if (file.exists()) {
            InventoryItem[] itemsArray = mapper.readValue(file, InventoryItem[].class);
            this.inventory = new ArrayList<>(Arrays.asList(itemsArray));
        }
    }

    public void writeInventoryToJson() throws IOException {
        File file = new File(this.fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        mapper.writeValue(file, this.inventory);
    }

    public List<InventoryItem> getAllItems() {
        return this.inventory;
    }

    public InventoryItem getItemById(int id) {
        return this.inventory.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
    }

    public void addItem(InventoryItem item) throws IllegalArgumentException {

        if (this.inventory.stream().parallel().anyMatch(obj -> obj.getId() == item.getId())){
            System.out.println("An item with the same ID already exists in the inventory.");
        }
        else {
            System.out.println();
            inventory.add(item);
        }
    }

    public void updateItem(InventoryItem item) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getId() == item.getId()) {
                inventory.set(i, item);
                break;
            }
        }
    }

    public void deleteItem(int id) {
        this.inventory = (ArrayList<InventoryItem>) this.inventory.stream().filter(item -> item.getId() != id).collect(Collectors.toList());
    }
}