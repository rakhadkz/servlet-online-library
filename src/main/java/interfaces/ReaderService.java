package interfaces;

import models.Book;
import models.Reader;

import java.util.ArrayList;

public interface ReaderService{
    //CRUD
    boolean create(Reader reader) throws Exception;
    Reader read(int readerId) throws Exception;
    boolean update(Reader reader) throws Exception;
    boolean delete(int readerId) throws Exception;

    //All readers
    ArrayList<Reader> getReaders() throws Exception;

    //Interaction with Book
    boolean addBook(int readerId, String isbn, int copies) throws Exception;
    boolean removeBook(int readerId, String isbn) throws Exception;
    boolean hasBooks(int readerId) throws Exception;
    ArrayList<Book> getBooks(int readerId) throws Exception;

    //Password changing
    boolean changePassword(int readerId, String oldPassword, String newPassword) throws Exception;
    boolean isCorrectPassword(int readerId, String password) throws Exception;
}
