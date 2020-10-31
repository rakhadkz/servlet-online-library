<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="models.Book" %>
<%@ page import="models.Librarian" %>
<%@ page import="models.Reader" %><%--

  Created by IntelliJ IDEA.
  User: rakhad
  Date: 10/26/20
  Time: 13:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <!-- jQuery and JS bundle w/ Popper.js -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <title>Library</title>
    <style type="text/css">
        .col {
            box-shadow: 0 1px 2px rgba(0,0,0,0.15);
            transition: box-shadow 0.3s ease-in-out;
        }
        .col:hover {
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container mt-5 p-5">
    <div style="display: flex;">
        <button class="btn btn-link btn-sort-name">Sort by Book name</button>
        <button class="btn btn-link btn-sort-author">Sort by Author</button>
    </div>
    <script>
        $('.btn-sort-name').click(function(){
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    window.open("http://localhost:8080/Assignment8_war/books?sort=name", "_self");
                }
            };
            xhttp.open("GET", "http://localhost:8080/Assignment8_war/books?sort=name", true);
            xhttp.send();
        })
        $('.btn-sort-author').click(function(){
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    window.open("http://localhost:8080/Assignment8_war/books?sort=author", "_self");
                }
            };
            xhttp.open("GET", "http://localhost:8080/Assignment8_war/books?sort=author", true);
            xhttp.send();
        })
    </script>
    <%
        int readerId = -1;
        if (request.getSession().getAttribute("user") instanceof Reader){
            readerId = ((Reader) request.getSession().getAttribute("user")).getId();
        }
    %>
    <input type="hidden" id="readerId" value="<%=readerId%>">
    <ul class="list-group">
        <c:forEach var="book" items="${books}">
            <li class="list-group-item">
                <div style="float: left;">
                    <b>${book.name}</b><br>
                    <small>${book.author}</small><br>
                    <small class="text-muted">${book.copies} copies</small>
                </div>
                <div style="float: right;">
                    <div>
                        <input type="hidden" value="${book.isbn}" class="isbn"/>
                        <input type="hidden" value="${book.copies}" class="copies"/>
                        <%
                            if (request.getSession().getAttribute("user") instanceof Librarian){
                                out.print("<a class=\"btn btn-outline-primary btn-edit mr-2\">Edit</a>");
                                out.print("<button class=\"btn btn-outline-danger btn-delete\">Delete</button>");
                        }else{
                                out.print("<button class=\"btn btn-outline-primary btn-borrow\">Borrow</button>");
                            }
                        %>
                    </div>
                </div>
            </li>
        </c:forEach>
        <script>
            $('.btn-edit').click(function() {
                isbn = $(this).siblings('.isbn').val();
                window.open("http://localhost:8080/Assignment8_war/books?isbn=" + isbn, '_self');
            })
            $('.btn-delete').click(function(){
                isbn = $(this).siblings('.isbn').val();
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function ()
                {
                    if (this.readyState == 4)
                    {
                        var response = JSON.parse(this.responseText).message;
                        console.log(response);
                        alert(response)
                        if (this.status == 200){
                            window.open("http://localhost:8080/Assignment8_war/books", "_self")
                        }
                    }
                };
                xhttp.open("DELETE", "http://localhost:8090/My_RESTful_Service_war/api/book/" + isbn, true);
                xhttp.setRequestHeader('Content-type', 'application/json');
                xhttp.send("");
            })
            $('.btn-borrow').click(function(){
                isbn = $(this).siblings('.isbn').val();
                copies = $(this).siblings('.copies').val();
                readerId = $("#readerId").val();
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function ()
                {
                    if (this.readyState == 4){
                        var response = JSON.parse(this.responseText).message;
                        console.log(response);
                        alert(response);
                    }
                };
                xhttp.open("POST", "http://localhost:8090/My_RESTful_Service_war/api/reader/addBook", true);
                xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                xhttp.send("isbn=" + isbn + "&readerId=" + readerId + "&copies=" + copies);
            })
        </script>
    </ul>
</div>
</body>
</html>
