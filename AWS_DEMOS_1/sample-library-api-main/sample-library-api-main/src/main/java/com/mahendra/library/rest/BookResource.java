package com.mahendra.library.rest;

import com.mahendra.library.dao.BookDAO;
import com.mahendra.library.exceptions.BookNotFoundException;
import com.mahendra.library.models.Book;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/books")
@Api(value="/api/books", description="Operations for Books")
public class BookResource {

	@Autowired private BookDAO dao;
	
	@GetMapping(produces = "application/json")
	@ApiOperation("List all books from library")
	public ResponseEntity<List<Book>> findAll(){
		List<Book> books = new LinkedList<>();
		dao.findAll().forEach(b -> books.add(b));
		if(books.isEmpty()){
			throw new BookNotFoundException();
		}
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

	@GetMapping(produces = "application/json",value = "/{ID}")
	@ApiOperation("Find book by Book ID")
	public ResponseEntity<Book> findBook(@PathVariable("ID") Integer id){
		Optional<Book> book = dao.findById(id);
		if(book.isEmpty()){
			throw new BookNotFoundException();
		}
		return new ResponseEntity<Book>(book.get(), HttpStatus.OK);
	}

	@PostMapping(produces="application/json", consumes="application/json")
	@ApiOperation("Save new book")
	public ResponseEntity<Book> save(Book book){
		Book newBook = dao.save(book);
		return new ResponseEntity<>(newBook, HttpStatus.CREATED);
	}

	@PutMapping(produces = "application/json", consumes="application/json", value = "/{ID}")
	public ResponseEntity<Book> update(@PathVariable("ID") int bookId, Book book){
		Optional<Book> oldBook = dao.findById(bookId);
		if(oldBook.isEmpty()){
			throw new BookNotFoundException();
		}
		Book newBook = dao.save(book);
		return new ResponseEntity<>(newBook, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{ID}", produces = "application/json")
	@ApiOperation("Delete a book by id")
	public ResponseEntity<String> delete(@PathVariable("ID") int bookId){
		dao.deleteById(bookId);
		return new ResponseEntity<>("Successfully deleted a book",HttpStatus.OK);
	}
}
