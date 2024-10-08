package com.example.project1.Controller;

import com.example.project1.Dao.NewBookRepository;
import com.example.project1.Dto.NewBookRequest;
import com.example.project1.Dto.NewBookResponse;
import com.example.project1.Dto.UserResponse;
import com.example.project1.Entity.NewBook;
import com.example.project1.Entity.Role;
import com.example.project1.Entity.User;
import com.example.project1.Service.NewBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Book管理",description = "管理Book的相關API")
@RestController
@RequestMapping("/NewBook")
public class NewBookController {
    @Autowired
    private NewBookService newBookService;

    @Operation(
            summary = "查詢書籍",
            description = "根據書名查詢書籍訊息。",
            parameters = {
                    @Parameter(name = "title", description = "書名", required = true, schema = @Schema(type = "string"))
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功返回書名"),
            @ApiResponse(responseCode = "404", description = "書籍未找到"),
            @ApiResponse(responseCode = "500", description = "伺服器錯誤")
    })
    @GetMapping("/book")
    public ResponseEntity<String> findNewBook(
            @Parameter(description = "查詢書名", required = true, allowEmptyValue = false)
            @RequestParam String title){
        try {
            NewBook newBook = newBookService.getNewBookByTitle(title);
            if (newBook != null) {
                return ResponseEntity.ok( "Successfully found the book : "+ title);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @Operation(
            summary = "查詢所有書籍",
            description = "返回所有書籍的詳細訊息。",
            responses = {
                    @ApiResponse(responseCode = "200", description = "成功返回所有書籍"),
                    @ApiResponse(responseCode = "404", description = "未找到書籍")
            }
    )
    @GetMapping("/doFindAllBooks")
    public ResponseEntity<Object> doFindAllBooks(Model model) {
        List<NewBook> book = newBookService.findAllBook();
        if (!book.isEmpty()) {
            List<NewBookResponse> newBookResponses = book.stream()
                    .map(book1 -> new NewBookResponse(
                            book1.getTitle(),
                            book1.getAuthor(),
                            book1.getDescription(),
                            book1.getPrice(),
                            book1.getSellprice()
                    ))
                    .collect(Collectors.toList());

            model.addAttribute("u", newBookResponses);
            return ResponseEntity.status(HttpStatus.OK).body(newBookResponses);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到書籍");
        }
    }



    @Operation(
            summary = "更新書的訊息",
            description = "根據提供的訊息更新書。"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功更新書籍"),
            @ApiResponse(responseCode = "400", description = "書籍更新失敗")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/doUpdateNewBook")
    public ResponseEntity<String> doUpdateNewBook(@RequestBody NewBookRequest newBookRequest){
        int result = newBookService.updateNewBook(newBookRequest);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book update failed");
        }
    }

    @Operation(
            summary = "新增書籍",
            description = "新增一本書籍。",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "書籍訊息",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NewBook.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "書籍創建成功"),
            @ApiResponse(responseCode = "500", description = "伺服器錯誤")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/doSaveNewBook")
    public ResponseEntity<?> doSaveNewBook (@RequestBody @Valid NewBook entity) {
        NewBook newBook = newBookService.saveNewBook(entity);
        if (newBook == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        String successMessage = " 書籍 " + newBook.getTitle() + " 創建成功 ";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", successMessage);
        responseBody.put("Book",newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body("Book save successfully" );
    }

    @Operation(
            summary = "刪除書籍",
            description = "根據書籍 ID 刪除指定的書籍。",
            parameters = {
                    @Parameter(name = "id", description = "書籍 ID", required = true, schema = @Schema(type = "integer"))
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "刪除成功"),
            @ApiResponse(responseCode = "404", description = "書籍未找到")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<String> doDeleteById (
            @Parameter(description = "要刪除書本的id")
            @PathVariable Integer id) {
        if (newBookService.existsById(id)) {
            newBookService.deleteById(id);
            return ResponseEntity.ok("Book with ID " + id + " deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }

   @Operation(
           summary = "403錯誤頁面",
           description = "返回403錯誤頁面"
   )
    @RequestMapping("/403")
    public String error403(){
        return "403";
    }

}