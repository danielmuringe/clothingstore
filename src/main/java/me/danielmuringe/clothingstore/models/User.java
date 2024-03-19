package me.danielmuringe.clothingstore.models;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    public class Hasher {

        public static String hashPassword(String password) throws NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));

            String hashedString = bytesToHex(encodedhash);
            return hashedString;
        }

        private static String bytesToHex(byte[] hash) {
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }

        public static boolean checkHash(String password, String hash) throws NoSuchAlgorithmException {
            return hash.equals(hashPassword(password));
        }
    }

    public static void addUser(String username, String password, String email) {

        try {
            String hash = Hasher.hashPassword(password);
            Reader.nullExecute(
                    "INSERT INTO user (username, hash, email, group_id) VALUES ('" + username + "', '" + hash + "', '"
                            + email + "', 2)");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public static String getUsername(int id) {
        return Reader.getDatum("SELECT username FROM user WHERE id=" + id, "username");
    }

    public static String getHash(int id) {
        return Reader.getDatum("SELECT hash FROM user WHERE id=" + id, "hash");
    }

    public static String getEmail(int id) {
        return Reader.getDatum("SELECT email FROM user WHERE id=" + id, "email");
    }

    public static int getId(String username) {
        return Integer.parseInt(Reader.getDatum("SELECT id FROM user WHERE username='" + username + "'", "id"));
    }

    public static int getGroupId(int id) {
        return Integer.parseInt(Reader.getDatum("SELECT group_id FROM user WHERE id=" + id, "group_id"));
    }

    public static void updateEmail(int id, String email) {
        Reader.nullExecute("UPDATE user SET email='" + email + "' WHERE id=" + id);
    }

    public static void updateHash(int id, String password) {
        try {
            String hash = Hasher.hashPassword(password);
            Reader.nullExecute("UPDATE user SET hash='" + hash + "' WHERE id=" + id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void updateUsername(int id, String username) {
        Reader.nullExecute("UPDATE user SET username='" + username + "' WHERE id=" + id);
    }

    public static void removeUser(int id) {
        Reader.nullExecute("DELETE FROM user WHERE id=" + id);
    }

    public static boolean checkPassword(String username, String password) {
        try {
            String hash = Hasher.hashPassword(password);
            return hash.equals(getHash(getId(username)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean usernameExists(String username) {
        String result = Reader.getDatum("SELECT username FROM user WHERE username='" + username + "'", "username");
        return result != null;
    }

    public static boolean emailExists(String email) {
        String result = Reader.getDatum("SELECT email FROM user WHERE email='" + email + "'", "email");
        return result != null;
    }

    public static boolean isAdmin(int id) {
        String result = Reader.getDatum("SELECT group_id FROM user WHERE id=" + id, "group_id");
        return result.equals("1");
    }

}
