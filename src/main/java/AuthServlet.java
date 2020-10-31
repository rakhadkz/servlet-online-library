import models.Librarian;
import models.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AuthServlet")
public class AuthServlet extends HttpServlet {
    HttpSession session;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = request.getParameter("role");
        session = request.getSession();

        if (session.getAttribute("user") != null){
            session.invalidate();
            for (Cookie cookie: request.getCookies()){
                cookie.setValue("");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            response.sendRedirect("books");
            return;
        }

        if (role.equals("librarian")){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            Librarian librarian = new Librarian(username, password);

            try {
                librarian.authenticate();
                session.setAttribute("user", librarian);
                Cookie cookie = new Cookie("fullName", librarian.getUsername());
                cookie.setMaxAge(25);
                response.addCookie(cookie);
            } catch (Exception e) {
                request.setAttribute("lib_error", e.getMessage());
            }
        }else{
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Reader reader = new Reader(email, password);
            try {
                reader.authenticate();
                session.setAttribute("user", reader);
                Cookie cookie = new Cookie("fullName", reader.getFirstName() + " " + reader.getLastName());
                cookie.setMaxAge(25);
                response.addCookie(cookie);
            } catch (Exception e) {
                request.setAttribute("reader_error", e.getMessage());
            }
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") != null){
            response.sendRedirect("books");
            return;
        }
        request.getRequestDispatcher("./auth.jsp").forward(request, response);
    }
}
