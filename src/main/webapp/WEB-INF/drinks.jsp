<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>home-bar.app</title>
    <link rel="stylesheet" type="text/css" href="/css/uikit.min.css">
    <script src="/js/uikit.min.js"></script>
    <script src="/js/uikit-icons.min.js"></script>
    <script src="/js/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/selectize.css">
    <link rel="stylesheet" type="text/css" href="/css/selectize.bootstrap3.css">
    <script src="/js/selectize.min.js"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	<c:if test="${ user != null }"><script src="/js/drinkList.js"></script></c:if>
	</head>
	<body>
		<!-- NAVBAR -->
		<nav class="uk-navbar-container" uk-navbar>
			<div class="uk-navbar-left">
				<a href="#offcanvas-menu" class="uk-button uk-button-default uk-hidden@m" uk-toggle>MENU</a>
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
						<li class="uk-active"><a href="/">Make a Drink</a></li>
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
					<li class="uk-active"><a href="/">Make a Drink</a></li>
					<li><a href="/drinks/new">Add a Drink</a></li>
				</ul>
			</div>
		</div>
		<!-- CONTENT -->
		<div style="background-color: #FFF">
			<h3 style="margin-left: 20px; margin-bottom: 5px;"><c:if test="${ user != null }">Hi ${ user.firstName }! </c:if>Let's Make a drink!</h3>
			<c:if test="${ user != null }">
				<div uk-filter="target: .js-filter" style="background-color: #FFF; padding-bottom: 0px;">
					<div uk-sticky style="padding: 0px 20px; background-color: #FFF">
						<div class="uk-grid-small uk-child-width-auto" style="margin-bottom: 5px; margin-top: 5px; background-color: #fff;" uk-grid>
							<span uk-icon="icon: chevron-left; ratio: 1.6" style="padding: 0px;"></span>
							<div class="filter-menu uk-overflow-auto uk-width-expand" style="background-color: #FFF">
								<ul class="uk-subnav uk-subnav-pill filter-menu uk-overflow-auto" uk-margin>
									<li class="filter-menu filter-visible" id="filter-reset" uk-filter-control="group: make"><a href="#">All Drinks</a></li>
									<li class="filter-menu filter-visible" uk-filter-control="filter: [data-can-make='true']; group: make"><a href="#">Drinks I Can Make</a></li>
									<li class="filter-menu filter-visible" uk-filter-control="filter: [data-creator='${ user.username }']; group: make"><a href="#">My Drinks</a></li>
									<li class="filter-divider">|</li>
									<c:forEach items="${ stockedIngredients }" var="ingredient">
										<li class="filter-menu filter-visible drink-filter ${ ingredient.id }" data-filter-text="${ ingredient.getFullIngredient().toUpperCase() }" data-filter-group="${ ingredient.id }" uk-filter-control="filter: [data-ingredients*='${ ingredient.name.toUpperCase() }']; group: ${ ingredient.id }"><a href="#">${ ingredient.name }</a></li>
										<li class="filter-menu drink-filter drink-filter-disable ${ ingredient.id }" uk-filter-control="group: ${ ingredient.id }" data-filter-group="${ ingredient.id }"><a href="#">${ ingredient.name }</a></li>
									</c:forEach>
								</ul>
							</div>
							<span uk-icon="icon: chevron-right; ratio: 1.6" style="padding: 0px;"></span>
						</div>
					</div>
			</c:if>
			<ul class="js-filter uk-child-width-1-1 uk-child-width-1-2@s uk-child-width-1-3@m uk-child-width-1-4@l uk-child-width-1-5@xl uk-text-center home-list" uk-grid="masonry: true">
				<c:forEach items="${ drinks }" var="drink">
					<li class="drink-card" data-creator="${ drink.creator.username }" data-ingredients="|${ drink.getAllFullIngredients().toUpperCase() }|">
						<div class="uk-card uk-card-default">
							<a class="uk-link-text link-card-body" href="/drinks/${ drink.id }">
							<c:if test="${ drink.image.length() > 0 }">
								<div class="uk-card-media-top home-image-div" style="background-image: url('https://s3-us-west-2.amazonaws.com/home-bar.app/recipeImages/500/${ drink.image }')">
									<img class="home-image" src="https://s3-us-west-2.amazonaws.com/home-bar.app/recipeImages/500/${ drink.image }" alt="cocktail" onerror="this.parentElement.style.display='none'">
								</div>
							</c:if>
							<span class="uk-card-body">
								<span class="uk-card-title">${ drink.name }</span>
								<span class="card-text">${ drink.ingredientList }</span>
							</span></a>
							<div class="uk-card-footer home-card-footer">
								<a href="/profile/${ drink.creator.id }" class="uk-text-small uk-text-muted uk-text-left card-link">Added by ${ drink.creator.username }</a>
							</div>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</body>
</html>