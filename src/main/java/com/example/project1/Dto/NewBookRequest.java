package com.example.project1.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "新書上傳要求")
public class NewBookRequest {
    @Schema(description = "書名", example = "Java Programming")
    private String title;
    @Schema(description = "作者", example = "John Doe")
    private String author;
    @Schema(description = "書籍描述", example = "一本關於java的指南")
    private String description;
    @Schema(description = "價格", example = "500")
    private Integer price;
    @Schema(description = "銷售價格", example = "520")
    private Integer sellprice;
    @Schema(description = "書籍id", example = "1")
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
    public Integer getId(){return id;
    }
    public void setId(Integer id){this.id = id;}
}
