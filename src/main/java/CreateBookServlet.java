import models.Book;
import models.Librarian;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateBookServlet")
public class CreateBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookIsbn = request.getParameter("isbn");
        String bookName = request.getParameter("name");
        String bookAuthor = request.getParameter("author");
        String bookCopies = request.getParameter("copies");
        Book newBook = new Book(bookIsbn, bookName, bookAuthor, Integer.parseInt(bookCopies));
        try {
            newBook.create();
            request.setAttribute("success",
                    "The book is created successfully");
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate"))
                request.setAttribute("error", "Such ISBN already exists");
            else
                request.setAttribute("error", e.getMessage());
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
        if (user != null){
            if (user instanceof Librarian){
                request.getRequestDispatcher("./newBook.jsp").forward(request, response);
            }else{
                response.sendRedirect("books");
            }
        }
    }
}
