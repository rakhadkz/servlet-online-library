package interfaces;

import models.Book;

import java.util.ArrayList;

public interface IReader extends IUser{
    void createReader() throws Exception;
    ArrayList<Book> getBooks() throws Exception;
    void update() throws Exception;
    void delete() throws Exception;
    void changePassword(String newPassword) throws Exception;
    void deleteBook(String isbn) throws Exception;

}
