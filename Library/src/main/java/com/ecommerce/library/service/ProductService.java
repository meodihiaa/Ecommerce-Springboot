package com.ecommerce.library.service;


import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();
    Product save(ProductDto product, MultipartFile imageProduct);
    Product update(ProductDto product);
    void deleteById(Long id);
    void enabledById(Long id);

}
