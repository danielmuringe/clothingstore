package me.danielmuringe.clothingstore.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Purchase {

    public static void addPurchase(int userId, int clothingItemId, int quantity) {
        Reader.nullExecute("INSERT INTO purchase (user_id, clothing_item_id, quantity) VALUES (" + userId + ", "
                + clothingItemId + ", " + quantity + ")");
    }

    public static int getUserId(int id) {
        return Integer.parseInt(Reader.getDatum("SELECT user_id FROM purchase WHERE id=" + id, "user_id"));
    }

    public static int getClothingItemId(int id) {
        return Integer
                .parseInt(Reader.getDatum("SELECT clothing_item_id FROM purchase WHERE id=" + id, "clothing_item_id"));
    }

    public static float getQuantity(int id) {
        return Float.parseFloat(Reader.getDatum("SELECT quantity FROM purchase WHERE id=" + id, "quantity"));
    }

    public static String getTimeStamp(int id) {
        String result = Reader.getDatum("SELECT purchase_date FROM purchase WHERE id=" + id, "purchase_date");
        result = result.replace(" ", ";");
        result = result.replace("-", "/");
        return result;
    }

    public static void removePurchase(int id) {
        Reader.nullExecute("DELETE FROM purchase WHERE id=" + id);
    }

    public static HashMap<String, Object> getPurchaseDatum(int id) {
        HashMap<String, Object> purchaseData = new HashMap<>();

        int clothingItemId = getClothingItemId(id);
        int quantity = (int) getQuantity(id);
        float price = ClothingItem.getPrice(clothingItemId);

        purchaseData.put("user_id", getUserId(id));
        purchaseData.put("clothing_item_id", clothingItemId);
        purchaseData.put("name", ClothingItem.getName(clothingItemId));
        purchaseData.put("image", ClothingItem.getImage(clothingItemId));
        purchaseData.put("price", price);
        purchaseData.put("quantity", quantity);
        purchaseData.put("purchase_date", getTimeStamp(id));
        purchaseData.put("total", (price * quantity));

        return purchaseData;
    }

    public static List<HashMap<String, Object>> getUserPurchasesData(int userId) {

        List<HashMap<String, Object>> purchasesData = new ArrayList<>();
        List<Integer> ids = Reader.getDataInt("purchase", "id");
        for (int id : ids) {
            HashMap<String, Object> datum = getPurchaseDatum(id);
            if ((int) datum.get("user_id") == userId) {
                purchasesData.add(datum);
            }
        }

        return purchasesData;

    }

    public static void removePurchasesByClothingItemId(int clothingItemId) {
        Reader.nullExecute("DELETE FROM purchase WHERE clothing_item_id=" + clothingItemId);
    }

}