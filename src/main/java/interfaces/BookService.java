package interfaces;

import models.Book;
import models.Reader;

import java.util.ArrayList;

public interface BookService {
    //CRUD
    boolean create(Book book) throws Exception;
    Book read(String isbn) throws Exception;
    boolean update(Book book) throws Exception;
    boolean delete(String isbn) throws Exception;

    //All books
    ArrayList<Book> getBooks() throws Exception;

    //Interaction with reader
    boolean isBorrowable(String isbn, int copies, int readerId) throws Exception;
    ArrayList<Reader> getReaders(String isbn) throws Exception;
}
