🛒 E-Commerce Backend (Spring Boot)

A scalable, high-performance E-Commerce backend built using Java, Spring Boot, MySQL, JWT Authentication, and RESTful APIs.
This project includes product management, user authentication, cart APIs, orders, payments, inventory tracking, and follows industry-standard backend architecture.

🚀 Features

🔐 Authentication & Authorization

User Registration & Login

JWT-based Authentication

Role-based access

Secure password hashing (BCrypt)

🛍️ Product & Category Management

Create / Update / Delete products

Category-wise filtering

Pagination & sorting

Product search API

🛒 Cart Management

Add / Remove items

Auto-update quantity

Real-time cart total calculation

📦 Order Management

Create orders from cart

Order status tracking (Placed → Shipped → Delivered)

Order history

💳 Payment Integration (Mock / Real-ready)

Razorpay/Stripe-ready structure

Mock payment validation

📦 Inventory Handling

Auto stock decrease after order

Stock-out protection

Admin stock update APIs

👨‍💻 Admin Features

Manage users

Manage products

Manage categories

View all orders

🏗️ Tech Stack
Layer	Technology
Language	Java 17
Framework	Spring Boot
Database	MySQL / PostgreSQL
Security	Spring Security + JWT
ORM	Hibernate / JPA
API Style	REST
Build Tool	Maven / Gradle
Documentation	Swagger UI


Follows MVC + Service + Repository layered architecture.

📚 API Documentation (Swagger)

Once project runs:

http://localhost:8080/swagger-ui/index.html

⚙️ Setup Instructions
1️⃣ Clone the Repository
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd YOUR_REPO

2️⃣ Configure Database

In application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3️⃣ Run the Application
mvn spring-boot:run


or

./mvnw spring-boot:run

📬 API Endpoints (Quick Overview)
🔐 Auth
| Method | Endpoint             | Description   |
| ------ | -------------------- | ------------- |
| POST   | `/api/auth/register` | Register user |
| POST   | `/api/auth/login`    | Login user    |

🛒 Cart
| Method | Endpoint                |
| ------ | ----------------------- |
| GET    | `/api/products`         |
| POST   | `/api/products` (Admin) |
| PUT    | `/api/products/{id}`    |
| DELETE | `/api/products/{id}`    |

📦 Orders
| Method | Endpoint           |
| ------ | ------------------ |
| POST   | `/api/cart/add`    |
| GET    | `/api/cart`        |
| POST   | `/api/cart/remove` |

🧪 Testing

Postman Collection included (if you want, I can generate one)

Swagger UI

Unit tests with JUnit + Mockito (optional)

📌 Future Enhancements

Payment Gateway integration (Razorpay/Stripe)

Recommendation system

Email & mobile OTP login

Admin dashboard (React / Angular)

🤝 Contributing

Pull requests are welcome. For major changes, please open an issue to discuss.

📄 License

This project is licensed under the MIT License.
