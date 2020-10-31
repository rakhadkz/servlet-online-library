import config.Database;
import interfaces.ReaderService;
import models.Book;
import models.Reader;

import java.sql.*;
import java.util.ArrayList;

public class ReaderServiceImpl implements ReaderService {
    @Override
    public boolean create(Reader reader) throws Exception {
        String query = "insert into reader (email, password, firstName, lastName) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, reader.getEmail());
        preparedStatement.setString(2, reader.getPassword());
        preparedStatement.setString(3, reader.getFirstName());
        preparedStatement.setString(4, reader.getLastName());
        preparedStatement.executeUpdate();
        Database.getConnection().close();
        preparedStatement.close();
        return true;
    }

    @Override
    public Reader read(int readerId) throws Exception {
        Database.getConnection().close();
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from Reader where id = " + readerId);
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
    public boolean update(Reader reader) throws Exception {
        String query = "update reader set firstName = ?, lastName = ?, email = ? where id = ?";
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, reader.getFirstName());
        preparedStatement.setString(2, reader.getLastName());
        preparedStatement.setString(3, reader.getEmail());
        preparedStatement.setInt(4, reader.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    @Override
    public boolean delete(int readerId) throws Exception {
        if (!hasBooks(readerId)){
            String query = "DELETE FROM reader WHERE id = ?";
            PreparedStatement st = Database.getConnection().prepareStatement(query);
            st.setInt(1, readerId);
            st.executeUpdate();
            st.close();
            Database.getConnection().close();
            return true;
        }
        throw new Exception("The reader has borrowed books");
    }

    @Override
    public ArrayList<Reader> getReaders() throws Exception {
        ArrayList<Reader> list = new ArrayList<>();
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
        return list;
    }

    @Override
    public boolean addBook(int readerId, String isbn, int copies) throws Exception {
        BookServiceImpl bookService = new BookServiceImpl();
        if (bookService.isBorrowable(isbn, readerId, copies)){
            String query = "insert into BorrowedBooks (borrowedBookIsbn, reader_id) values (?, ?)";
            PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, isbn);
            preparedStatement.setInt(2, readerId);
            preparedStatement.executeUpdate();
            Database.getConnection().close();
            preparedStatement.close();
            return true;
        }
        throw new Exception("All copies are borrowed");
    }

    @Override
    public boolean removeBook(int readerId, String isbn) throws Exception {
        String query = "DELETE FROM BorrowedBooks WHERE borrowedBookIsbn = ? and reader_id = ?";
        PreparedStatement st = Database.getConnection().prepareStatement(query);
        st.setString(1, isbn);
        st.setInt(2, readerId);
        st.executeUpdate();
        st.close();
        Database.getConnection().close();
        return true;
    }

    @Override
    public boolean hasBooks(int readerId) throws Exception {
        String query = "select * from BorrowedBooks where reader_id = " + readerId;
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet.isBeforeFirst();
    }

    @Override
    public ArrayList<Book> getBooks(int readerId) throws Exception {
        ArrayList<Book> list = new ArrayList<>();
        String query = "select * from BorrowedBooks bb inner join Book b on " +
                "bb.borrowedBookIsbn = b.isbn where bb.reader_id = " + readerId;
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
    public boolean changePassword(int readerId, String oldPassword, String newPassword) throws Exception {
        if (isCorrectPassword(readerId, oldPassword)){
            String query = "update reader set password = ? where id = ?";
            PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, readerId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        }
        throw new Exception("Incorrect current password");
    }

    @Override
    public boolean isCorrectPassword(int readerId, String password) throws Exception {
        String query = "select * from reader where id = " + readerId + " and password = '" + password + "'";
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet.isBeforeFirst();
    }
}
