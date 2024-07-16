package com.example.project1.Controller;

import com.example.project1.Entity.NewBook;
import com.example.project1.Service.NewBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/NewBook")
public class NewBookController {
    @Autowired
    private NewBookService newBookService;

    @GetMapping("/book")
    public ResponseEntity<String> findNewBook(String newBook){
        newBookService.findNewBook(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PostMapping("/doUpdateNewBook")
    public ResponseEntity<NewBook> doUpdateNewBook(NewBook entity){
        newBookService.updateNewBook(entity);
        return ResponseEntity.status(HttpStatus.OK).body(entity);
    }

    @RequestMapping("/edit/{id}")
    public String doFindById(@PathVariable Integer id, Model model){
        Optional<NewBook> newBook = newBookService.findById(id);
        model.addAttribute("n", newBook);
        return "newBook_update";
    }

    @RequestMapping("/doSaveNewBook")
    public ResponseEntity<NewBook> doSaveNewBook (NewBook entity) {
        newBookService.saveNewBook(entity);
        return ResponseEntity.status(HttpStatus.OK).body(entity);
    }

    @RequestMapping("/delete/{id}")
    public String doDeleteById (@PathVariable Integer id) {
        newBookService.deleteById(id);
        return "delete";
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

    @RequestMapping("/403")
    public String error403(){
        return "403";
    }

}