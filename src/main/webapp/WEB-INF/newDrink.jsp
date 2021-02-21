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
	<script src="/js/newDrink.js"></script>
	</head>
	<body>
		<!-- NAVBAR -->
		<nav class="uk-navbar-container" uk-navbar>
			<div class="uk-navbar-center">
				<div class="uk-navbar-center-left">
					<ul class="uk-navbar-nav">
						<li><a href="/bar">My Bar</a></li>
						<li><a href="/shopping">Shopping List</a></li>
					</ul>
				</div>
				<a class="uk-navbar-item uk-logo" href="#">home-bar.app</a>
				<div class="uk-navbar-center-right">
					<ul class="uk-navbar-nav">
						<li><a href="/drinks">Make a Drink</a></li>
						<li class="uk-active"><a href="/drinks/new">Add a Drink</a></li>
					</ul>
				</div>
			</div>
			<div class="uk-navbar-right">
				<a href="/profile" class="uk-button uk-button-default">Profile</a>
			</div>
		</nav>
		<!-- CONTENT -->
		<div style="padding: 50px;">
			<h3>Submit a new recipe</h3>
			<form>
				<fieldset class="uk-fieldset">
					<input class="uk-input uk-form-width-large" type="text" id="name" placeholder="Recipe Name"/>
				</fieldset>
				<div id="ingredients">
				</div>
				<a id="addIngredient">Add Ingredient</a>
				<fieldset class="uk-fieldset">
					<textarea class="uk-textarea uk-form-width-large" id="instructions" placeholder="Drink Instructions..." rows="10" style="width: 1134px;"></textarea>
					<input class="uk-input" type="text" id="source" placeholder="Source (Give credit if this is not your own recipe)." style="width: 1134px; margin-top: 10px;"/>
				</fieldset>
			</form>
			<button class="uk-button uk-button-primary" id="submit">SUBMIT</button>
		</div>
	</body>
</html>