package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("title", "Manage Products");
        model.addAttribute("products",productDtoList);
        model.addAttribute("size",productDtoList.size());
        return "products";
    }

    @GetMapping("/add-product")
    public  String addProductForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("title","Add Product");
        model.addAttribute("categories",categories);
        model.addAttribute("product", new ProductDto());
        return "add-product";
    }

    @PostMapping("save-product")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto,
                              @RequestParam("imageProduct")MultipartFile imageProduct,
                              RedirectAttributes attributes) {
        try {
            productService.save(productDto, imageProduct);
            attributes.addFlashAttribute("success", "Add product successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to add!");
        }
        return "redirect:/products";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        try {
            List<Category> categories = categoryService.findAllByActivated();
            model.addAttribute("title","Update product");
            ProductDto productDto = productService.getById(id);
            model.addAttribute("categories", categories);
            model.addAttribute("productDto", productDto);
        } catch (Exception e) {

        }
        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes attributes) {
        try {
            productService.update(productDto, imageProduct);
            attributes.addFlashAttribute("success", "Update successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error","Fail to update!");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/delete-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteProduct(Long id, RedirectAttributes attributes) {
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to delete product");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/enable-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enableProduct(Long id, RedirectAttributes attributes) {
        try {
            productService.enabledById(id);
            attributes.addFlashAttribute("success", "Enabled successfully");

        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to enable product");
        }
        return "redirect:/products";
    }
}
