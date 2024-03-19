package me.danielmuringe.clothingstore.controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import me.danielmuringe.clothingstore.models.CartItem;
import me.danielmuringe.clothingstore.models.ClothingItem;
import me.danielmuringe.clothingstore.models.Purchase;
import me.danielmuringe.clothingstore.models.User;

@Controller
public class Main {

    @Autowired
    public HttpSession session;

    public boolean isLoggedIn() {
        return session.getAttribute("username") != null;
    }

    public boolean isAdmin() {
        return User.isAdmin(User.getId((String) session.getAttribute("username")));
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "");
            return "signup";
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password, @RequestParam String email,
            Model model) {
        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            if (!User.usernameExists(username)) {
                User.addUser(username, password, email);
                session.setAttribute("username", username);
                return "redirect:/";
            } else {
                model.addAttribute("error", "Username exists");
                return "signup";
            }
        }
    }

    @GetMapping("/login")
    public String login() {
        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            return "login";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {

        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            if (!User.usernameExists(username)) {
                model.addAttribute("error", "Invalid username or password");
                return "login";
            } else if (User.checkPassword(username, password)) {
                session.setAttribute("username", username);
                return "redirect:/";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "login";
            }
        }
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/")
    public String home(Model model) {
        if (isLoggedIn()) {
            int userId = User.getId((String) session.getAttribute("username"));
            model.addAttribute("products", ClothingItem.getItemsData());
            model.addAttribute("isAdmin", User.isAdmin(userId));
            List<HashMap<String, Object>> cartItems = CartItem.getUserCartsItemsData(userId);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("fromPage", "home");

            float cartItemsTotal = 0;
            for (HashMap<String, Object> cartItem : cartItems) {
                cartItemsTotal += (float) cartItem.get("total");
            }
            model.addAttribute("cartItemsTotal", cartItemsTotal);
            return "home";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/home")
    public String homeExplicit(Model model) {
        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/catalog/{productName}")
    public String catalog(@PathVariable String productName, Model model) {
        if (isLoggedIn()) {
            int productId = ClothingItem.getId(productName);
            int userId = User.getId((String) session.getAttribute("username"));
            model.addAttribute("product", ClothingItem.getItemDatum(productId));
            model.addAttribute("isAdmin", User.isAdmin(userId));
            List<HashMap<String, Object>> cartItems = CartItem.getUserCartsItemsData(userId);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("fromPage", "catalog/" + productName);

            float cartItemsTotal = 0;
            for (HashMap<String, Object> cartItem : cartItems) {
                cartItemsTotal += (float) cartItem.get("total");
            }
            model.addAttribute("cartItemsTotal", cartItemsTotal);

            return "catalog";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/cart-item/add/{productName}")
    public String addToCart(@PathVariable String productName, Model model) {
        if (isLoggedIn()) {
            int productId = ClothingItem.getId(productName);
            int userId = User.getId((String) session.getAttribute("username"));

            if (CartItem.itemExists(userId, productId)) {
                CartItem.increaseQuantityByOne(CartItem.getId(userId, productId));
            } else {
                CartItem.addCartItem(userId, productId, 1);
            }
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/cart-item/remove/{productName}")
    public String removeFromCart(@PathVariable String productName, Model model) {
        if (isLoggedIn()) {
            int productId = ClothingItem.getId(productName);
            int userId = User.getId((String) session.getAttribute("username"));
            CartItem.reduceQuantityByOne(CartItem.getId(userId, productId));
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/clothing-items")
    public String addClothingItem(Model model) {
        if (isLoggedIn() && isAdmin()) {
            model.addAttribute("products", ClothingItem.getItemsData());
            return "clothing-items";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/clothing-items/add")
    public String addClothingItem(
            @RequestParam String name,
            @RequestParam String brand,
            @RequestParam String category,
            @RequestParam Float price,
            @RequestParam String size,
            @RequestParam String color,
            @RequestParam MultipartFile image,
            Model model) throws IOException {
        if (isLoggedIn() && isAdmin()) {
            String imageName = image.getOriginalFilename();
            String imagePath = "src/main/resources/static/img/" + imageName;
            File imageFile = new File(imagePath);
            try {
                image.transferTo(imageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ClothingItem.addClothingItem(name, brand, category, (float) price, size, color, imageName);
            return "redirect:/clothing-items";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/clothing-items/remove/{productName}")
    public String removeClothingItem(@PathVariable String productName, Model model) {
        if (isLoggedIn() && isAdmin()) {
            int clothingItemId = ClothingItem.getId(productName);
            Purchase.removePurchasesByClothingItemId(clothingItemId);
            CartItem.removeCartItemsByClothingItemId(clothingItemId);
            ClothingItem.deleteClothingItem(clothingItemId);
            return "redirect:/clothing-items";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/purchase")
    public String purchase() {
        if (isLoggedIn()) {
            int userId = User.getId((String) session.getAttribute("username"));

            for (HashMap<String, Object> cartItem : CartItem.getUserCartsItemsData(userId)) {
                int cartItemId = (int) CartItem.getId(userId, (int) cartItem.get("clothing_item_id"));
                Purchase.addPurchase(userId, (int) cartItem.get("clothing_item_id"), (int) cartItem.get("quantity"));
                CartItem.removeCartItem(cartItemId);
            }

            return "redirect:/";

        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/purchases")
    public String purchases(Model model) {
        if (isLoggedIn()) {

            int userId = User.getId((String) session.getAttribute("username"));
            List<HashMap<String, Object>> purchaseItems = Purchase.getUserPurchasesData(userId);
            model.addAttribute("purchases", purchaseItems);
            return "purchases";

        } else {
            return "redirect:/login";
        }
    }

}
