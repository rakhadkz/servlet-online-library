package models;

import config.Database;
import interfaces.ILibrarian;
import interfaces.IUser;

import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Librarian implements ILibrarian {
    private String username;
    private String password;
    public String check;

    public Librarian(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void authenticate() throws Exception {
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("select * from librarian where lib_username = ? and lib_password = ?");
        preparedStatement.setString(1, this.username);
        preparedStatement.setString(2, this.password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.isBeforeFirst())
            throw new Exception("Incorrect username or password");
    }

    @Override
    public boolean logOut() {
        return false;
    }

    @Override
    public void registerReader(Reader reader) throws Exception {
        String query = "insert into reader (firstName, lastName, email, password) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, reader.getFirstName());
        preparedStatement.setString(2, reader.getLastName());
        preparedStatement.setString(3, reader.getEmail());
        preparedStatement.setString(4, reader.getPassword());
        preparedStatement.executeQuery();
    }

    @Override
    public void updateReader(Reader reader) {

    }

    @Override
    public void removeReader(Reader reader) throws Exception {

    }

    @Override
    public void addBook(Book book) throws Exception {

    }

    @Override
    public void updateBook(Book book) {

    }

    @Override
    public void removeBookFromList(Book book) {

    }

    @Override
    public void addBookToList(Reader reader, Book book) throws Exception {

    }
}
