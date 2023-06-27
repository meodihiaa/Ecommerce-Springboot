package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private CartItemRepository itemRepository;

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Override
    public ShoppingCart addItemToCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        if (cart == null) {
            cart = new ShoppingCart();
        }
        // lấy tất cả sản phẩm trong giỏ hàng
        Set<CartItem> cartItems = cart.getCartItem();
        // tìm xem sản phẩm cần thêm vào giỏ hàng đã có trong giỏ hàng chưa
        CartItem cartItem = findCartItem(cartItems, product.getId());
        // giỏ hàng hiện tại chưa có sản phẩm nào
        if (cartItems == null) {
            cartItems = new HashSet<>();
            // nếu sản phẩm cần thêm chưa có trong giỏ hàng
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setCart(cart);
                // thêm sản phẩm cần thêm (chưa có) vào giỏ hàng
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            }
        } else {
            // giỏ hàng đã có một số sản phẩm
            // nếu sản phẩm cần thêm chưa có trong giỏ hàng
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            } else {
                // nếu sản phẩm cần thêm ĐÃ có trong giỏ hàng
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.setTotalPrice(cartItem.getTotalPrice() + quantity * product.getCostPrice());
                itemRepository.save(cartItem);
            }
        }
            cart.setCartItem(cartItems);

            int totalItems = totalItems(cart.getCartItem());
            double totalPrice = totalPrice(cart.getCartItem());

            cart.setTotalItems(totalItems);
            cart.setTotalPrices(totalPrice);
            cart.setCustomer(customer);
            return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart updateItemInCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItems = cart.getCartItem();
        CartItem cartItem = findCartItem(cartItems, product.getId());

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * product.getCostPrice());
        itemRepository.save(cartItem);

        int totalItems = totalItems(cartItems);
        double totalPrice = totalPrice(cartItems);

        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);

        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart deleteItemFromCart(Product product, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItems = cart.getCartItem();
        CartItem cartItem = findCartItem(cartItems, product.getId());

        cartItems.remove(cartItem);
        itemRepository.delete(cartItem);

        int totalItems = totalItems(cartItems);
        double totalPrice = totalPrice(cartItems);

        cart.setCartItem(cartItems);

        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);

        return cartRepository.save(cart);
    }


    private CartItem findCartItem(Set<CartItem> cartItems, Long productId) {
        if (cartItems == null) {
            return null;
        }
        CartItem cartItem = null;

        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    private int totalItems(Set<CartItem> cartItems) {
        int totalItems = 0;
        for (CartItem item : cartItems) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    private double totalPrice(Set<CartItem> cartItems) {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}
