package com.example.project1.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="new_book")
public class NewBook {
    private String title;
    private String author;
    private String description;

    private Integer price;

    private Integer sellprice;

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSellprice() {
        return sellprice;
    }

    public void setSellprice(Integer sellprice) {
        this.sellprice = sellprice;
    }

}
