package models;

import config.Database;
import interfaces.IReader;
import interfaces.IUser;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Stack;

public class Reader implements IReader, Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Reader(int id) {
        this.id = id;
    }

    public Reader(int id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Reader(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Reader(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Reader(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public Reader(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Reader(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Reader findById(int id) throws SQLException {
        Database.getConnection().close();
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from Reader where id = " + id);
        Reader reader = null;
        while (resultSet.next()){
            reader = new Reader(
                    resultSet.getInt("id"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
        }
        resultSet.close();
        statement.close();
        Database.getConnection().close();
        return reader;
    }

    @Override
    public void authenticate() throws Exception {
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("select * from reader where email = ? and password = ?");
        preparedStatement.setString(1, this.email);
        preparedStatement.setString(2, this.password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.isBeforeFirst())
            throw new Exception("Incorrect email or password");
        resultSet.next();
        this.firstName = resultSet.getString("firstName");
        this.lastName = resultSet.getString("lastName");
        this.id = resultSet.getInt("id");
    }

    @Override
    public boolean logOut() {
        return false;
    }

    @Override
    public void createReader() throws Exception {
        String query = "insert into reader (email, password, firstName, lastName) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, this.email);
        preparedStatement.setString(2, this.password);
        preparedStatement.setString(3, this.firstName);
        preparedStatement.setString(4, this.lastName);
        preparedStatement.executeUpdate();
        Database.getConnection().close();
        preparedStatement.close();
    }

    public static Stack<Reader> getAllReaders(){
        Stack<Reader> list = new Stack<>();
        try {
            Statement statement = Database.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Reader");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                list.add(new Reader(id, firstName, lastName, email, password));
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
    public ArrayList<Book> getBooks() throws Exception {
        ArrayList<Book> list = new ArrayList<>();
        String query = "select * from BorrowedBooks bb inner join Book b on " +
                "bb.borrowedBookIsbn = b.isbn where bb.reader_id = " + this.id;
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            String isbn = resultSet.getString("isbn");
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            list.add(new Book(isbn, name, author));
        }
        resultSet.close();
        statement.close();
        Database.getConnection().close();
        return list;
    }

    @Override
    public void update() throws Exception {
        String query = "update reader set firstName = ?, lastName = ?, email = ? where id = ?";
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, this.firstName);
        preparedStatement.setString(2, this.lastName);
        preparedStatement.setString(3, this.email);
        preparedStatement.setInt(4, this.id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    @Override
    public void delete() throws Exception {
        if (!hasBooks()){
            String query = "DELETE FROM reader WHERE id = ?";
            PreparedStatement st = Database.getConnection().prepareStatement(query);
            st.setInt(1, this.id);
            st.executeUpdate();
            st.close();
            Database.getConnection().close();
        }else{
            throw new Exception("The reader has borrowed books");
        }
    }

    private boolean hasBooks() throws SQLException {
        String query = "select * from BorrowedBooks where reader_id = " + this.id;
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet.isBeforeFirst();
    }

    @Override
    public void changePassword(String newPassword) throws Exception {
        if (isPassword()){
            String query = "update reader set password = ? where id = ?";
            PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }else{
            throw new Exception("Incorrect current password");
        }
    }

    @Override
    public void deleteBook(String isbn) throws Exception {

    }

    private boolean isPassword() throws SQLException {
        String query = "select * from reader where id = " + this.id + " and password = '" + this.password + "'";
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet.isBeforeFirst();
    }
}
