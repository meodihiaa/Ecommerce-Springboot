package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String products(Model model){
        model.addAttribute("title","Products");
        List<Product> products = productService.getAllProducts();
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("products",products);
        model.addAttribute("viewProducts",listViewProducts);
        return "shop";
    }

    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        try {
            /*List<Category> categories = categoryService.findAllByActivated();*/

            Product product = productService.getProductById(id);
            model.addAttribute("title", product.getName());
            /*model.addAttribute("categories", categories);*/
            model.addAttribute("product", product);
        } catch (Exception e) {

        }
        return "product-detail";
    }
}
