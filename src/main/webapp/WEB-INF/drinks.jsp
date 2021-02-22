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
			<h3>Make a drink!</h3>
			<div uk-filter="target: .js-filter">
				<div class="uk-grid-small uk-grid-divider uk-child-width-auto" uk-grid>
					<div>
						<ul class="uk-subnav uk-subnav-pill" uk-margin>
							<li id="filter-reset" uk-filter-control="group: make"><a href="#">All Drinks</a></li>
							<li uk-filter-control="filter: [data-can-make='true']; group: make"><a href="#">Drinks I Can Make</a></li>
							<li uk-filter-control="filter: [data-creator='${ user.username }']; group: make"><a href="#">My Drinks</a></li>
						</ul>
					</div>
					<!--TODO: Dynamic filters based on ingredients -->
					<div>
						<ul class="uk-subnav uk-subnav-pill" uk-margin>
							<c:forEach items="${ stockedIngredients }" var="ingredient">
								<li class="drink-filter ${ ingredient.id }" data-filter-text="${ ingredient.getFullIngredient().toUpperCase() }" data-filter-group="${ ingredient.id }" uk-filter-control="filter: [data-ingredients*='${ ingredient.name.toUpperCase() }']; group: ${ ingredient.id }"><a href="#">${ ingredient.name }</a></li>
								<li class="drink-filter drink-filter-disable ${ ingredient.id }" uk-filter-control="group: ${ ingredient.id }" data-filter-group="${ ingredient.id }"><a href="#">${ ingredient.name }</a></li>
							</c:forEach>
						</ul>
					</div>
				</div>
			<ul class="js-filter uk-child-width-1-5 uk-child-width-1-5@m uk-text-center" uk-grid="masonry: true">
				<c:forEach items="${ drinks }" var="drink">
					<li class="drink-card" data-creator="${ drink.creator.username }" data-ingredients="|${ drink.getAllFullIngredients().toUpperCase() }|">
						<div class="uk-card uk-card-default">
							<c:if test="${ drink.image.length() > 0 }">
								<div class="uk-card-media-top">
									<img src="${ drink.image }" alt="cocktail">
								</div>
							</c:if>
							<div class="uk-card-body">
								<a class="uk-link-text" href="/drinks/${ drink.id }"><h3 class="uk-card-title">${ drink.name }</h3></a>
								<a class="uk-link-text" href="/drinks/${ drink.id }"><p>${ drink.ingredientList }</p></a>
							</div>
							<div class="uk-card-footer">
								<p class="uk-text-small uk-text-muted uk-text-left">Added by ${ drink.creator.username }</p>
							</div>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</body>
</html>