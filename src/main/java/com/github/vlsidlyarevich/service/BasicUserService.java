package com.github.vlsidlyarevich.service;

import com.github.vlsidlyarevich.model.User;
import com.github.vlsidlyarevich.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class BasicUserService implements UserService {

    private final UserRepository repository;

    @Autowired
    public BasicUserService(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(final User user) {
        user.setCreatedAt(String.valueOf(LocalDateTime.now()));
        return repository.save(user);
    }

    @Override
    public Optional<User> find(final String id) {
        return repository.findById(id);
    }

    @Override
    public User findByUsername(final String userName) {
        return repository.findByUsername(userName);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User update(final String id, final User user) {
        user.setId(id);

        final Optional<User> dbUserOpt = repository.findById(id);

        if (dbUserOpt.isPresent()) {
            final User dbUser = dbUserOpt.get();
            user.setCreatedAt(dbUser.getCreatedAt());
            user.setUpdatedAt(String.valueOf(LocalDateTime.now()));
        } else {
            user.setCreatedAt(String.valueOf(LocalDateTime.now()));
        }
        repository.save(user);
        return user;
    }

    @Override
    public String delete(final String id) {
        repository.deleteById(id);
        return id;
    }
}
