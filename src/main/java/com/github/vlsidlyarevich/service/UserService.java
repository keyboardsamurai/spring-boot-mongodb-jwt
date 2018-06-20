package com.github.vlsidlyarevich.service;

import com.github.vlsidlyarevich.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

    User create(User object);

    Optional<User> find(String id);

    User findByUsername(String userName);

    List<User> findAll();

    User update(String id, User object);

    String delete(String id);
}
