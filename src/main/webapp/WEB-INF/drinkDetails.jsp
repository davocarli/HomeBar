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
						<li class="uk-active"><a href="/">Make a Drink</a></li>
						<li><a href="/drinks/new">Add a Drink</a></li>
					</ul>
				</div>
			</div>
			<div class="uk-navbar-right">
				<c:if test="${ user != null }"><a href="/profile" class="uk-button uk-button-default">Profile</a></c:if><c:if test="${ user == null }"><a href="/login" class="uk-button uk-button-default">LOG IN</a></c:if>
			</div>
		</nav>
		<!-- CONTENT -->
		<div style="padding: 50px;">
			<c:choose>
				<c:when test="${ recipe.image.length() > 0 }">	
					<div class="uk-card uk-card-default uk-grid-collapse uk-child-width-1-2@s uk-margin" uk-grid>
					    <div class="uk-flex-last@s uk-card-media-right uk-cover-container drink-detail-image">
					        <img class="uk-cover-container" uk-cover src="https://s3-us-west-2.amazonaws.com/home-bar.app/recipeImages/1000/${ recipe.image }" alt="cocktail" onerror="this.style.display='none'">
					    </div>
					    <div>
					        <div class="uk-card-body">
					            <h3 style="display: inline-block;" class="uk-card-title">${ recipe.name }</h3><c:if test="${ recipe.creator.id == user.id }"> <a href="/drinks/${ recipe.id }/edit" class="uk-link-text">Edit</a></c:if>
					            <h4>Ingredients</h4>
					            <ul>
					            	<c:forEach items="${ recipe.ingredients }" var="ingredient">
					            		<li>${ ingredient.name } (${ ingredient.amount })</li>
					            	</c:forEach>
					            </ul>
					            <h4>Method</h4>
					            <p style="white-space: pre-line;">${ recipe.instructions }<c:if test="${ recipe.source.length() > 0 }"><br><br><b>Source:</b> ${ recipe.source }</c:if></p>
					        </div>
					    </div>
					</div>
				</c:when>
				<c:otherwise>
					<div style="padding: 20px;" class="uk-card uk-card-default uk-grid-collapse uk-child-width-1-2@s uk-margin" uk-grid>
			            <h3 style="display: inline-block;" class="uk-card-title">${ recipe.name }</h3><c:if test="${ recipe.creator.id == user.id }"> <a href="/drinks/${ recipe.id }/edit" class="uk-link-text">Edit</a></c:if>
			            <div>
			            	<h4>Ingredients</h4>
			            	<ul>
			            		<c:forEach items="${ recipe.ingredients }" var="ingredient">
			            			<li>${ ingredient.name } (${ ingredient.amount })</li>
			            		</c:forEach>
			            	</ul>
			            </div>
			            <div>
			            	<h4>Method</h4>
			            	<p style="white-space: pre-line;">${ recipe.instructions }<c:if test="${ recipe.source.length() > 0 }"><br><br><b>Source:</b> ${ recipe.source }</c:if></p>
			            </div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>