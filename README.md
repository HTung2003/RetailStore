# 🛒 RetailStore - Backend hệ thống bán lẻ

**RetailStore** là một hệ thống backend RESTful API dành cho ứng dụng quản lý bán hàng, được phát triển bằng **Spring Boot 3**, bảo mật bằng **JWT**, sử dụng **MySQL** làm cơ sở dữ liệu, và triển khai dễ dàng với **Docker**.

---

## 🚀 Tính năng chính

- 🔐 Đăng ký / Đăng nhập người dùng với JWT
- 📦 Quản lý sản phẩm (CRUD)
- 📥 Đặt hàng sản phẩm
- 🧾 Quản lý đơn hàng
- 👤 Quản lý người dùng
- 🗂 Phân quyền người dùng (Admin/User)

---

## 🧰 Công nghệ sử dụng

| Công nghệ         | Mô tả                                              |
|------------------|---------------------------------------------------|
| Spring Boot 3    | Khung phát triển ứng dụng Java hiện đại           |
| Spring Security  | Bảo mật ứng dụng với JWT                           |
| Spring Data JPA  | Truy xuất dữ liệu qua repository pattern          |
| MySQL            | Cơ sở dữ liệu chính                               |
| Docker           | Đóng gói và triển khai ứng dụng dễ dàng           |
| Docker Compose   | Quản lý nhiều container cùng lúc                  |

---

## 📂 Cấu trúc thư mục

RetailStore/
├── src/main/java/com/example/retailstore/
│ ├── config/ # Cấu hình bảo mật, JWT
│ ├── controller/ # Controller xử lý API
│ ├── dto/ # DTOs cho request/response
│ ├── entity/ # Các thực thể (User, Product, Order,...)
│ ├── repository/ # JpaRepository interface
│ ├── service/ # Business logic
│ └── RetailStoreApplication.java
├── src/main/resources/
│ └── application.yml # Cấu hình Spring Boot
├── Dockerfile # Đóng gói ứng dụng Spring Boot
├── docker-compose.yml # Dịch vụ Spring Boot + MySQL
└── README.md # Tài liệu này


---

## ⚙️ Cài đặt và chạy ứng dụng

### 🐳 Chạy bằng Docker

> 💡 Đảm bảo đã cài Docker & Docker Compose

1. **Build image Spring Boot:**

```bash
docker build -t retailstore-backend .
Chạy toàn bộ hệ thống:

docker-compose up -d
Mặc định:

Spring Boot chạy ở: http://localhost:8080

MySQL chạy ở: localhost:3307 (port trong docker)

Xác thực JWT
Sau khi đăng nhập thành công, người dùng sẽ nhận được access token dạng JWT.

Tất cả các API yêu cầu chứng thực phải gửi kèm:

Authorization: Bearer <token>
-------------------------------------------------------------------------------------------------------------|
demo create User
curl --location --request POST 'http://localhost:8080/retailstore/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"admin",
    "password":"12345678",
    "email":"admin@gmail.com",
    "phone":"0396606503",
    "address":"Ha Noi, Viet Nam",
    "role":"ADMIN"
}'

Login User
curl --location --request POST 'http://localhost:8080/retailstore/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"user01",
    "password":"12345678"
}'

get-all-user
curl --location --request GET 'http://localhost:8080/retailstore/users/get-all-users' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJhbmhfSmFjayIsInN1YiI6ImFkbWluIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzQ5NzQwMDExLCJpYXQiOjE3NDk3MzY0MTEsImp0aSI6IjA5YTM0ZGI2LWFhYjctNGQzNy1iNjc3LTliOTljZWVlM2E5YSJ9.H5Lv6Js_zSBrs5YQmTXvhAV-W4dTKofnkeQTEqgKC5RZMnSk9dCfRFYeEB0vnxd1qSxstvDVbuwQGq0FO5lRVA'

