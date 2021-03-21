<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${ recipe.name }</title>
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
	<script src="/js/home-bar.js"></script>
	<c:if test="${ stockedIngredients != null }">
		<script>
			$(function() {
				detailReplacements("<c:forEach items="${stockedIngredients}" var="ingredient">${ingredient.getFullIngredient()}\n</c:forEach>");
				$('#source').html(replaceURLWithHTMLLinks($('#source').html()));
			})
		</script>
	</c:if>
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
						<li><c:choose><c:when test="${ assumedUser == null }"><a href="/">Make a Drink</a></c:when><c:otherwise><a href="/?assumeduser=${ assumedUser }">Make a Drink</a></c:otherwise></c:choose></li>
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
					<li><c:choose><c:when test="${ assumedUser == null }"><a href="/">Make a Drink</a></c:when><c:otherwise><a href="/?assumeduser=${ assumeduser }">Make a Drink</a></c:otherwise></c:choose></li>
					<li><a href="/drinks/new">Add a Drink</a></li>
					<li><c:choose><c:when test="${ user != null }"><a href="/profile">Profile</a></c:when><c:otherwise><a href="/login">Log In</a></c:otherwise></c:choose></li>
					<li><c:if test="${ user != null }"><a href="/logout">Log Out</a></c:if></li>
				</ul>
			</div>
		</div>
		<!-- CONTENT -->
		<div id="content" style="padding: 2% 5%;">
			<c:choose>
				<c:when test="${ recipe.image.length() > 0 }">	
					<div class="uk-card uk-card-default uk-grid-collapse uk-child-width-1-2@s uk-margin" uk-grid>
					    <div class="uk-flex-last@s uk-card-media-right uk-cover-container drink-detail-image">
					        <img class="uk-cover-container" uk-cover src="https://s3-us-west-2.amazonaws.com/home-bar.app/recipeImages/1000/${ recipe.image }" alt="cocktail" onerror="this.src = 'https://s3-us-west-2.amazonaws.com/home-bar.app/recipeImages/fullSize/${ recipe.image }'">
					    </div>
					    <div>
					        <div class="uk-card-body">
					            <h3 style="display: inline-block;" class="uk-card-title">${ recipe.name }</h3><c:if test="${ recipe.creator.id == user.id }"> <a href="/drinks/${ recipe.id }/edit" class="uk-link-text">Edit</a></c:if>
					            <h4>Ingredients</h4>
					            <ul id="drink-ingredients">
					            	<c:forEach items="${ recipe.ingredients }" var="ingredient">
					            		<li data-ingredient="${ ingredient.getFullIngredient() }"><span></span><div data-ingredient-id="${ ingredient.id }" class="uk-card uk-card-default uk-card-body ingredient-drop uk-hidden@m uk-hidden" uk-dropdown="">TESTING</div>${ ingredient.name }<c:if test="${ ingredient.amount.length() > 0 }"> (${ ingredient.amount })</c:if></li><div data-ingredient-id="${ ingredient.id }" class="uk-card uk-card-default uk-card-body ingredient-drop uk-visible@m uk-hidden" uk-dropdown="pos: right-center"></div>
					            	</c:forEach>
					            </ul>
					            <h4>Method</h4>
					            <p id="source" style="white-space: pre-line;">${ recipe.instructions }<c:if test="${ recipe.source.length() > 0 }"><br><br><b>Source:</b> ${ recipe.source }</c:if></p>
					        </div>
					    </div>
					</div>
				</c:when>
				<c:otherwise>
					<div style="padding: 20px;" class="uk-card uk-card-default uk-grid-collapse uk-child-width-1-2@s uk-margin" uk-grid>
			            <h3 style="display: inline-block;" class="uk-card-title">${ recipe.name }</h3><c:if test="${ recipe.creator.id == user.id }"> <a href="/drinks/${ recipe.id }/edit" class="uk-link-text">Edit</a></c:if>
			            <div>
			            	<h4>Ingredients</h4>
			            	<ul id="drink-ingredients">
			            		<c:forEach items="${ recipe.ingredients }" var="ingredient">
			            			<li data-ingredient="${ ingredient.getFullIngredient() }"><span></span><div data-ingredient-id="${ ingredient.id }" class="uk-card uk-card-default uk-card-body ingredient-drop uk-hidden@m uk-hidden" uk-dropdown="">TESTING</div>${ ingredient.name }<c:if test="${ ingredient.amount.length() > 0 }"> (${ ingredient.amount })</c:if></li><div data-ingredient-id="${ ingredient.id }" class="uk-card uk-card-default uk-card-body ingredient-drop uk-visible@m uk-hidden" uk-dropdown="pos: right-center"></div>
			            		</c:forEach>
			            	</ul>
			            </div>
			            <div>
			            	<h4>Method</h4>
			            	<p id="source" style="white-space: pre-line;">${ recipe.instructions }<c:if test="${ recipe.source.length() > 0 }"><br><br><b>Source:</b> ${ recipe.source }</c:if></p>
			            </div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>