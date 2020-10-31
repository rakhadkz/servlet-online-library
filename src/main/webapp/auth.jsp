<%--
  Created by IntelliJ IDEA.
  User: rakhad
  Date: 10/25/20
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">

    <!-- jQuery and JS bundle w/ Popper.js -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
    <title>Authentication</title>
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
<div class="container mt-5">
    <div class="text-center">
        <p class="display-4">Welcome to AITU Library</p>
    </div>
    <div class="row">
        <div class="col card p-5 m-5">
            <form method="post" action="auth">
                <input type="hidden" name="role" value="librarian">
                <div class="form-group text-center">
                    <p class="h3">Librarians</p>
                    <hr>
                </div>
                <div class="form-group">
                    <label for="lib-username">Username</label>
                    <input type="text" class="form-control" name="username" id="lib-username">
                    <small class="form-text text-muted">Username is provided by administrator</small>
                </div>
                <div class="form-group">
                    <label for="lib-password">Password</label>
                    <input type="password" class="form-control" name="password" id="lib-password">
                </div>
                <%
                    if (request.getAttribute("lib_error") != null){
                        out.print("<div class='alert alert-danger'>");
                        out.print(request.getAttribute("lib_error"));
                        out.print("</div>");
                    }
                %>
                <button type="submit" class="btn btn-primary" id="lib-login">Login</button>
            </form>
        </div>
        <div class="col card p-5 m-5">
            <form action="auth" method="post">
                <input type="hidden" name="role" value="reader">
                <div class="form-group text-center">
                    <p class="h3">Readers</p>
                    <hr>
                </div>
                <div class="form-group">
                    <label for="rd-email">Email address</label>
                    <input type="email" class="form-control" name="email" id="rd-email" >
                    <small class="form-text text-muted">Reader is registered by Librarians</small>
                </div>
                <div class="form-group">
                    <label for="rd-password">Password</label>
                    <input type="password" class="form-control" name="password" id="rd-password">
                </div>
                <%
                    if (request.getAttribute("reader_error") != null){
                        out.print("<div class='alert alert-danger'>");
                        out.print(request.getAttribute("reader_error"));
                        out.print("</div>");
                    }
                %>
                <button type="submit" class="btn btn-primary">Login</button>
            </form>
        </div>
        <script>
            $('input').keypress(function() {
                $(this).parent().siblings('.alert').hide()
            })
        </script>
    </div>
</div>
</body>
</html>
