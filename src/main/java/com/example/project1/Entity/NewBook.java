package com.example.project1.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Schema(description = "書籍實體")
@Entity
@Table(name="book")
public class NewBook {
    @Schema(description = "書名", example = "Slow Dance")
    private String title;
    @Schema(description = "作者", example = "Rainbow Rowell")
    private String author;
    @Schema(description = "書籍描述", example = "Shiloh questions if Kerry still wants to reconnect after all the time and changes.")
    private String description;
    @Schema(description = "價格", example = "600")
    private Integer price;
    @Schema(description = "銷售價格", example = "550")
    private Integer sellprice;
    @Schema(description = "書籍id", example = "1")
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Getters and Setters

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

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

}
