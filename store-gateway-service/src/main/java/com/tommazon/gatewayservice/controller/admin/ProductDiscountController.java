package com.tommazon.gatewayservice.controller.admin;

import com.tommazon.gatewayservice.controller.AbstractRestController;
import com.tommazon.gatewayservice.controller.requestDto.ProductDiscountRequest;
import com.tommazon.gatewayservice.controller.responseDto.ProductDiscountResponse;
import com.tommazon.gatewayservice.entity.ProductDiscount;
import com.tommazon.gatewayservice.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/productdiscount")
@AllArgsConstructor
public class ProductDiscountController extends AbstractRestController {
    private final AdminService adminService;
    @PostMapping
    public ResponseEntity<ProductDiscountResponse> create(@RequestBody ProductDiscountRequest productDiscountRequest, @RequestParam(required=false) String refId) {
        ProductDiscount createdProductDiscount = adminService.addProductDiscount(productDiscountRequest);
        var builder = ProductDiscountResponse.builder();
        builder.refId(refId);
        builder.productDiscount(createdProductDiscount);
        return ResponseEntity.status(HttpStatus.CREATED).body(builder.build());
    }

    @PutMapping("/id/{productDiscountId}")
    public ResponseEntity<ProductDiscountResponse> update(@PathVariable Long productDiscountId, @RequestBody ProductDiscountRequest ProductDiscount, @RequestParam(required=false) String refId) {
        ProductDiscount createdProductDiscount = adminService.updateProductDiscount(productDiscountId,ProductDiscount);
        var builder = ProductDiscountResponse.builder();
        builder.refId(refId);
        builder.productDiscount(createdProductDiscount);
        return ResponseEntity.status(HttpStatus.OK).body(builder.build());
    }
    @PutMapping("/id/{productDiscountId}/{enabled}")
    public ResponseEntity<ProductDiscountResponse> toggle(@PathVariable Long productDiscountId, @PathVariable Boolean enabled, @RequestParam(required=false) String refId) {
        ProductDiscount createdProductDiscount = adminService.toggleProductDiscount(productDiscountId, enabled);
        var builder = ProductDiscountResponse.builder();
        builder.refId(refId);
        builder.productDiscount(createdProductDiscount);
        return ResponseEntity.status(HttpStatus.OK).body(builder.build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDiscountResponse>> getAll(@RequestParam(required=false) Boolean enabled,  @RequestParam(required=false) String refId) {
        List<ProductDiscount> result;
        if (enabled != null)
            result = adminService.getAllProductDiscountByEnabled(enabled);
        else
            result = adminService.getAllProductDiscount();

        List<ProductDiscountResponse> prs = new ArrayList<>(result.size());
        var prb = ProductDiscountResponse.builder();
        result.forEach(it -> {
            prb.productDiscount(it);
            prb.refId(refId);
            prs.add(prb.build());
        });
        return ResponseEntity.ok(prs);
    }

    @GetMapping("/id/{productDiscountId}")
    public ResponseEntity<ProductDiscountResponse> get(@PathVariable Long productDiscountId, @RequestParam(required=false) String refId) {
        Optional<ProductDiscount> pd = adminService.getProductDiscountById(productDiscountId);
        var pr = ProductDiscountResponse.builder();

        if (pd.isPresent()) {
            pr.productDiscount(pd.get());
            pr.refId(refId);
            return ResponseEntity.ok(pr.build());
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
