<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>home-bar.app</title>
    <link rel="manifest" href="/manifest/manifest.json">
    <link rel="shortcut icon" type="image/png" href="/icons/icon_32.png"/>
    <link rel="stylesheet" type="text/css" href="/css/uikit.min.css">
    <script src="/js/uikit.min.js"></script>
    <script src="/js/uikit-icons.min.js"></script>
    <script src="/js/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/selectize.css">
    <link rel="stylesheet" type="text/css" href="/css/selectize.bootstrap3.css">
    <script src="/js/selectize.min.js"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	<script src="/js/swipe.js"></script>
	<script src="/js/home-bar.js"></script>
	<c:if test="${ stockedIngredients != null }">
	<script>
		$(function() {
			initDrinkFilters()
		});
	</script>
	</c:if>
	</head>
	<body>
		<!-- DESKTOP NAVBAR -->
		<nav class="uk-navbar-container uk-navbar-transparent uk-background-primary uk-light uk-visible@m" uk-navbar>
			<div class="uk-navbar-left">
				<c:if test="${ user != null }">
					<ul class="uk-navbar-nav">
						<li><a href="https://app.smartsheet.com/b/form/766d239d2b2f47ea922a50b19c74621c">Feedback</a></li>
						<li><c:if test="${ user != null }"><a href="/profile" class="uk-visible@m">Profile</a></c:if></li>
					</ul>
				</c:if>
			</div>
			<div class="uk-navbar-center">
				<div class="uk-navbar-center-left uk-visible@m">
					<ul class="uk-navbar-nav">
						<li><a href="/bar">My Bar</a></li>
						<li><a href="/shopping">Shopping List</a></li>
					</ul>
				</div>
					<img src="/icons/icon_80.png" class="uk-navbar-item uk-logo"/>
				<div class="uk-navbar-center-right uk-visible@m">
					<ul class="uk-navbar-nav">
						<li><c:choose><c:when test="${ assumedUser == null }"><a href="/">Make a Drink</a></c:when><c:otherwise><a href="/?assumeduser=${ assumeduser }">Make a Drink</a></c:otherwise></c:choose></li>
						<li><a href="/drinks/new">Add a Drink</a></li>
					</ul>
				</div>
			</div>
			<div class="uk-navbar-right">
				<ul class="uk-navbar-nav">
					<li><c:choose><c:when test="${ user == null }"><a href="/login">LOG IN</a></c:when><c:otherwise><a href="/logout">LOG OUT</a></c:otherwise></c:choose></li>
				</ul>
			</div>
		</nav>
		<!-- MOBILE NAVBAR -->
		<nav class="uk-navbar-container uk-navbar-transparent uk-background-primary uk-light uk-hidden@m" uk-navbar>
			<div class="uk-navbar-left">
				<ul class="uk-navbar-nav">
					<li><a href="#offcanvas-menu" class="uk-navbar-toggle" uk-navbar-toggle-icon uk-toggle></a></li>
				</ul>
			</div>
			<div class="uk-navbar-center">
				<img src="/icons/icon_80.png" class="uk-navbar-item uk-logo"/>
			</div>
			<div class="uk-navbar-right">
				<ul class="uk-navbar-nav">
					<li><c:if test="${ user != null }"><a href="/profile" uk-icon="user"></a></c:if><c:if test="${ user == null }"><a href="/login">LOG IN</a></c:if>
				</ul>
			</div>
		</nav>
		<!-- MOBILE OFFCANVAS -->
		<div id="offcanvas-menu" uk-offcanvas="overlay: true;">
			<div class="uk-offcanvas-bar uk-light">
				<div class="uk-background-primary uk-offcanvas-top">
					<ul class="uk-nav">
						<li><a href="https://app.smartsheet.com/b/form/766d239d2b2f47ea922a50b19c74621c">Submit Feedback</a></li>
						<li><br><br><br></li>
					</ul>
				</div>
				<h3><a href="#offcanvas-menu" class="uk-offcanvas-close" uk-close></a></h3>
				<ul class="uk-nav uk-nav-primary">
					<li><a href="/bar">My Bar</a></li>
					<li><a href="/shopping">Shopping List</a></li>
					<li><c:choose><c:when test="${ assumedUser.length() > 0 }"><a href="/">Make a Drink</a></c:when><c:otherwise><a href="/?assumeduser=${ assumedUser }">Make a Drink</a></c:otherwise></c:choose></li>
					<li><a href="/drinks/new">Add a Drink</a></li>
					<li><c:if test="${ user != null }"><a href="/logout">Log Out</a></c:if></li>
				</ul>
			</div>
		</div>
		<!-- CONTENT -->
		<div id="main-content" style="background-color: #FFF">
			<h3 style="margin-left: 20px; margin-bottom: 5px;"><c:if test="${ user != null }">Hi ${ user.firstName }! </c:if>Let's Make a drink!<c:if test="${ assumedUser != null }"> <a href="/profile/${ assumedUser }"><span class="uk-text-small uk-text-muted">Browsing as ${ assumedUser }</span></a></c:if></h3>
			<c:if test="${ stockedIngredients != null }">
				<div uk-filter="target: .js-filter" style="background-color: #FFF; padding-bottom: 0px;">
					<div uk-sticky style="padding: 0px 20px; background-color: #FFF">
						<div class="uk-grid-small uk-child-width-auto" style="margin-bottom: 5px; margin-top: 5px; background-color: #fff;" uk-grid>
							<!-- <span uk-icon="icon: chevron-left; ratio: 1.6" style="padding: 0px;"></span> -->
							<div class="filter-menu uk-overflow-auto uk-width-expand" style="background-color: #FFF">
								<ul class="uk-subnav uk-subnav-pill filter-menu" uk-margin>
									<li class="filter-menu filter-visible" id="filter-reset" uk-filter-control="group: make"><a href="#">All Drinks</a></li>
									<li class="filter-menu filter-visible" uk-filter-control="filter: [data-can-make='true']; group: make"><a href="#">Drinks I Can Make</a></li>
									<li class="filter-menu filter-visible" uk-filter-control="filter: [data-creator='${ user.username }']; group: make"><a href="#">My Drinks</a></li>
									<li class="filter-menu filter-visible" uk-filter-control="filter: [data-favorite='true']; group: make"><a href="#">Favorites</a></li>
									<li class="filter-divider">|</li>
									<c:forEach items="${ stockedIngredients }" var="ingredient">
										<li class="filter-menu filter-visible drink-filter ${ ingredient.id }" data-filter-text="${ fn:replace(ingredient.getFullIngredient().toUpperCase(), '\'', '') }" data-filter-group="${ ingredient.id }" uk-filter-control="filter: [data-ingredients*='${ fn:replace(ingredient.name.toUpperCase(), '\'', '') }']; group: ${ ingredient.id }"><a href="#">${ ingredient.name }</a></li>
										<li class="filter-menu drink-filter drink-filter-disable ${ ingredient.id }" uk-filter-control="group: ${ ingredient.id }" data-filter-group="${ ingredient.id }"><a href="#">${ ingredient.name }</a></li>
									</c:forEach>
								</ul>
							</div>
							<a id="filter-dropdown-button" class="no-highlights" href="javascript:void(0);" onClick="dropdownFilters()" uk-icon="icon: chevron-down; ratio: 1.6" style="padding: 0px; margin-right: -20px;"></a>
						</div>
					</div>
			</c:if>
			<ul class="js-filter uk-child-width-1-1 uk-child-width-1-2@s uk-child-width-1-3@m uk-child-width-1-4@l uk-child-width-1-5@xl uk-text-center home-list" uk-grid="masonry: true">
				<c:forEach items="${ drinks }" var="drink">
					<c:set value="${ drink.isFavoritedBy(user.id) }" var="favorited"/>
					<li class="drink-card" <c:if test="${ user != null }">data-favorite="${ favorited }"</c:if> data-creator="${ drink.creator.username }" data-ingredients="|${ fn:replace(drink.getAllFullIngredients().toUpperCase(), '\'', '') }|">
						<div class="uk-card uk-card-default">
							<c:if test="${ user != null }"><span style="z-index: 1000; position: absolute; top: 10px; right: 10px;" class="heart <c:if test="${ favorited }">active</c:if>" uk-icon="heart"></span></c:if>
							<c:choose><c:when test="${ assumedUser == null }">
							<a class="uk-link-text link-card-body" href="/drinks/${ drink.id }"></c:when><c:otherwise><a class="uk-link-text link-card-body" href="/drinks/${ drink.id }?assumeduser=${ assumedUser }"></c:otherwise></c:choose>
								<c:if test="${ drink.image.length() > 0 }">
									<div class="uk-card-media-top home-image-div">
										<img class="home-image" loading="lazy" src="https://s3-us-west-2.amazonaws.com/home-bar.app/recipeImages/500/${ drink.image }" alt="cocktail" onerror="this.parentElement.style.display='none'">
									</div>
								</c:if>
								<span class="uk-card-body">
									<h5 class="uk-card-title">${ drink.name }</h5>
									<span class="card-text">${ drink.ingredientList }</span>
								</span>
							</a>
							<div class="uk-card-footer home-card-footer">
								<div class="uk-align-left">
									<a href="/profile/${ drink.creator.username }" class="uk-text-small uk-text-muted uk-text-left card-link">Added by ${ drink.creator.username }</a>
								</div>
								<div class="uk-align-right">
									<c:if test="${ drink.averageRating > 0 }"><fmt:formatNumber var="formattedRating" type="number" minFractionDigits="1" maxFractionDigits="1" value="${ drink.averageRating }"/><span class="rating-number">${ formattedRating } </span><span class="star" uk-icon="star"></span></c:if>
								</div>
							</div>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</body>
</html>