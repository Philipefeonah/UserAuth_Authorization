package com.maxiflexy.springsecurityjwt.controller;

import com.maxiflexy.springsecurityjwt.dto.ReqRes;
import com.maxiflexy.springsecurityjwt.entity.Product;
import com.maxiflexy.springsecurityjwt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUsersController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/public/product")
    public ResponseEntity<Object> getAllProducts(){
        return ResponseEntity.ok(productRepository.findAll());
    }


    @PostMapping("/admin/saveProduct")
    public ResponseEntity<Object> saveProduct(@RequestBody ReqRes productRequest){
        Product product = new Product();
        product.setProductName(productRequest.getName());
        return ResponseEntity.ok(productRepository.save(product));
    }

    @GetMapping("/user/alone")
    public ResponseEntity<Object> userAlone(){
        return ResponseEntity.ok("Users alone can access this API only");
    }

    @GetMapping("/admin-user")
    public ResponseEntity<Object> bothAdminAndUserApi(){
        return ResponseEntity.ok(" Both Admin and user can access this API");
    }
}
