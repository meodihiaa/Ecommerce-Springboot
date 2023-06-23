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
    public String products(Model model) {
        model.addAttribute("title", "Products");
        List<Product> products = productService.getAllProducts();
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("products", products);
        model.addAttribute("viewProducts", listViewProducts);
        return "shop";
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
        model.addAttribute("relatedProducts",relatedProducts);

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-detail";
    }
}
