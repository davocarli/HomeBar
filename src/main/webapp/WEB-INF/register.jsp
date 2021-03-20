<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Register</title>
    <link rel="manifest" href="/manifest/manifest.json">
    <link rel="shortcut icon" type="image/png" href="/icons/icon_32.png"/>
    <link rel="stylesheet" type="text/css" href="/css/uikit.min.css">
    <script src="/js/uikit.min.js"></script>
    <script src="/js/uikit-icons.min.js"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body">
	<body class="uk-flex uk-flex-center uk-flex-middle uk-background-muted uk-height-viewport" data-uk-height-viewport>
		<div class="uk-width-medium uk-padding-small">
			<div class="uk-text-center">
				<h1>home-bar.app</h1>
			</div>
			<!-- register -->
			<form:form method="POST" action="/register" modelAttribute="user">
				<fieldset class="uk-fieldset">
					<div class="uk-margin-small">
						<div class="uk-inline uk-width-1-1">
							<form:errors path="firstName" class="uk-text-danger"/>
							<form:input class="uk-input uk-border-pill" required="true" placeholder="first name" path="firstName"/>
						</div>
					</div>
					<div class="uk-margin-small">
						<div class="uk-inline uk-width-1-1">
							<form:errors path="lastName" class="uk-text-danger"/>
							<form:input class="uk-input uk-border-pill" required="true" placeholder="last name" path="lastName"/>
						</div>
					</div>
					<div class="uk-margin-small">
						<div class="uk-inline uk-width-1-1">
							<form:errors path="email" class="uk-text-danger"/>
							<form:input class="uk-input uk-border-pill" required="true" type="email" placeholder="email address" path="email"/>
						</div>
					</div>
					<div class="uk-margin-small">
						<div class="uk-inline uk-width-1-1">
							<form:errors path="username" class="uk-text-danger"/>
							<form:input class="uk-input uk-border-pill" required="true" placeholder="public username" path="username"/>
						</div>
					</div>
					<div class="uk-margin-small">
						<div class="uk-inline uk-width-1-1">
							<form:errors path="password" class="uk-text-danger"/>
							<form:input class="uk-input uk-border-pill" required="true" type="password" placeholder="password" path="password"/>
						</div>
					</div>
					<div class="uk-margin-small">
						<div class="uk-inline uk-width-1-1">
							<form:errors path="passwordConfirmation" class="uk-text-danger"/>
							<form:input class="uk-input uk-border-pill" required="true" type="password" placeholder="confirm password" path="passwordConfirmation"/>
						</div>
					</div>
					<div class="uk-margin-bottom">
						<input class="uk-button uk-button-primary uk-border-pill uk-width-1-1" type="submit" value="REGISTER"/>
					</div>
				</fieldset>
			</form:form>
			<!-- /register -->
			<div class="uk-text-center">
				<a class="uk-link-reset uk-text-small toggle-class" href="/">Log in</a>
			</div>
		</div>
</body>