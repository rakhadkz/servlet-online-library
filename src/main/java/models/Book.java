package models;

import config.Database;
import interfaces.IBook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;

public class Book implements IBook {
    private String isbn;
    private String name;
    private String author;
    private int copies;

    public Book(String isbn, String name, String author, int copies) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.copies = copies;
    }

    public Book(String isbn, int copies) {
        this.isbn = isbn;
        this.copies = copies;
    }

    public Book(String isbn, String name, String author) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
    }

    public Book(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public static Book findByIsbn(String isbn) throws Exception {
        Database.getConnection().close();
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from Book where isbn = '" + isbn + "'");
        Book book = null;
        if (!resultSet.isBeforeFirst()){
            throw new Exception("No book with such ISBN");
        }
        while (resultSet.next()){
            book = new Book(
                    resultSet.getString("isbn"),
                    resultSet.getString("name"),
                    resultSet.getString("author"),
                    resultSet.getInt("copies")
            );
        }
        resultSet.close();
        statement.close();
        Database.getConnection().close();
        return book;
    }

    static public ArrayList<Book> getAllBooks() {
        ArrayList<Book> list = new ArrayList<>();
        try {
            Statement statement = Database.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Book");
            while (resultSet.next()){
                String isbn = resultSet.getString("isbn");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                int copies = resultSet.getInt("copies");
                list.add(new Book(isbn, name, author, copies));
            }
            resultSet.close();
            statement.close();
            Database.getConnection().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    @Override
    public void deleteReader(int id) throws Exception {
        String query = "DELETE FROM BorrowedBooks WHERE borrowedBookIsbn = ? and reader_id = ?";
        PreparedStatement st = Database.getConnection().prepareStatement(query);
        st.setString(1, this.isbn);
        st.setInt(2, id);
        st.executeUpdate();
        st.close();
        Database.getConnection().close();
    }

    @Override
    public ArrayList<Reader> getReaders() throws Exception {
        ArrayList<Reader> list = new ArrayList<>();
        String query = "select * from BorrowedBooks b inner join Reader r on " +
                "b.reader_id = r.id where b.borrowedBookIsbn = '" + this.isbn + "'";
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            list.add(new Reader(id, firstName, lastName));
        }
        resultSet.close();
        statement.close();
        Database.getConnection().close();
        return list;
    }

    @Override
    public void delete() throws SQLException {
        String query = "DELETE FROM Book WHERE isbn = ?";
        PreparedStatement st = Database.getConnection().prepareStatement(query);
        st.setString(1, this.isbn);
        st.executeUpdate();
        st.close();
        Database.getConnection().close();
    }

    @Override
    public void update() throws SQLException {
        String query = "update book set name = ?, author = ? where isbn = ?";
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, this.name);
        preparedStatement.setString(2, this.author);
        preparedStatement.setString(3, this.isbn);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void addReader(int readerID) throws Exception {
        if (isReader(readerID)){
            this.borrow(readerID);
        }else{
            throw new Exception("No reader with such id");
        }
    }

    @Override
    public void create() throws Exception {
        String query = "insert into book (isbn, name, author, copies) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, this.isbn);
        preparedStatement.setString(2, this.name);
        preparedStatement.setString(3, this.author);
        preparedStatement.setInt(4, this.copies);
        preparedStatement.executeUpdate();
        Database.getConnection().close();
        preparedStatement.close();
    }

    @Override
    public void borrow(int readerID) throws Exception {
        if (isBorrowable(readerID)){
            String query = "insert into BorrowedBooks (borrowedBookIsbn, reader_id) values (?, ?)";
            PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.isbn);
            preparedStatement.setInt(2, readerID);
            preparedStatement.executeUpdate();
            Database.getConnection().close();
            preparedStatement.close();
            return;
        }
        throw new Exception("All copies are borrowed");
    }

    @Override
    public boolean isBorrowable(int readerID) throws Exception {
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from BorrowedBooks where borrowedBookIsbn = '" + this.isbn + "'");
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        if (this.copies > resultSetMetaData.getColumnCount()){
            while (resultSet.next()){
                if (readerID == resultSet.getInt("reader_id")){
                    throw new Exception("You have already borrowed this book");
                }
            }
            return true;
        }
        statement.close();
        resultSet.close();
        Database.getConnection().close();
        return false;
    }

    private boolean isReader(int id) throws Exception {
        boolean isReader;
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from reader where id = " + id);
        isReader = resultSet.isBeforeFirst();
        resultSet.close();
        statement.close();
        Database.getConnection().close();
        return isReader;
    }


}
