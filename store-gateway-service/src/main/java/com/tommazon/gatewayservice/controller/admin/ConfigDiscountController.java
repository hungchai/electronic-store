package com.tommazon.gatewayservice.controller.admin;

import com.tommazon.gatewayservice.controller.AbstractRestController;
import com.tommazon.gatewayservice.controller.responseDto.ConfigDiscountResponse;
import com.tommazon.gatewayservice.entity.ConfigDiscount;
import com.tommazon.gatewayservice.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/discount/config")
@AllArgsConstructor
public class ConfigDiscountController extends AbstractRestController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<ConfigDiscountResponse> create(@RequestBody ConfigDiscount configDiscount, @RequestParam String refId) {
        ConfigDiscount createdConfigDiscount = adminService.addConfigDiscount(configDiscount);
        ConfigDiscountResponse.ConfigDiscountResponseBuilder builder = ConfigDiscountResponse.builder();
        builder.refId(refId);
        builder.configDiscount(createdConfigDiscount);
        return ResponseEntity.status(HttpStatus.CREATED).body(builder.build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ConfigDiscountResponse>> getAll(@RequestParam(required=false) Boolean enabled, @RequestParam(required=false) String refId) {
        List<ConfigDiscount> result;
        if (enabled != null)
            result = adminService.getAllConfigDiscountsByEnabled(enabled);
        else
            result = adminService.getAllConfigDiscounts();

        List<ConfigDiscountResponse> prs = new ArrayList<>(result.size());
        var prb = ConfigDiscountResponse.builder();
        result.forEach(it -> {
            prb.configDiscount(it);
            prb.refId(refId);
            prs.add(prb.build());
        });
        return ResponseEntity.ok(prs);
    }

    @PutMapping("/id/{configDiscountId}")
    public ResponseEntity<ConfigDiscountResponse> update(@PathVariable Long configDiscountId, @RequestBody ConfigDiscount updateConfigDiscount, @RequestParam String refId) {
        ConfigDiscount cd = adminService.updateConfigDiscount(configDiscountId, updateConfigDiscount);
        ConfigDiscountResponse.ConfigDiscountResponseBuilder builder = ConfigDiscountResponse.builder();
        builder.refId(refId);
        builder.configDiscount(cd);
        return ResponseEntity.ok(builder.build());
    }

    @PutMapping("/id/{configDiscountId}/{enabled}")
    public ResponseEntity<ConfigDiscountResponse> toggle(@PathVariable Long configDiscountId, @PathVariable Boolean enabled, @RequestParam(required=false) String refId) {
        var cd = adminService.toggleConfigDiscount(configDiscountId, enabled);
        ConfigDiscountResponse.ConfigDiscountResponseBuilder builder = ConfigDiscountResponse.builder();
        builder.refId(refId);
        builder.configDiscount(cd);
        builder.remark("TOGGLED OFF");
        return ResponseEntity.ok(builder.build());
    }
    @GetMapping("/id/{configDiscountId}")
    public ResponseEntity<ConfigDiscountResponse> get(@PathVariable Long configDiscountId, @RequestParam(required=false) String refId) {
        Optional<ConfigDiscount> cd = adminService.getConfigDiscountById(configDiscountId);
        var pr = ConfigDiscountResponse.builder();

        if (cd.isPresent()) {
            pr.configDiscount(cd.get());
            pr.refId(refId);
            return ResponseEntity.ok(pr.build());
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
