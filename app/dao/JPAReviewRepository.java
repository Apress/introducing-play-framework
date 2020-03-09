package dao;

import models.Review;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAReviewRepository implements ReviewRepository{

    private JPAApi jpaApi;
    private DatabaseExecutionContext executionContext;

    @Inject
    public JPAReviewRepository(JPAApi api, DatabaseExecutionContext executionContext) {
        this.jpaApi = api;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<String> saveReview(Review review) {
        return CompletableFuture.supplyAsync(
                () -> {
                    // lambda is an instance of Function<EntityManager, Long>
                    return jpaApi.withTransaction(
                            entityManager -> {
                                entityManager.persist(review);
                                return "saved";
                            });
                },
                executionContext);
    }
}