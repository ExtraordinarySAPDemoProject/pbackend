package org.latheild.user.dao;

import org.latheild.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findById(String id);

    User findByEmail(String email);

    ArrayList<User> findAll();

    int countById(String id);

    int countByEmail(String email);

    void deleteById(String id);

    void deleteByEmail(String email);

}
