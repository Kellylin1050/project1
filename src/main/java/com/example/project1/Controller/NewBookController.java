package com.example.project1.Controller;

import com.example.project1.Dao.NewBookRepository;
import com.example.project1.Dto.NewBookRequest;
import com.example.project1.Entity.NewBook;
import com.example.project1.Entity.User;
import com.example.project1.Service.NewBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Book管理",description = "管理Book的相關API")
@RestController
@RequestMapping("/NewBook")
public class NewBookController {
    @Autowired
    private NewBookService newBookService;

    @Operation(
            summary = "查詢新書",
            description = "根據書名查詢新書訊息。",
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
    public ResponseEntity<String> findNewBook(@RequestParam String title){
        try {
            NewBook newBook = newBookService.getNewBookByTitle(title);
            if (newBook != null) {
                return ResponseEntity.ok(title);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }   /*newBookService.getNewBookByTitle(title);
        if (title != null) {
            return ResponseEntity.status(HttpStatus.OK).body(title);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            List<NewBook> newBooks = newBookService.getNewBookByTitle(title);
            if (newBooks != null && !newBooks.isEmpty()) {
                return ResponseEntity.ok(title);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }*/
    }

    @Operation(
            summary = "更新新書訊息",
            description = "根據提供的訊息更新新書。"
            //requestBody =@RequestBody(description = "新書更新訊息", required = true, content = @Content(schema = @Schema(implementation = NewBookRequest.class)))
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/doUpdateNewBook")
    public ResponseEntity<String> doUpdateNewBook(@RequestBody NewBookRequest newBookRequest){
        //newBookService.updateNewBook(id,newBookRequest);
        //return ResponseEntity.status(HttpStatus.OK).body();
        int result = newBookService.updateNewBook(newBookRequest);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("NewBook updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NewBook update failed");
        }
    }

    /*@GetMapping("/doFindById/{id}")
    public String doFindById(@PathVariable Integer id, Model model){
        NewBook newBook = newBookService.getNewBookById(id);
        //model.addAttribute("n", newBook);
        return newBook ;
    }*/

    @Operation(
            summary = "新增新書",
            description = "新增一本新的書籍。",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "新書訊息",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NewBook.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "新書創建成功"),
            @ApiResponse(responseCode = "500", description = "伺服器錯誤")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/doSaveNewBook")
    public ResponseEntity<?> doSaveNewBook (@RequestBody @Valid NewBook entity) {
        NewBook newBook = newBookService.saveNewBook(entity);
        if (newBook == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("NewBook save successfully");
    }

    @Operation(
            summary = "刪除新書",
            description = "根據書籍 ID 刪除指定的新書。",
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
    public ResponseEntity<String> doDeleteById (@PathVariable Integer id) {
        newBookService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("delete");
    }

   /* @RequestMapping("/new")
    public String doNewBookAddUI(){
        return "newBook_adds";
    }
   @RequestMapping("/")
    public String doNewBookUI(Model model){
        List<NewBook> newBookList=newBookService.findNewBook();
        model.addAttribute("NewBookList", newBookList);
        return "index";
    }*/
   @Operation(
           summary = "403錯誤頁面",
           description = "返回403錯誤頁面"
   )
    @RequestMapping("/403")
    public String error403(){
        return "403";
    }

}