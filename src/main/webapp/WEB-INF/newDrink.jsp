<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>New Drink</title>
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
			<div class="uk-navbar-left">
				<a href="#offcanvas-menu" class="uk-button uk-button-default uk-hidden@m" uk-toggle>MENU</a>
			</div>
			<div class="uk-navbar-left">
				<c:if test="${ user != null }"><a href="/logout" class="uk-button uk-button-default uk-visible@m">Log out</a></c:if>
			</div>
			<div class="uk-navbar-center">
				<div class="uk-navbar-center-left uk-visible@m">
					<ul class="uk-navbar-nav">
						<li><a href="/bar">My Bar</a></li>
						<li><a href="/shopping">Shopping List</a></li>
					</ul>
				</div>
				<a class="uk-navbar-item uk-logo" href="#">home-bar.app</a>
				<div class="uk-navbar-center-right uk-visible@m">
					<ul class="uk-navbar-nav">
						<li><a href="/">Make a Drink</a></li>
						<li><a href="/drinks/new">Add a Drink</a></li>
					</ul>
				</div>
			</div>
			<div class="uk-navbar-right">
				<c:if test="${ user != null }"><a href="/profile" class="uk-button uk-button-default uk-visible@m">Profile</a></c:if><c:if test="${ user == null }"><a href="/login" class="uk-button uk-button-default uk-visible@m">LOG IN</a></c:if>
			</div>
		</nav>
		<div id="offcanvas-menu" uk-offcanvas="overlay: true;">
			<div class="uk-offcanvas-bar uk-light">
				<h3><a href="#offcanvas-menu" class="uk-offcanvas-close" uk-close></a></h3>
				<ul class="uk-nav uk-nav-primary">
					<li><a href="/bar">My Bar</a></li>
					<li><a href="/shopping">Shopping List</a></li>
					<li><a href="/">Make a Drink</a></li>
					<li class="uk-active"><a href="/drinks/new">Add a Drink</a></li>
					<li><c:choose><c:when test="${ user != null }"><a href="/profile">Profile</a></c:when><c:otherwise><a href="/login">Log In</a></c:otherwise></c:choose></li>
					<li><c:if test="${ user != null }"><a href="/logout">Log Out</a></c:if></li>
				</ul>
			</div>
		</div>
		<!-- CONTENT -->
		<div style="padding: 2% 5%;">
			<h3>Submit a new recipe</h3>
			<button id="upload-button" onclick="uploadFile()">UPLOAD</button>
			<form class="uk-grid-small" uk-grid>
				<div class="uk-width-5-6@s">
					<input class="uk-input" type="text" id="name" placeholder="Recipe Name"/>
				</div>
				<div class="uk-width-1-6@s">
					<div uk-form-custom="target: true">
						<input type="file" id="fileupload" name="fileupload" accept="image/*">
						<input class="uk-input" type="text" placeholder="Upload Image" disabled>
					</div>
				</div>
				<a class="uk-grid-1-1" id="addIngredient">Add Ingredient</a>
				<div class="uk-width-1-1">
					<textarea class="uk-textarea" id="instructions" placeholder="Drink Instructions..." rows="10"></textarea>
				</div>
				<div class="uk-width-1-1">
					<input class="uk-input" type="text" id="source" placeholder="Source (Give credit if this is not your original recipe)."/>
				</div>
			</form>
			<button class="uk-button uk-button-primary" id="submit">SUBMIT</button>
		</div>
	</body>
</html>