import com.google.gson.JsonObject;
import comparators.AuthorComparator;
import comparators.NameComparator;
import models.Book;
import models.Librarian;
import models.Reader;
import org.glassfish.jersey.client.ClientConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BookServlet")
public class BookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String submit = request.getParameter("submit");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        JsonObject json = new JsonObject();
        switch (submit){
            case "borrow":
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                Book book = new Book(request.getParameter("isbn"), Integer.parseInt(request.getParameter("copies")));
                try {
                    Reader reader = (Reader) session.getAttribute("user");
                    book.borrow(reader.getId());
                    json.addProperty("status", "success");
                    json.addProperty("message", "The book is borrowed successfully");
                    out.print(json.toString());
                } catch (Exception e) {
                    json.addProperty("status", "error");
                    json.addProperty("message", e.getMessage());
                    out.print(json.toString());
                }
                break;
            case "update":
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                String isbn = request.getParameter("isbn");
                String name = request.getParameter("name");
                String author = request.getParameter("author");
                Book updatedBook = new Book(isbn, name, author);
                try {
                    updatedBook.update();
                    json.addProperty("status", "success");
                    json.addProperty("message", "Data updated successfully");
                    out.print(json.toString());
                } catch (SQLException throwables) {
                    json.addProperty("status", "error");
                    json.addProperty("message", throwables.getMessage());
                    out.print(json.toString());
                }
                break;
            case "delete":
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                isbn = request.getParameter("isbn");
                json = new JsonObject();
                out = response.getWriter();

                Book deleted = new Book(isbn);

                try {
                    deleted.delete();
                    json.addProperty("status", "success");
                    json.addProperty("message", "The book is deleted successfully");
                    out.print(json.toString());
                } catch (SQLException throwables) {
                    json.addProperty("status", "error");
                    json.addProperty("message", throwables.getMessage());
                    out.print(json.toString());
                }
                break;
            case "deleteReader":
                isbn = request.getParameter("isbn");
                String readerID = request.getParameter("readerID");
                deleted = new Book(isbn);
                try {
                    deleted.deleteReader(Integer.parseInt(readerID));
                    json.addProperty("status", "success");
                    json.addProperty("message", "The reader deleted successfully");
                    out.print(json.toString());
                } catch (Exception e) {
                    json.addProperty("status", "error");
                    json.addProperty("message", e.getMessage());
                    out.print(json.toString());
                }
                break;
            case "addReader":
                isbn = request.getParameter("isbn");
                String copies = request.getParameter("copies");
                readerID = request.getParameter("readerID");
                book = new Book(isbn, Integer.parseInt(copies));
                try {
                    book.addReader(Integer.parseInt(readerID));
                    json.addProperty("status", "success");
                    json.addProperty("message", "The reader added successfully");
                    out.print(json.toString());
                } catch (Exception e) {
                    json.addProperty("status", "error");
                    json.addProperty("message", e.getMessage());
                    out.print(json.toString());
                }
                break;
            default:
                break;
        }
    }

    private WebTarget getWebTarget(){
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        String baseURI = "http://localhost:8090/My_RESTful_Service_war/api/book";
        return client.target(baseURI);
    }

    private ArrayList<Book> getBook(){
        WebTarget target = getWebTarget();
        return target.request(MediaType.APPLICATION_JSON).get(Response.class).readEntity(new GenericType<ArrayList<Book>>(){});
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
        if (user == null){
            response.sendRedirect("auth");
            return;
        }
        if (user instanceof Reader){
            System.out.println(((Reader) user).getId());
        }
        if (request.getParameter("isbn") != null && user instanceof Librarian){
            try {
                Book book = new Book(request.getParameter("isbn"));
                request.setAttribute("readers", book.getReaders());
                request.setAttribute("book", Book.findByIsbn(request.getParameter("isbn")));
                request.getRequestDispatcher("./book.jsp").forward(request, response);
            } catch (Exception ignored) {

            }
        }
        ArrayList<Book> getBooks = Book.getAllBooks();
        if(request.getParameter("sort") != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            if (request.getParameter("sort").equals("name"))
                getBooks.sort(new NameComparator());
            if (request.getParameter("sort").equals("author"))
                getBooks.sort(new AuthorComparator());
        }
        request.setAttribute("books", getBooks);
        request.getRequestDispatcher("./main.jsp").forward(request, response);
    }
}
