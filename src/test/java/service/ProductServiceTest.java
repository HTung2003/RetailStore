package service;

import dto.request.ProductRequest;
import dto.response.ProductResponse;
import entity.Product;
import exception.AppException;
import exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import repository.ProductRepository;
import service.ProductService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductRequest request = new ProductRequest();
        Product product = new Product();

        when(modelMapper.map(request, Product.class)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        productService.createProduct(request);

        verify(modelMapper).map(request, Product.class);
        verify(productRepository).save(product);
    }

    @Test
    void testUpdateProductSuccess() {
        String productId = "prod1";
        ProductRequest request = new ProductRequest();
        Product existingProduct = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        doNothing().when(modelMapper).map(request, existingProduct);
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        productService.updateProduct(productId, request);

        verify(productRepository).findById(productId);
        verify(modelMapper).map(request, existingProduct);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void testUpdateProductFailNotFound() {
        String productId = "notExist";
        ProductRequest request = new ProductRequest();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> {
            productService.updateProduct(productId, request);
        });

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, ex.getErrorCode());
        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testGetProductByIdSuccess() {
        String productId = "prod1";
        Product product = new Product();
        ProductResponse response = new ProductResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductResponse.class)).thenReturn(response);

        ProductResponse result = productService.getProductById(productId);

        assertEquals(response, result);
        verify(productRepository).findById(productId);
        verify(modelMapper).map(product, ProductResponse.class);
    }

    @Test
    void testGetProductByIdFailNotFound() {
        String productId = "notExist";

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> {
            productService.getProductById(productId);
        });

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, ex.getErrorCode());
        verify(productRepository).findById(productId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = Arrays.asList(new Product(), new Product());
        ProductResponse response1 = new ProductResponse();
        ProductResponse response2 = new ProductResponse();

        when(productRepository.findAll()).thenReturn(productList);
        when(modelMapper.map(productList.get(0), ProductResponse.class)).thenReturn(response1);
        when(modelMapper.map(productList.get(1), ProductResponse.class)).thenReturn(response2);

        List<ProductResponse> responses = productService.getAllProducts();

        assertEquals(2, responses.size());
        assertTrue(responses.contains(response1));
        assertTrue(responses.contains(response2));
        verify(productRepository).findAll();
        verify(modelMapper).map(productList.get(0), ProductResponse.class);
        verify(modelMapper).map(productList.get(1), ProductResponse.class);
    }

    @Test
    void testDeleteProductSuccess() {
        String productId = "prod1";
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(productId);

        verify(productRepository).findById(productId);
        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProductFailNotFound() {
        String productId = "notExist";

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> {
            productService.deleteProduct(productId);
        });

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, ex.getErrorCode());
        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }
}
