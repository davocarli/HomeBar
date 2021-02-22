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
	<script src="/js/drinkList.js"></script>
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
						<li class="uk-active"><a href="/drinks">Make a Drink</a></li>
						<li><a href="/drinks/new">Add a Drink</a></li>
					</ul>
				</div>
			</div>
			<div class="uk-navbar-right">
				<a href="/profile" class="uk-button uk-button-default">Profile</a>
			</div>
		</nav>
		<!-- CONTENT -->
		<div style="padding: 50px;">
			<div>
				<h2 style="display: inline-block;">${ recipe.name }</h2><c:if test="${ recipe.creator.id == user.id }"> <a href="/drinks/${ recipe.id }/edit" class="uk-link-text">Edit</a></c:if>
			</div>
			<div class="uk-text-top" style="width: 49%; display: inline-block;">
			<h3>Ingredients</h3>
			<ul>
				<c:forEach items="${ recipe.ingredients }" var="ingredient">
					<li>${ ingredient.name } (${ ingredient.amount })</li>
				</c:forEach>
			</ul>
			<c:if test="${ recipe.image.length() == 0 || recipe.image == null }"></div><div style="width: 49%; display: inline-block;"></c:if>
			<h3>Instructions</h3>
			<p style="white-space: pre-line">${ recipe.instructions }
				<c:if test="${ recipe.source.length() > 0 }"><br><br><b>Source:</b> ${ recipe.source }</c:if>
			</p>
			</div>
			<c:if test="${ recipe.image.length() > 0 }">
				<div style="width: 49%; display: inline-block;">
					<img src="${ recipe.image }" alt="cocktail">
				</div>
			</c:if>
		</div>
	</body>
</html>