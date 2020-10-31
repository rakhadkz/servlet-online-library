<%@ page import="models.Reader" %>
<%@ page import="models.Librarian" %><%--
  Created by IntelliJ IDEA.
  User: rakhad
  Date: 10/26/20
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo01" aria-controls="navbarTogglerDemo01" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <%
        String fullName = "";
        boolean isReader = false;
        HttpSession httpSession = request.getSession();
        Object object = httpSession.getAttribute("user");
        if (object instanceof Reader){
            isReader = true;
            fullName = ((Reader) object).getFirstName() + " " + ((Reader) object).getLastName();
        }else if (object instanceof Librarian){
            fullName = ((Librarian) object).getUsername();
        }else{
            fullName = "";
        }
    %>
    <div class="collapse navbar-collapse px-5" id="navbarTogglerDemo01">
        <a class="navbar-brand" href="books">Library</a>
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <li class="nav-item">
                <a class="nav-link" href="books">Main</a>
            </li>
            <%
                if (isReader){
                    out.print("<li class=\"nav-item\">");
                    out.print("<a href='readers?id="+ ((Reader) object).getId() + "' class=\"nav-link\" href=\"#\">Borrowed books</a>");
                    out.print("</li>");
                }else{
                    out.print("<li class=\"nav-item\">");
                    out.print("<a href='createBook' class=\"nav-link\" href=\"#\">New book</a>");
                    out.print("</li>");
                    out.print("<li class=\"nav-item\">");
                    out.print("<a href='createReader' class=\"nav-link\" href=\"#\">New reader</a>");
                    out.print("</li>");
                    out.print("<li class=\"nav-item\">");
                    out.print("<a href='readers' class=\"nav-link\" href=\"#\">Readers</a>");
                    out.print("</li>");
                }
            %>
        </ul>
        <div class="form-inline my-2 my-lg-0">
            <div class="btn-group">
                <a href="" class="nav-link text-light dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <%=fullName%>
                </a>
                <div class="dropdown-menu dropdown-menu-right">
                    <%
                        if (object instanceof Reader){
                            out.print("<a class=\"dropdown-item\" href='readers?id=" + ((Reader) object).getId() +"'>Profile</a>");
                        }
                    %>
                    <div class="dropdown-divider"></div>
                    <form action="auth" method="post">
                        <button type="submit" class="dropdown-item">Log out</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</nav>