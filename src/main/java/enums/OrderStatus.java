package enums;

public enum OrderStatus {
    PENDING,        // Chờ xử lý
    PROCESSING,     // Đang xử lý
    SHIPPED,        // Đã giao cho đơn vị vận chuyển
    DELIVERED,      // Đã giao thành công
    CANCELED,       // Đã hủy
    RETURNED        // Trả hàng
}
