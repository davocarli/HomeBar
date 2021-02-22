<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <link rel="stylesheet" type="text/css" href="/css/uikit.min.css">
    <script src="/js/uikit.min.js"></script>
    <script src="/js/uikit-icons.min.js"></script>
    <script src="/js/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/selectize.css">
    <link rel="stylesheet" type="text/css" href="/css/selectize.bootstrap3.css">
    <script src="/js/selectize.min.js"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	<script>
		$(function() {
			$('.selectize').selectize({
				plugins: ['remove_button'],
				delimiter: '|',
				create: true,
			});
		})
	</script>
	</head>
	<body>
		<!-- NAVBAR -->
		<nav class="uk-navbar-container" uk-navbar>
			<div class="uk-navbar-center">
				<div class="uk-navbar-center-left">
					<ul class="uk-navbar-nav">
						<li class="uk-active"><a href="/bar">My Bar</a></li>
						<li><a href="/shopping">Shopping List</a></li>
					</ul>
				</div>
				<a class="uk-navbar-item uk-logo" href="#">home-bar.app</a>
				<div class="uk-navbar-center-right">
					<ul class="uk-navbar-nav">
						<li><a href="/drinks">Make a Drink</a></li>
						<li><a href="/drinks/new">Add a Drink</a></li>
					</ul>
				</div>
			</div>
			<div class="uk-navbar-right">
				<a href="/profile" class="uk-button uk-button-default">Profile</a>
			</div>
		</nav>
		<!-- CONTENT -->
		<div style="padding: 50px">
			<h3>Edit ingredient</h3>
			<form:form method="POST" action="/ingredients/${ ingredient.id }/edit" modelAttribute="ingredient">
				<span class="uk-text-danger">${ formErrors }</span>
				<fieldset class="uk-fieldset">
					<div class="uk-inline uk-width-2-2">
						<form:input class="uk-input uk-form-width-large" path="name" placeholder="Ingredient Name"/>
						<form:input class="uk-input uk-form-width-large selectize" multiple="multiple" path="substituteNames" placeholder="This ingredient can also substitute for..."/>
						<input class="uk-button uk-inline uk-button-primary" type="submit" value="UPDATE"/>
						<!--TODO: DYNAMICALLY ADD SUGGESTED OPTIONS BASED ON OTHER INGREDIENTS IN DATABASE -->
					</div>
				</fieldset>
			</form:form>
		</div>
	</body>
</html>