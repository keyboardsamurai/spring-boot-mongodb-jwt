package com.github.vlsidlyarevich.controller;

import com.github.vlsidlyarevich.converter.ConverterFacade;
import com.github.vlsidlyarevich.dto.UserDTO;
import com.github.vlsidlyarevich.model.User;
import com.github.vlsidlyarevich.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/signup")
public class SignUpController {

    private final UserService service;

    private final ConverterFacade converterFacade;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignUpController(final UserService service,
                            final ConverterFacade converterFacade, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.converterFacade = converterFacade;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody final UserDTO dto) {
        final User user = converterFacade.convert(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(service.create(user), HttpStatus.OK);
    }
}
