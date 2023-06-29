package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/products")
    public String products(Model model, Principal principal, HttpSession session) {
        model.addAttribute("title", "Products");
        List<Product> products = productService.getAllProducts();
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("products", products);
        model.addAttribute("viewProducts", listViewProducts);

        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
        model.addAttribute("categories", categories);
        if (principal != null) {
            session.setAttribute("username", principal.getName());
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getShoppingCart();
            if (cart == null) {
                cart = new ShoppingCart();
            }
            session.setAttribute("totalItems", cart.getTotalItems());
        } else {
            session.removeAttribute("username");
        }


        return "shop";
    }

    @GetMapping("/high-price")
    public String getHighPrice(Model model){
        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
        List<Product> products = productService.getHighPrice();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "high-price";
    }

    @GetMapping("/low-price")
    public String getLowPrice(Model model){
        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
        List<Product> products = productService.getLowPrice();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "low-price";
    }

    @GetMapping("/find-product/{id}")
    public String findProductById(@PathVariable("id") Long id, Model model, Principal principal) {
/*        if (principal == null) {
            return "redirect:/login";
        }*/
        Product product = productService.getProductById(id);
        model.addAttribute("title", product.getName());
        model.addAttribute("product", product);
        Long categoryId = product.getCategory().getId();
        List<Product> relatedProducts = productService.getRelatedProducts(categoryId);
        model.addAttribute("relatedProducts", relatedProducts);

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-detail";
    }

    @GetMapping("/find-products-in-category/{id}")
    public String findProductsInCategory(@PathVariable("id") Long id, Model model, Principal principal) {
        List<Product> productsInCategory = productService.getProductsInCategory(id);
        model.addAttribute("products", productsInCategory);
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
        model.addAttribute("categories", categories);
        return "products-in-category";
    }
}
