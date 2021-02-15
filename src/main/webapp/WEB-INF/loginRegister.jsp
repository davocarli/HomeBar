<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <link href="/uikit-3.6.16/css/uikit.min.css">
    <script src="/uikit-3.6.16/js/uikit-min.js"></script>
    <script src="/uikit-3.6.16/js/uikit-icons-min.js"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body">
	<div id="register-div" style="width: 49%; display: inline-block;">
		<h2>Register</h2>
		<p class="text-danger">${ registrationErrors }</p>
		<form:form method="POST" action="/register" modelAttribute="user">
			<p class="text-danger"><form:errors path="user.*"/></p>
			<div class="mb-3">
				<form:label class="form-label" path="name">First Name:</form:label>
				<form:input class="form-control" path="name"/>
			</div>
			<div class="mb-3">
				<form:label class="form-label" path="email">Email:</form:label>
				<form:input type="email" class="form-control" path="email"></form:input>
			</div>
			<div class="mb-3">
				<form:label class="form-label" type="password" path="password">Password:</form:label>
				<form:input class="form-control" type="password" path="password"/>
			</div>
			<div class="mb-3">
				<form:label class="form-label" type="password" path="passwordConfirmation">Confirm Password:</form:label>
				<form:input class="form-control" type="password" path="passwordConfirmation"/>
			</div>
			<input class="btn btn-secondary rounded-pill float-end" type="submit" value="Register"/>
		</form:form>
	</div>
	<div id="login-div" style="width: 49%; display: inline-block; float: right;">
		<h2>Login</h2>
		<p class="text-danger"><c:out value="${ loginErrors }"/></p>
		<form method="post" action="/login">
			<div class="mb-3">
				<label class="form-label" type="email" for="email">Email:</label>
				<input class="form-control" type="email" id="email" name="email"/>
			</div>
			<div class="mb-3">
				<label class="form-label" for="password">Password:</label>
				<input class="form-control" type="password" id="passtword" name="password"/>
			</div>
			<input class="btn btn-secondary rounded-pill float-end" type="submit" value="Login"/>
		</form>
	</div>
</body>