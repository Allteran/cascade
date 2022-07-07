package io.allteran.cascade.managerservice.service;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.dto.CredentialsDTO;
import io.allteran.cascade.managerservice.dto.EmployeeDTO;
import io.allteran.cascade.managerservice.exception.NotFoundException;
import io.allteran.cascade.managerservice.exception.UserFieldException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.CharBuffer;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, EmployeeService employeeService) {
        this.passwordEncoder = passwordEncoder;
        this.employeeService = employeeService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Employee signIn(CredentialsDTO credentials) {
        var user = employeeService.findByPhone(credentials.getLogin());
        if(user == null || user.getId().isEmpty()) {
            throw new NotFoundException("User with phone [" + credentials.getLogin() + "] not found");
        }

        if(passwordEncoder.matches(CharBuffer.wrap(credentials.getPassword()), user.getPassword())) {
            user.setToken(createToken(user));
            return user;
        }
        throw new UserFieldException("Invalid password");
    }

    public Employee validateToken(String token) {
        String login = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Employee user = employeeService.findByPhone(login);
        if(user == null || user.getId().isEmpty()) {
            throw new NotFoundException("User with phone [" + login + "] not found");
        }
        return user;
    }

    private String createToken(Employee user) {
        Claims claims = Jwts.claims().setSubject(user.getPhone());
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


}
