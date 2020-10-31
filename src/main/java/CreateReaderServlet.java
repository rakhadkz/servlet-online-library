import models.Librarian;
import models.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateReaderServlet")
public class CreateReaderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        Reader reader = new Reader(firstName, lastName, email, password);
        try {
            reader.createReader();
            request.setAttribute("success", "The reader is created successfully");
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate"))
                request.setAttribute("error", "Such ID already exists");
            else
                request.setAttribute("error", e.getMessage());
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
        if (user != null){
            if (user instanceof Librarian){
                request.getRequestDispatcher("./newReader.jsp").forward(request, response);
            }else{
                response.sendRedirect("books");
            }
        }
    }
}
