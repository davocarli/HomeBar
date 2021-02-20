<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
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
			<!-- login -->
			<form:form method="POST" action="/login">
				<fieldset class="uk-fieldset">
					<div class="uk-margin-small">
						<div class="uk-inline uk-width-1-1">
							<span class="uk-form-icon uk-form-icon-flip" data-uk-icon="icon: user"></span>
							<input class="uk-input uk-border-pill" required placeholder="email" type="text">
						</div>
					</div>
					<div class="uk-margin-small">
						<div class="uk-inline uk-width-1-1">
							<span class="uk-form-icon uk-form-icon-flip" data-uk-icon="icon: lock"></span>
							<input class="uk-input uk-border-pill" required placeholder="password" type="password">
						</div>
					</div>
					<div class="uk-margin-bottom">
						<button type="submit" class="uk-button uk-button-primary uk-border-pill uk-width-1-1">LOG IN</button>
					</div>
				</fieldset>
			</form:form>
			<!-- /login -->
			<div class="uk-text-center">
				<a class="uk-link-reset uk-text-small toggle-class" href="/register">Register</a>
			</div>
		</div>
</body>