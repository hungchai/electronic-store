package com.tommazon.gatewayservice.controller.admin;

import com.tommazon.gatewayservice.controller.AbstractRestController;
import com.tommazon.gatewayservice.controller.responseDto.ProductResponse;
import com.tommazon.gatewayservice.entity.Product;
import com.tommazon.gatewayservice.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/product")
@AllArgsConstructor
public class ProductController extends AbstractRestController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody Product product, @RequestParam(required=false) String refId) {
        Product createdProduct = adminService.createProduct(product);
        var pr = ProductResponse.builder();
        pr.refId(refId);
        pr.product(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(pr.build());
    }

    @DeleteMapping("/id/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long productId, @RequestParam(required=false) String refId) {
        var deleteProduct = adminService.toggleProduct(productId, false);
        var pr = ProductResponse.builder();
        pr.refId(refId);
        pr.product(deleteProduct);
        pr.remark("TOGGLED OFF");
        return ResponseEntity.ok(pr.build());
    }

    @DeleteMapping("/sku/{skuId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable String skuId, @RequestParam String refId) {
        var deleteProduct = adminService.toggleProductBySku(skuId, false);
        var pr = ProductResponse.builder();
        pr.refId(refId);
        pr.product(deleteProduct);
        pr.remark("TOGGLED OFF");
        return ResponseEntity.ok(pr.build());
    }
    @PutMapping("/id/{productId}/{enabled}")
    public ResponseEntity<ProductResponse> toggleById(@PathVariable Long productId, @PathVariable Boolean enabled, @RequestParam(required=false) String refId) {
        Product updatedProduct = adminService.toggleProduct(productId, enabled);
        var pr = ProductResponse.builder();
        pr.product(updatedProduct);
        pr.refId(refId);
        return ResponseEntity.ok(pr.build());
    }


    @PutMapping("/sku/{sku}/{enabled}")
    public ResponseEntity<ProductResponse> toggleBySku(@PathVariable String sku, @PathVariable Boolean enabled, @RequestParam(required=false) String refId) {
        Product updatedProduct = adminService.toggleProductBySku(sku, enabled);
        var pr = ProductResponse.builder();
        pr.product(updatedProduct);
        pr.refId(refId);
        return ResponseEntity.ok(pr.build());
    }
    @PutMapping("/id/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody Product product, @RequestParam(required=false) String refId) {
        Product updatedProduct = adminService.updateProduct(productId, product);
        var pr = ProductResponse.builder();
        pr.product(updatedProduct);
        pr.refId(refId);
        return ResponseEntity.ok(pr.build());
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId, @RequestParam(required=false) String refId) {
        Optional<Product> product = adminService.getProductById(productId);
        var pr = ProductResponse.builder();

        if (product.isPresent()) {
            pr.product(product.get());
            pr.refId(refId);
            return ResponseEntity.ok(pr.build());
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySku(@PathVariable String sku, @RequestParam(required=false) String refId) {
        Optional<Product> product = adminService.getProductBySku(sku.toUpperCase());
        var pr = ProductResponse.builder();

        if (product.isPresent()) {
            pr.product(product.get());
            pr.refId(refId);
            return ResponseEntity.ok(pr.build());
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(required=false) Boolean enabled, @RequestParam(required=false) String refId) {
        List<Product> products;
        if (enabled != null)
            products = adminService.getAllProductByEnabled(enabled);
        else
            products = adminService.getAllProducts();

        List<ProductResponse> prs = new ArrayList<>(products.size());
        var prb =  ProductResponse.builder();
        products.forEach(it -> {
            prb.product(it);
            prb.refId(refId);
            prs.add(prb.build());
        });
        return ResponseEntity.ok(prs);
    }
}
