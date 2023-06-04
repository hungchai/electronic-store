package com.tommazon.gatewayservice.service;

import com.tommazon.gatewayservice.controller.requestDto.ProductDiscountRequest;
import com.tommazon.gatewayservice.entity.ConfigDiscount;
import com.tommazon.gatewayservice.entity.Product;
import com.tommazon.gatewayservice.entity.ProductDiscount;
import com.tommazon.gatewayservice.entity.respository.ConfigDiscountRepository;
import com.tommazon.gatewayservice.entity.respository.ProductDiscountRepository;
import com.tommazon.gatewayservice.entity.respository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService extends BaseService {
    private final ConfigDiscountRepository configDiscountRepository;
    private final ProductDiscountRepository productDiscountRepository;

    public AdminService(ProductRepository productRepository, ConfigDiscountRepository configDiscountRepository, ProductDiscountRepository productDiscountRepository) {
        super(productRepository);
        this.configDiscountRepository = configDiscountRepository;
        this.productDiscountRepository = productDiscountRepository;
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }



    public Optional<Product> getProductBySku(String sku) {
        return Optional.ofNullable(productRepository.findBySku(sku));
    }

    public List<Product> getAllProducts() {
        List<Product> result = new ArrayList<>();
        productRepository.findAll().forEach(result::add);
        return result;
    }


    @Transactional
    public Product updateProduct(Long productId, Product updatedProduct) {
        var product = getProductById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (updatedProduct.getName() != null) {
            product.setName(updatedProduct.getName());
        }
        if (updatedProduct.getPrice() != null) {
            product.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getEnabled() != null) {
            product.setEnabled(updatedProduct.getEnabled());
        }
        if (updatedProduct.getCcy() != null) {
            product.setCcy(updatedProduct.getCcy());
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product deleteProduct(Long productId) {
        var product = getProductById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productRepository.delete(product);
        return product;
    }
    public Product toggleProduct(Long productId, boolean enabled) {
        var product = getProductById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setEnabled(enabled);
        productRepository.save(product);
        return product;
    }
    @Transactional
    public Product toggleProductBySku(String skuId, Boolean enabled) {
        var product = getProductBySku(skuId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        toggleProduct(product.getId(),enabled);
        return product;
    }

    @Transactional
    public ConfigDiscount addConfigDiscount(ConfigDiscount configDiscount) {
        return configDiscountRepository.save(configDiscount);
    }
    public Optional<ConfigDiscount> getConfigDiscountById(Long configDiscountId) {
        return configDiscountRepository.findById(configDiscountId);
    }
    public List<ConfigDiscount> getAllConfigDiscounts() {
        List<ConfigDiscount> result = new ArrayList<>();
        configDiscountRepository.findAll().forEach(result::add);
        return result;
    }

    public List<ConfigDiscount> getAllConfigDiscountsByEnabled(Boolean enabled) {
        return configDiscountRepository.findAllByEnabled(enabled);
    }

    @Transactional
    public ConfigDiscount toggleConfigDiscount(Long configDiscountId, boolean enabled) {
        var configDiscount = getConfigDiscountById(configDiscountId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        configDiscount.setEnabled(enabled);
        configDiscountRepository.save(configDiscount);
        return configDiscount;
    }


    @Transactional
    public ConfigDiscount updateConfigDiscount(Long configDiscountId, ConfigDiscount updatedConfigDiscount) {
        var configDiscount = getConfigDiscountById(configDiscountId).orElseThrow(() -> new IllegalArgumentException("Config Discount not found"));
        if (updatedConfigDiscount.getDescription() != null) {
            configDiscount.setDescription(updatedConfigDiscount.getDescription());
        }
        if (updatedConfigDiscount.getDiscountPercentage() != null) {
            configDiscount.setDiscountPercentage(updatedConfigDiscount.getDiscountPercentage());
        }
        if (updatedConfigDiscount.getEnabled() != null) {
            configDiscount.setEnabled(updatedConfigDiscount.getEnabled());
            if (!updatedConfigDiscount.getEnabled())
            {
                List<ProductDiscount> pds = productDiscountRepository.findAllByConfigDiscount(configDiscount);
                pds.forEach(it -> {
                    it.setEnabled(updatedConfigDiscount.getEnabled());
                });
                productDiscountRepository.saveAll(pds);
            }
        }
        if (updatedConfigDiscount.getMinimumOrderQty() != null) {
            configDiscount.setMinimumOrderQty(updatedConfigDiscount.getMinimumOrderQty());
        }
        return configDiscountRepository.save(configDiscount);
    }

    @Transactional
    public ProductDiscount addProductDiscount(ProductDiscountRequest productDiscountRequest) {
        var builder = ProductDiscount.builder();
        builder.product(productRepository.findById(productDiscountRequest.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found")));
        builder.configDiscount(configDiscountRepository.findById(productDiscountRequest.getConfigDiscountId()).orElseThrow(() -> new IllegalArgumentException("Config Discount not found")));
        builder.enabled(productDiscountRequest.getEnabled());
        builder.description(productDiscountRequest.getDescription());
        return productDiscountRepository.save(builder.build());
    }
    @Transactional
    public ProductDiscount updateProductDiscount(Long productDiscountId, ProductDiscountRequest productDiscountRequest) {
        var productDiscount = getProductDiscountById(productDiscountId).orElseThrow(() -> new IllegalArgumentException("Product Discount Mapping not found"));
        if (productDiscountRequest.getProductId() != null)
            productDiscount.setProduct(productRepository.findById(productDiscountRequest.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found")));
        if (productDiscountRequest.getConfigDiscountId() != null)
            productDiscount.setConfigDiscount(configDiscountRepository.findById(productDiscountRequest.getConfigDiscountId()).orElseThrow(() -> new IllegalArgumentException("Config Discount not found")));
        if (productDiscountRequest.getEnabled() != null)
            productDiscount.setEnabled(productDiscountRequest.getEnabled());
        if (productDiscountRequest.getDescription() != null)
            productDiscount.setDescription(productDiscountRequest.getDescription());
        return productDiscountRepository.save(productDiscount);
    }
    public List<ProductDiscount> getAllProductDiscount() {
        List<ProductDiscount> result = new ArrayList<>();
        productDiscountRepository.findAll().forEach(result::add);
        return result;
    }

    public List<ProductDiscount> getAllProductDiscountByEnabled(Boolean enabled ) {
        return productDiscountRepository.findAllByEnabled(enabled);
    }
    public Optional<ProductDiscount> getProductDiscountById(Long productDiscountId) {
        return productDiscountRepository.findById(productDiscountId);
    }
    @Transactional
    public ProductDiscount toggleProductDiscount(Long productDiscountId, Boolean enabled) {
        var productDiscount = getProductDiscountById(productDiscountId).orElseThrow(() -> new IllegalArgumentException("Product Discount Mapping not found"));
        productDiscount.setEnabled(enabled);
        productDiscountRepository.save(productDiscount);
        return productDiscount;
    }
}
