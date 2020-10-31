<%--
  Created by IntelliJ IDEA.
  User: rakhad
  Date: 10/27/20
  Time: 20:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="./fontawesome-free-5.15.1-web/css/all.css">
    <!-- jQuery and JS bundle w/ Popper.js -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <title>New reader</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container mt-5 p-5">
    <div class="text-center px-5">
        <p class="h1">New reader</p>
        <hr>
    </div>
    <form class="px-5" action="createReader" method="post">
        <div class="form-group">
            <label for="firstName">First Name</label>
            <input type="text" class="form-control" name="firstName" id="firstName" required>
        </div>
        <div class="form-group">
            <label for="lastName">Last Name</label>
            <input type="text" class="form-control" name="lastName" id="lastName" required>
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control" name="email" id="email" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" name="password" id="password" required>
        </div>
        <%
            if (request.getAttribute("error") != null){
                out.print("<div class=\"alert alert-danger\" role=\"alert\">");
                out.print((String) request.getAttribute("error"));
                out.print("</div>");
            }
            if (request.getAttribute("success") != null){
                out.print("<div class=\"alert alert-success\" role=\"alert\">");
                out.print((String) request.getAttribute("success"));
                out.print("</div>");
            }
        %>
        <button type="submit" class="btn btn-primary">Register</button>
    </form>
</div>
</body>
</html>
