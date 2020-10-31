import config.Database;
import models.Librarian;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LibrarianAuthServlet")
public class LibrarianAuthServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        Librarian librarian = new Librarian(username, password);

        if (session.getAttribute("user") != null){
            session.invalidate();
            response.sendRedirect("lib-auth");
            return;
        }

        try {
            librarian.authenticate();
            session.setAttribute("user", librarian);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") != null)
            response.sendRedirect("test");
        else
            request.getRequestDispatcher("./auth.jsp").forward(request, response);
    }
}
