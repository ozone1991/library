package com.hertz.library.controller;

import com.hertz.library.dto.AuthorDTO;
import com.hertz.library.exceptions.AuthorDoesNotExistException;
import com.hertz.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        try {
            List<AuthorDTO> authors = authorService.findAll();
            return new ResponseEntity<>(authors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        try {
            Optional<AuthorDTO> author = authorService.findById(id);
            return author.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createNewAuthor(@RequestBody AuthorDTO authorDTO) {
        try {
            AuthorDTO newAuthor = authorService.save(authorDTO);
            return new ResponseEntity<>(newAuthor, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        Optional<AuthorDTO> author = authorService.findById(id);
        if (author.isPresent()) {
            authorService.deleteById(id);
            return new ResponseEntity<>("Author succesfully deleted: " + id, HttpStatus.OK);
        } else {
            throw new AuthorDoesNotExistException();
        }
    }

    @ExceptionHandler(value = AuthorDoesNotExistException.class)
    public ResponseEntity<String> handleAuthorDoesNotExistException() {
        return new ResponseEntity<>("Author does not exist", HttpStatus.NOT_FOUND);
    }
}
