import com.google.gson.JsonObject;
import models.Book;
import models.Librarian;
import models.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ReaderServlet")
public class ReaderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String submit = request.getParameter("submit");
        switch (submit){
            case "update":
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                JsonObject json = new JsonObject();
                int id = Integer.parseInt(request.getParameter("id"));
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String email = request.getParameter("email");
                Reader reader = new Reader(id, firstName, lastName, email);
                try {
                    reader.update();
                    json.addProperty("status", "success");
                    json.addProperty("message", "Data updated successfully");
                    out.print(json.toString());
                } catch (Exception e) {
                    json.addProperty("status", "error");
                    json.addProperty("message", e.getMessage());
                    out.print(json.toString());
                }
                break;
            case "changePassword":
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                out = response.getWriter();
                json = new JsonObject();
                String newPassword = request.getParameter("newPassword");
                reader = new Reader(Integer.parseInt(request.getParameter("id")), request.getParameter("password"));
                try {
                    reader.changePassword(newPassword);
                    System.out.println("Hello");
                    json.addProperty("status", "success");
                    json.addProperty("message", "Password changed successfully");
                    out.print(json.toString());
                } catch (Exception e) {
                    json.addProperty("status", "error");
                    json.addProperty("message", e.getMessage());
                    out.print(json.toString());
                }
                break;
            case "addBook":
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                int readerID = Integer.parseInt(request.getParameter("id"));
                out = response.getWriter();
                json = new JsonObject();
                try {
                    Book book = Book.findByIsbn(request.getParameter("isbn"));
                    book.addReader(readerID);
                    json.addProperty("status", "success");
                    json.addProperty("message", "The book added successfully");
                    out.print(json.toString());
                } catch (Exception e) {
                    json.addProperty("status", "error");
                    json.addProperty("message", e.getMessage());
                    out.println(json.toString());
                }
                break;
            case "deleteBook":
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                readerID = Integer.parseInt(request.getParameter("id"));
                String isbn = request.getParameter("isbn");
                out = response.getWriter();
                json = new JsonObject();
                Book book = new Book(isbn);
                try {
                    book.deleteReader(readerID);
                    json.addProperty("status", "success");
                    json.addProperty("message", "The book deleted successfully");
                    out.print(json.toString());
                } catch (Exception e) {
                    json.addProperty("status", "error");
                    json.addProperty("message", e.getMessage());
                    out.println(json.toString());
                }
                break;
            case "delete":
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                readerID = Integer.parseInt(request.getParameter("id"));
                out = response.getWriter();
                json = new JsonObject();
                reader = new Reader(readerID);
                try {
                    reader.delete();
                    json.addProperty("status", "success");
                    json.addProperty("message", "The reader deleted successfully");
                    out.print(json.toString());
                } catch (Exception e) {
                    json.addProperty("status", "error");
                    json.addProperty("message", e.getMessage());
                    out.println(json.toString());
                }
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null){
            response.sendRedirect("auth");
            return;
        }
        if (request.getParameter("id") != null &&
                (user instanceof Librarian || ((Reader) user).getId() == Integer.parseInt(request.getParameter("id")))){
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Reader reader = new Reader(Integer.parseInt(request.getParameter("id")));
                request.setAttribute("books", reader.getBooks());
                request.setAttribute("reader", Reader.findById(id));
                request.getRequestDispatcher("./reader.jsp").forward(request, response);
            } catch (Exception ignored) {

            }
        }
        request.setAttribute("readers", Reader.getAllReaders());
        request.getRequestDispatcher("./allReaders.jsp").forward(request, response);
    }
}
