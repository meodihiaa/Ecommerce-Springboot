package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;
    @Autowired

    private BCryptPasswordEncoder passwordEncoder;



    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Đăng nhập");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Đăng ký");
        model.addAttribute("customerDto", new CustomerDto());
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                  BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
            Customer customer = customerService.findByUsername(customerDto.getUsername());
            if (customer != null) {
                model.addAttribute("username", "Tên username đã tồn tại!");
                //Trả về customerDto để người dùng không cần nhập lại thông tin đã nhâp trước đó
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
            if (customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                model.addAttribute("success", "Đăng ký thành công!");
                return "register";
            } else {
                model.addAttribute("password", "Mật khẩu không khớp, kiểm tra lại!");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi máy chủ");
            model.addAttribute("customerDto", customerDto);
        }
        return "register";
    }
}
