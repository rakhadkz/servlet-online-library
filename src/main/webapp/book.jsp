<%@ page import="models.Book" %><%--
  Created by IntelliJ IDEA.
  User: rakhad
  Date: 10/26/20
  Time: 21:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <!-- jQuery and JS bundle w/ Popper.js -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <title>Library</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container mt-2 p-5">
    <%
        Book book = (Book) request.getAttribute("book");
    %>
    <input type="hidden" id="isbn" value="<%=book.getIsbn()%>">
    <input type="hidden" id="copies" value="<%=book.getCopies()%>">
    <div class="text-center">
        <b class="h1"><%=book.getName()%></b><br>
        <small class="text-muted h4"><%=book.getAuthor()%></small>
    </div><br>
    <div class="row">
        <div class="col p-5">
            <b class="h4">Update main info</b>
            <hr>
            <form>
                <div class="form-group">
                    <label for="bookName">Book name</label>
                    <input type="text" class="form-control" name="name" id="bookName" value="<%=book.getName()%>" >
                </div>
                <div class="form-group">
                    <label for="bookAuthor">Book author</label>
                    <input type="text" class="form-control" name="author" id="bookAuthor" value="<%=book.getAuthor()%>" >
                </div>
                <button type="button" class="btn btn-primary btn-update">Update</button>
            </form>
        </div>
        <script>
            $(".btn-update").click(function(){
                name = $("#bookName").val();
                author = $("#bookAuthor").val();
                isbn = $("#isbn").val();
                if (!name || !author){
                    alert("Please, fill all fields");
                    return;
                }
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function ()
                {
                    if (this.readyState == 4){
                        var response = JSON.parse(this.responseText).message;
                        alert(response)
                        location.reload();
                    }
                };
                xhttp.open("PUT", "http://localhost:8090/My_RESTful_Service_war/api/book", true);
                xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhttp.send("isbn=" + isbn + "&name=" + name + "&author=" + author);
            })
        </script>
        <div class="col p-5">
            <b class="h4">Borrowers</b>
            <hr>
            <form>
                <div class="form-group">
                    <label for="readerID">Student id</label>
                    <input type="text" class="form-control" name="readerID" id="readerID" placeholder="Type student id" required>
                </div>
                <button type="button" class="btn btn-primary btn-add-reader">Add</button>
            </form>
            <script>
                $('.btn-add-reader').click(function(){
                    event.preventDefault()
                   id = $("#readerID").val();
                   isbn = $("#isbn").val();
                   copies = $("#copies").val();
                   if (!id){
                       alert("Please, enter reader id");
                       return;
                   }
                    var xhttp = new XMLHttpRequest();
                    xhttp.onreadystatechange = function ()
                    {
                        if (this.readyState == 4){
                            var response = JSON.parse(this.responseText).message;
                            alert(response)
                            location.reload();
                        }
                    };
                    xhttp.open("POST", "http://localhost:8090/My_RESTful_Service_war/api/reader/addBook", true);
                    xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                    xhttp.send("isbn=" + isbn + "&readerId=" + id);
                });
            </script>
            <ul class="list-group">
                <c:forEach var="reader" items="${readers}">
                    <li class="list-group-item">
                            <form style="margin: auto;">
                                <span>${reader.firstName} ${reader.lastName}</span>
                                <input type="hidden" class="readerID" value="${reader.id}">
                                <button type="button" class="btn btn-danger float-right btn-delete-reader">Delete</button>
                            </form>
                    </li>
                </c:forEach>
                <script>
                    $('.btn-delete-reader').click(function(){
                        isbn = $('#isbn').val();
                        id = $(this).siblings('.readerID').val();
                        var xhttp = new XMLHttpRequest();
                        xhttp.onreadystatechange = function ()
                        {
                            if (this.readyState == 4){
                                var response = JSON.parse(this.responseText).message;
                                alert(response)
                                location.reload();
                            }
                        };
                        xhttp.open("POST", "http://localhost:8090/My_RESTful_Service_war/api/reader/removeBook", true);
                        xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                        xhttp.send("&isbn=" + isbn + "&readerId=" + id);
                    })
                </script>
            </ul>
            <script>
                $('.btn-reader-delete').click(function(){
                    alert($(this).siblings('.readerID').val())
                })
            </script>
        </div>
    </div>
    <div class="text-center text-muted">
        <small>You can
            <a href="" class="btn-delete" style="color: red;">delete</a>
            <input type="hidden" class="isbn" value="<%=book.getIsbn()%>">
            the book</small>
    </div>
    <script>
        $('.btn-delete').click(function(){
            event.preventDefault();
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
    </script>

</div>
</body>
</html>


