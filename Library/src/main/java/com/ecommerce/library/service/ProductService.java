package com.ecommerce.library.service;


import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();
    Product save(ProductDto productDto, MultipartFile imageProduct);
    Product update(ProductDto productDto, MultipartFile imageProduct);
    void deleteById(Long id);
    void enabledById(Long id);
    ProductDto getById(Long id);
    Page<Product> findAll(int pageNo);

}
