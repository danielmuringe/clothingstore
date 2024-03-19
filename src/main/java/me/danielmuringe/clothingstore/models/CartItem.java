package me.danielmuringe.clothingstore.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartItem {

    public static void addCartItem(int user_id, int clothing_item_id, int quantity) {
        Reader.nullExecute("INSERT INTO cart_item (user_id, clothing_item_id, quantity) VALUES ('"
                + user_id + "', '" + clothing_item_id + "', '" + quantity + "')");
    }

    public static int getUserId(int id) {
        String result = Reader.getDatum("SELECT user_id FROM cart_item WHERE id=" + id, "user_id");
        return Integer.parseInt(result);
    }

    public static int getClothingItemId(int id) {
        String result = Reader.getDatum("SELECT clothing_item_id FROM cart_item WHERE id=" + id, "clothing_item_id");
        return Integer.parseInt(result);
    }

    public static float getQuantity(int id) {
        return Float.parseFloat(Reader.getDatum("SELECT quantity FROM cart_item WHERE id=" + id, "quantity"));
    }

    public static HashMap<String, Object> getItemDatum(int id) {
        HashMap<String, Object> itemData = new HashMap<>();

        float price = ClothingItem.getPrice(getClothingItemId(id));
        int quantity = (int) getQuantity(id);

        itemData.put("user_id", getUserId(id));
        itemData.put("clothing_item_id", getClothingItemId(id));
        itemData.put("quantity", quantity);
        itemData.put("name", ClothingItem.getName(getClothingItemId(id)));
        itemData.put("price", price);
        itemData.put("image", ClothingItem.getImage(getClothingItemId(id)));
        itemData.put("total", (price * quantity));

        return itemData;
    }

    public static List<HashMap<String, Object>> getUserCartsItemsData(int user_id) {
        List<HashMap<String, Object>> itemsData = new ArrayList<>();

        List<Integer> ids = Reader.getDataInt("cart_item", "id");

        for (int id : ids) {
            HashMap<String, Object> datum = getItemDatum(id);

            if (((int) datum.get("user_id") == user_id) && ((int) datum.get("quantity") > 0)) {
                itemsData.add(datum);
            }

        }

        return itemsData;
    }

    public static void updateQuantity(int id, int quantity) {
        Reader.nullExecute("UPDATE cart_item SET quantity='" + quantity + "' WHERE id=" + id);
    }

    public static void reduceQuantityByOne(int id) {
        int quantity = (int) getQuantity(id);
        if (quantity > 0) {
            Reader.nullExecute("UPDATE cart_item SET quantity='" + (quantity - 1) + "' WHERE id=" + id);
        }
    }

    public static void increaseQuantityByOne(int id) {
        int quantity = (int) getQuantity(id);
        Reader.nullExecute("UPDATE cart_item SET quantity='" + (quantity + 1) + "' WHERE id=" + id);
    }

    public static List<HashMap<String, Object>> getItemsData() {

        List<HashMap<String, Object>> itemsData = new ArrayList<>();
        List<Integer> ids = Reader.getDataInt("cart_item", "id");

        for (int id : ids) {
            itemsData.add(getItemDatum(id));
        }

        return itemsData;
    }

    public static void removeCartItem(int id) {
        Reader.nullExecute("DELETE FROM cart_item WHERE id=" + id);
    }

    public static void removeCartItemsByClothingItemId(int clothing_item_id) {
        Reader.nullExecute("DELETE FROM cart_item WHERE clothing_item_id=" + clothing_item_id);
    }

    public static boolean itemExists(int user_id, int clothing_item_id) {
        String result = Reader.getDatum(
                "SELECT id FROM cart_item WHERE user_id=" + user_id + " AND clothing_item_id=" + clothing_item_id,
                "id");
        return result != null;
    }

    public static int getId(int user_id, int clothing_item_id) {
        String result = Reader.getDatum(
                "SELECT id FROM cart_item WHERE user_id=" + user_id + " AND clothing_item_id=" + clothing_item_id,
                "id");
        return Integer.parseInt(result);
    }

}