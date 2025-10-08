package com.example.RetailStore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(9998, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1001, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_BLANK(1002, "Username is blank", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_BLANK(1004, "Password is blank", HttpStatus.BAD_REQUEST),
    PHONE_NOT_BLANK(1005, "Phone is blank", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_BLANK(1006, "Email is blank", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1007, "Phone number only enter numbers", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1008, "Email is invalid", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_BLANK(1009, "Address is blank", HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXISTS(1010, "Username already exist", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1011, "User not existed", HttpStatus.NOT_FOUND),
    EMAIL_NOT_EXISTED(1012, "Email not existed", HttpStatus.NOT_FOUND),
    INCORRECT_PASSWORD(401, "Incorrect password", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_BLANK(1013, "Role is blank", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1014, "Product not found", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(1015, "Cart not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(1016, "Cart item not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(1017, "Order not found", HttpStatus.NOT_FOUND),
    ORDER_ITEM_NOT_FOUND(1018, "Order item not found", HttpStatus.NOT_FOUND),


;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
    this.code = code;
    this.message = message;
    this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
