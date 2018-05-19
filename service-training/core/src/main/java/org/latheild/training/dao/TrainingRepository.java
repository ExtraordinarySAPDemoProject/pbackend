package org.latheild.training.dao;

import org.latheild.training.domain.Training;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface TrainingRepository extends MongoRepository<Training, String> {

    Optional<Training> findById(String id);

    ArrayList<Training> findAllByAuthor(String author);

    ArrayList<Training> findAll();

    int countById(String id);

    int countByAuthor(String author);

    void deleteById(String id);

    void deleteAllByAuthor(String author);

}
