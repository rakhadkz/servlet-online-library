package interfaces;

import models.Book;
import models.Reader;

public interface ILibrarian extends IUser {
    void registerReader(Reader reader) throws Exception;
    void updateReader(Reader reader);
    void removeReader(Reader reader) throws Exception;
    void addBook(Book book) throws Exception;
    void updateBook(Book book);
    void removeBookFromList(Book book);
    void addBookToList(Reader reader, Book book) throws Exception;
}
