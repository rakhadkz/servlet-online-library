<%@ page import="models.Reader" %><%--
  Created by IntelliJ IDEA.
  User: rakhad
  Date: 10/27/20
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <!-- jQuery and JS bundle w/ Popper.js -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <title>Reader</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container mt-2 p-5">
    <%
        Reader reader = (Reader) request.getAttribute("reader");
    %>
    <div class="text-center">
        <b class="h1"><%=reader.getFirstName() + " " + reader.getLastName()%></b><br>
        <small class="text-muted h4"><%=reader.getEmail()%></small>
    </div><br>
    <div class="row">
        <div class="col p-5">
            <b class="h4">Update personal info</b>
            <hr>
            <form>
                <input type="hidden" id="submit" value="update">
                <input type="hidden" id="readerID" value="<%=reader.getId()%>">
                <div class="form-group">
                    <label for="firstName">First Name</label>
                    <input type="text" class="form-control" name="firstName" id="firstName" value="<%=reader.getFirstName()%>" required>
                </div>
                <div class="form-group">
                    <label for="lastName">Last Name</label>
                    <input type="text" class="form-control" name="lastName" id="lastName" value="<%=reader.getLastName()%>" required>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="text" class="form-control" name="email" id="email" value="<%=reader.getEmail()%>" required>
                </div>
                <button type="button" class="btn btn-primary" id="btn-update">Update</button>
            </form>
            <br>
            <b class="h4">Change password</b>
            <hr>
            <form>
                <input type="hidden" name="submit" value="update">
                <div class="form-group">
                    <label for="password">Current password</label>
                    <input type="text" class="form-control" name="password" id="password" value="" required>
                </div>
                <div class="form-group">
                    <label for="newPassword">New password</label>
                    <input type="text" class="form-control" name="newPassword" id="newPassword" value="" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm new password</label>
                    <input type="text" class="form-control" name="confirmPassword" id="confirmPassword" value="" required>
                </div>
                <button type="button" class="btn btn-primary btn-change-password" id="btn-change-password">Change</button>
            </form>
            <script>
                $('#btn-update').click(function(){
                    email = $("#email").val();
                    firstName = $("#firstName").val();
                    lastName = $("#lastName").val();
                    id = $("#readerID").val();
                    if (!email || !firstName || !lastName){
                        alert('Please, fill all fields');
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
                    xhttp.open("PUT", "http://localhost:8090/My_RESTful_Service_war/api/reader", true);
                    xhttp.setRequestHeader('Content-type', 'application/json');
                    xhttp.send(JSON.stringify({
                        id: id,
                        firstName: firstName,
                        lastName: lastName,
                        email: email
                    }))
                });

                $('#btn-change-password').click(function(){
                    event.preventDefault();
                    id = $('#readerID').val();
                    password = $("#password").val();
                    newPassword = $('#newPassword').val();
                    confirmPassword = $('#confirmPassword').val()
                    if (!newPassword || !password || !confirmPassword){
                        alert("Please, fill all fields");
                        return;
                    }
                    if (newPassword != confirmPassword){
                        alert("Passwords do not match");
                        return;
                    }
                    var xhttp = new XMLHttpRequest();
                    xhttp.onreadystatechange = function ()
                    {
                        if (this.readyState == 4){
                            var response = JSON.parse(this.responseText).message;
                            alert(response)
                            if (this.status == 200)
                                location.reload();
                        }
                    };
                    xhttp.open("PUT", "http://localhost:8090/My_RESTful_Service_war/api/reader/updatePassword", true);
                    xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                    xhttp.send("readerId=" + id + "&oldP=" + password + "&newP=" + newPassword);
                })
            </script>
        </div>
        <div class="col p-5">
            <b class="h4">Borrowed books</b>
            <hr>
            <form>
                <div class="form-group">
                    <label for="isbn">ISBN</label>
                    <input type="text" class="form-control" name="isbn" id="isbn" placeholder="Type a book isbn" required>
                </div>
                <button type="button" class="btn btn-primary" id="btn-add-book">Add</button>
            </form>
            <ul class="list-group">
                <c:forEach var="book" items="${books}">
                    <li class="list-group-item">
                        <form style="margin: auto;">
                            <span>${book.name}</span><br>
                            <span>${book.author}</span>
                            <input type="hidden" class="isbn" value="${book.isbn}">
                            <button type="button" class="btn btn-danger float-right btn-delete">Delete</button>
                        </form>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <script>
            $(".btn-delete").click(function(){
                isbn = $(this).siblings('.isbn').val();
                id = $("#readerID").val();
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function ()
                {
                    if (this.readyState == 4){
                        var response = JSON.parse(this.responseText).message;
                        alert(response)
                        if (this.status == 200)
                            location.reload();
                    }
                };
                xhttp.open("POST", "http://localhost:8090/My_RESTful_Service_war/api/reader/removeBook", true);
                xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                xhttp.send("readerId=" + id + "&isbn=" + isbn);
            })
            $("#btn-add-book").click(function(){
               isbn = $("#isbn").val();
               id = $("#readerID").val();
               if (!isbn){
                   alert("ISBN cannot be empty");
                   return;
               }
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function ()
                {
                    if (this.readyState == 4){
                        var response = JSON.parse(this.responseText).message;
                        alert(response)
                        if (this.status == 200)
                            location.reload();
                    }
                };
                xhttp.open("POST", "http://localhost:8090/My_RESTful_Service_war/api/reader/addBook", true);
                xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                xhttp.send("isbn=" + isbn + "&readerId=" + id);
            });
        </script>
    </div>
    <div class="text-center text-muted">
        <small>You can
            <a href="" class="btn-delete-reader" style="color: red;">delete</a>
            the reader</small>
    </div>
    <script>
        $('.btn-delete-reader').click(function(){
            event.preventDefault()
            id = $("#readerID").val();
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function ()
            {
                if (this.readyState == 4)
                {
                    var response = JSON.parse(this.responseText).message;
                    console.log(response);
                    alert(response)
                    if (this.status == 200){
                        window.open("http://localhost:8080/Assignment8_war/readers", "_self")
                    }
                }
            };
            xhttp.open("DELETE", "http://localhost:8090/My_RESTful_Service_war/api/reader/" + id, true);
            xhttp.setRequestHeader('Content-type', 'application/json');
            xhttp.send("");
        });
    </script>
</div>
</body>
</html>
