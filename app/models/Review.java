package models;

import dao.ReviewRepository;

import javax.persistence.*;


public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String userId;
    public String bookId;

    public void save(ReviewRepository reviewRepo) {
        System.out.println("review repo "+reviewRepo);
        reviewRepo.saveReview(this);
    }

}
