package config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    public static Connection connection;

    public static Connection getConnection(){
        Context initialContext = null;
        Connection connection = null;
        try
        {
            initialContext = new InitialContext();
            Context envCtx = (Context)initialContext.lookup("java:comp/env");
            DataSource ds = (DataSource)envCtx.lookup("jdbc/Assignment8");
            connection = ds.getConnection();
        }
        catch (NamingException | SQLException e)
        {
            e.printStackTrace();
        }
        return connection;
    }

    public String auth(String username, String password){
        String user = "";
        try{
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Librarian");
            ResultSetMetaData metaData = resultSet.getMetaData();
            boolean isUser = metaData.getColumnCount() == 1;
            if (isUser){
                user = resultSet.getString("lib_username");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

}
