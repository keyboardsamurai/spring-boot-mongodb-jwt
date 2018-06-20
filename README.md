# Spring-Boot-MongoDB-JWT
[![Build Status](https://travis-ci.org/vlsidlyarevich/Spring-Boot-MongoDB-JWT.svg?branch=master)](https://travis-ci.org/vlsidlyarevich/Spring-Boot-MongoDB-JWT)
### Spring Boot base for projects with MongoDB and JWT based security.
---
This is a quick-start base for java projects with Spring Boot, MongoDB and configured JWT security.
### Running
* Download this base
* Start the MongoDB service/daemon in your system 
* Run project by `Application.class` or by `mvn clean install`, `java -jar target/*.jar`, or by `mvn spring-boot:run`

---
### JWT security
Page `http://localhost:8080/api/hello` is secured. To access this page, you need to do the following:

* **POST** request to `http://localhost:8080/api/signup` with body
```json
  username: "user",
  password: "12345"
```

Example: ```curl -d '{"username":"demo", "password":"demo"}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/signup```

* **POST** request to `http://localhost:8080/api/auth`, then take token from response and use it in header to access secured page

Example: ```curl -d '{"username":"demo", "password":"demo"}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/auth```

* **GET** request to `http://localhost:8080/api/hello` with header:

```json
  Authorization: Bearer <your token here>
```

Example: ```curl -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...your full token here' http://localhost:8080/api/hello```

Security is based on [AuthenticationTokenFilter](https://github.com/vlsidlyarevich/Spring-Boot-MongoDB-JWT/blob/master/src/main/java/com/github/vlsidlyarevich/security/filter/AuthenticationTokenFilter.java#L16-L33):

```java
@Override 
public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) 
             throws IOException, ServletException { 
  HttpServletRequest httpRequest = (HttpServletRequest) request; 
  Authentication authentication = authenticationService.authenticate(httpRequest); 
  SecurityContextHolder.getContext().setAuthentication(authentication); 
  filterChain.doFilter(request, response); 
  SecurityContextHolder.getContext().setAuthentication(null); 
} 
```
And some services for [Token creation](https://github.com/vlsidlyarevich/Spring-Boot-MongoDB-JWT/blob/master/src/main/java/com/github/vlsidlyarevich/security/service/impl/TokenServiceImpl.java) and [Token verification](https://github.com/vlsidlyarevich/Spring-Boot-MongoDB-JWT/blob/master/src/main/java/com/github/vlsidlyarevich/security/service/impl/TokenAuthenticationServiceImpl.java). 
