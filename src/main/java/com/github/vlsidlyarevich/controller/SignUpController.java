package com.github.vlsidlyarevich.controller;

import com.github.vlsidlyarevich.converter.ConverterFacade;
import com.github.vlsidlyarevich.dto.UserDTO;
import com.github.vlsidlyarevich.model.User;
import com.github.vlsidlyarevich.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/signup")
public class SignUpController {

    private final UserService service;

    private final ConverterFacade converterFacade;

    private final PasswordEncoder passwordEncoder;


    private final Validator validator;

    @Autowired
    public SignUpController(final UserService service,
                            final ConverterFacade converterFacade, PasswordEncoder passwordEncoder, Validator validator) {
        this.service = service;
        this.converterFacade = converterFacade;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Validated
    public ResponseEntity<?> signUp(@RequestBody final UserDTO dto,Errors errors) {
        ValidationUtils.invokeValidator(validator,dto,errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        final User user = converterFacade.convert(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(service.create(user), HttpStatus.OK);
    }
}
