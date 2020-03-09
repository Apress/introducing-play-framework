package dao;

import com.google.inject.ImplementedBy;
import models.Review;

import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAReviewRepository.class)
public interface ReviewRepository {

    CompletionStage<String> saveReview(Review review);
}
