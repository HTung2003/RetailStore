package controller;

import dto.request.ProductRequest;
import dto.response.ApiResponse;
import dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {

    ProductService productService;

    // Tạo sản phẩm mới
    @PostMapping
    ApiResponse<String> createProduct(@RequestBody @Valid ProductRequest request) {
        productService.createProduct(request);
        return ApiResponse.<String>builder()
                .data("Product created successfully")
                .build();
    }

    // Cập nhật sản phẩm
    @PostMapping("/update/{productId}")
    ApiResponse<String> updateProduct(@PathVariable String productId,
                                      @RequestBody @Valid ProductRequest request) {
        productService.updateProduct(productId, request);
        return ApiResponse.<String>builder()
                .data("Product updated successfully")
                .build();
    }

    // Lấy thông tin sản phẩm theo ID
    @GetMapping("/{productId}")
    ApiResponse<ProductResponse> getProductById(@PathVariable String productId) {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.getProductById(productId))
                .build();
    }

    // Lấy danh sách tất cả sản phẩm
    @GetMapping("/get-all-products")
    ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProducts())
                .build();
    }

    // Xóa sản phẩm
    @DeleteMapping("/{productId}")
    ApiResponse<String> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<String>builder()
                .data("Product deleted successfully")
                .build();
    }
}
