package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class CartController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session) {
        if (principal == null) return "redirect:/login";
        else {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            if (shoppingCart == null) {
                shoppingCart = new ShoppingCart();
            }
            session.setAttribute("totalItems", shoppingCart.getTotalItems());
            if (shoppingCart.getCartItem().size() == 0) {
                model.addAttribute("check", "Không có sản phẩm trong giỏ hàng");
            }
            model.addAttribute("subTotal", shoppingCart.getTotalPrices());
            model.addAttribute("shoppingCart", shoppingCart);
            model.addAttribute("title", "Giỏ hàng");
            return "cart";
        }
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(
            @RequestParam("id") Long productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            Principal principal,
            Model model,
            HttpServletRequest request) {
        if (principal == null) return "redirect:/login";
        else {
            Product product = productService.getProductById(productId);
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            ShoppingCart cart = cartService.addItemToCart(product, quantity, customer);
            return "redirect:" + request.getHeader("Referer");
        }

    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItemFromCart(
            @RequestParam("id") Long productId,
            Model model,
            Principal principal) {
        if (principal == null) return "redirect:/login";
        else {
            Product product = productService.getProductById(productId);
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            ShoppingCart cart = cartService.deleteItemFromCart(product, customer);
            model.addAttribute("shoppingCart", cart);
            return "redirect:/cart";

        }
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("id") Long productId,
                             @RequestParam("quantity") int quantity,
                             Model model,
                             Principal principal) {
        if (principal == null) return "redirect:/login";
        else {
            Product product = productService.getProductById(productId);
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            ShoppingCart cart = cartService.updateItemInCart(product, quantity, customer);
            model.addAttribute("shoppingCart", cart);
            return "redirect:/cart";
        }

    }
}
