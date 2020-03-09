package models;

import io.ebean.Model;

import javax.persistence.*;

import io.ebean.*;

@Entity
@Table(name="book")
public class Book extends Model {

   @Id
   private String id;
   private String author;
   private String title;
   private String picture;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
class Test {
    void test() {
     Book book = new Book();
     book.save();
     Book b = Ebean.find(Book.class).where().idEq("").findOne();
        Ebean.find(Book.class).where().like("title","java%").orderBy("tile desc").findList();

    }
}