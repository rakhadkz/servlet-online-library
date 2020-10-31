package interfaces;

import java.sql.SQLException;

public interface IUser {
    void authenticate() throws Exception;
    boolean logOut();
}
