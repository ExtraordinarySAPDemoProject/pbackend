package org.latheild.comment.dao;

import org.latheild.comment.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {

    @Override
    Optional<Comment> findById(String id);

    ArrayList<Comment> findAllByUserId(String userId);

    ArrayList<Comment> findAllByTrainingId(String trainingId);

    ArrayList<Comment> findAllByUserIdAndTrainingId(String userId, String trainingId);

    ArrayList<Comment> findAll();

    int countById(String id);

    int countByUserId(String userId);

    int countByTrainingId(String trainingId);

    void deleteById(String id);

    void deleteAllByUserId(String userId);

    void deleteAllByTrainingId(String trainingId);

    void deleteAllByUserIdAndTrainingId(String userId, String trainingId);
}
