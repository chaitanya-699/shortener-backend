# URL Shortener Backend

A Spring Boot-based backend for a URL shortener service, supporting user registration, login, analytics, and guest access.

## Features

- User registration and authentication (JWT-based)
- OAuth login support
- URL shortening for registered and guest users
- Analytics: clicks by date, device, country, browser, etc.
- Password reset via OTP
- RESTful API endpoints
- GeoIP-based location tracking

## Tech Stack

- Java 17+
- Spring Boot
- Spring Security (JWT)
- JPA/Hibernate
- H2/PostgreSQL/MySQL (configurable)
- MaxMind GeoLite2 for IP geolocation

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- (Optional) PostgreSQL/MySQL database

### Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/chaitanya-699/shortener-backend.git
   cd shortener-backend
   ```

2. **Configure database:**
   - Edit `src/main/resources/application.properties` for your DB settings.
   - Default is H2 in-memory for quick start.

3. **GeoIP Database:**
   - The MaxMind GeoLite2 database is included at `src/main/resources/geoIp/GeoLite2-Country.mmdb`.

4. **Build and run:**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

5. **API runs at:**
   `http://localhost:8080/`

## API Endpoints

### Authentication

- `POST /register` — Register a new user
- `POST /login` — Login and receive JWT
- `POST /logout` — Logout user
- `POST /oauth` — OAuth login

### URL Shortening

- `POST /url/shorten` — Shorten a URL (user)
- `POST /url/guest/shorten` — Shorten a URL (guest)
- `GET /url/{code}` — Redirect to original URL

### Analytics

- `GET /analytics/{userId}` — Get analytics for user
- `GET /analytics/guest/{guestId}` — Get analytics for guest

### Other

- `POST /forgot-password` — Request password reset OTP
- `POST /verify-otp` — Verify OTP for password reset

## Folder Structure

```
src/
  main/
    java/com/url/backend/
      controller/    # REST controllers
      service/       # Business logic
      repo/          # JPA repositories
      entity/        # JPA entities
      DTO/           # Data Transfer Objects
      util/          # Utility classes
      config/        # Security configuration
    resources/
      geoIp/         # GeoLite2 database
  test/
    java/com/url/backend/ # Unit tests
pom.xml              # Maven build file
```

## Environment Variables

Configure in `application.properties`:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `jwt.secret`
- `geoip.database.path`

## Running Tests

```sh
mvn test
```

## License

This project uses the MIT License.

## Contact

For questions or support, contact [chaitanya-699](https://github.com/chaitanya-699).
