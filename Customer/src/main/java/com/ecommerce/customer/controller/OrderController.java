package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.AdminRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/check-out")
    public String checkOut(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);

        if (customer == null) {
            model.addAttribute("error", "Không tìm thấy thông tin khách hàng!");
            return "account";
        }

        if (customer.getPhoneNumber() == null || customer.getAddress() == null
                || customer.getCity() == null || customer.getCountry() == null) {
            model.addAttribute("error", "Bạn phải điền thông tin để thanh toán!");
            model.addAttribute("customer", customer);
            return "account";
        }
        ShoppingCart shoppingCart = customer.getShoppingCart();

        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
        }
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("title", "Thanh toán");

        return "checkout";
    }

    @GetMapping("/order")
    public String order(Principal principal, Model model, HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
        }
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
        List<Order> orderList = customer.getOrders();
        model.addAttribute("orders", orderList);
        if (orderList.size() == 0) model.addAttribute("check", "Không có đơn hàng nào!");
        model.addAttribute("title", "Đơn hàng");

        return "order";
    }

    @GetMapping("/save-order")
    public String saveOrder(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        orderService.saveOrder(shoppingCart);
        return "redirect:/order";
    }

    @RequestMapping(value = "/update-order", method = RequestMethod.POST, params = "action=cancel")
    public String cancelOrder(
            @RequestParam("id") Long id,
            Model model,
            Principal principal) {
        if (principal == null) return "redirect:/login";
        else {
            orderService.cancelOrder(id);
            return "redirect:/order";
        }
    }

    @RequestMapping(value = "/update-order", method = RequestMethod.POST, params = "action=accept")
    public String acceptOrder(
            @RequestParam("id") Long id,
            Model model,
            Principal principal) {
        if (principal == null) return "redirect:/login";
        else {
            orderService.acceptOrder(id);
            return "redirect:/order";
        }
    }
    }
