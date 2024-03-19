package me.danielmuringe.clothingstore.models;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class ClothingItem {

    public static void addClothingItem(String name, String brand, String category, Float price, String size,
            String color,
            String image) {
        Reader.nullExecute("INSERT INTO clothing_item (name, brand, category, price, size, color, image) VALUES ('"
                + name + "', '" + brand + "', '" + category + "', " + price + ", '" + size + "', '" + color + "', '"
                + image + "')");
    }

    public static int getId(String name) {
        String result = Reader.getDatum("SELECT id FROM clothing_item WHERE name='" + name + "'", "id");
        return Integer.parseInt(result);
    }

    public static String getName(int id) {
        return Reader.getDatum("SELECT name FROM clothing_item WHERE id=" + id, "name");
    }

    public static String getBrand(int id) {
        return Reader.getDatum("SELECT brand FROM clothing_item WHERE id=" + id, "brand");
    }

    public static String getCategory(int id) {
        return Reader.getDatum("SELECT category FROM clothing_item WHERE id=" + id, "category");
    }

    public static float getPrice(int id) {
        return Float.parseFloat(Reader.getDatum("SELECT price FROM clothing_item WHERE id=" + id, "price"));
    }

    public static String getSize(int id) {
        return Reader.getDatum("SELECT size FROM clothing_item WHERE id=" + id, "size");
    }

    public static String getColor(int id) {
        return Reader.getDatum("SELECT color FROM clothing_item WHERE id=" + id, "color");
    }

    public static String getImage(int id) {
        return "/img/" + Reader.getDatum("SELECT image FROM clothing_item WHERE id=" + id, "image");
    }

    public static HashMap<String, Object> getItemDatum(int id) {
        HashMap<String, Object> itemData = new HashMap<>();
        itemData.put("name", getName(id));
        itemData.put("brand", getBrand(id));
        itemData.put("category", getCategory(id));
        itemData.put("price", getPrice(id));
        itemData.put("size", getSize(id));
        itemData.put("color", getColor(id));
        itemData.put("image", getImage(id));

        return itemData;

    }

    public static List<HashMap<String, Object>> getItemsData() {

        List<HashMap<String, Object>> itemsData = new ArrayList<>();
        List<Integer> ids = Reader.getDataInt("clothing_item", "id");

        for (int id : ids) {
            itemsData.add(getItemDatum(id));
        }

        return itemsData;

    }

    public static void updateName(int id, String name) {
        Reader.nullExecute("UPDATE clothing_item SET name='" + name + "' WHERE id=" + id);
    }

    public static void updateBrand(int id, String brand) {
        Reader.nullExecute("UPDATE clothing_item SET brand='" + brand + "' WHERE id=" + id);
    }

    public static void updateCategory(int id, String category) {
        Reader.nullExecute("UPDATE clothing_item SET category='" + category + "' WHERE id=" + id);
    }

    public static void updatePrice(int id, int price) {
        Reader.nullExecute("UPDATE clothing_item SET price=" + price + " WHERE id=" + id);
    }

    public static void updateSize(int id, int size) {
        Reader.nullExecute("UPDATE clothing_item SET size=" + size + " WHERE id=" + id);
    }

    public static void updateColor(int id, String color) {
        Reader.nullExecute("UPDATE clothing_item SET color='" + color + "' WHERE id=" + id);
    }

    public static void deleteClothingItem(int id) {
        Reader.nullExecute("DELETE FROM clothing_item WHERE id=" + id);
    }

}