<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('application.version')" var="appversion" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <meta name="theme-color" content="#1e87f0">
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
	<link rel="stylesheet" type="text/css" href="/css/style.css?${ appversion }">
	<script src="/js/swipe.js"></script>
	<script src="/js/home-bar.js?${ appversion }"></script>
	<c:if test="${ stockedIngredients != null }">
		<script>
			$(function() {
				detailReplacements("<c:forEach items="${stockedIngredients}" var="ingredient">${ingredient.getFullIngredient()}\n</c:forEach>");
				$('#source').html(replaceURLWithHTMLLinks($('#source').html()));
				fillStars(${ recipe.getRatingOfUser(user.id).getRatingValue() })
			})
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
					<li><c:choose><c:when test="${ assumedUser == null }"><a href="/">Make a Drink</a></c:when><c:otherwise><a href="/?assumeduser=${ assumedUser }">Make a Drink</a></c:otherwise></c:choose></li>
					<li><a href="/drinks/new">Add a Drink</a></li>
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
					            <div class="drink-actions">
					            	<span id="rating" class="text-muted"><c:if test="${ recipe.averageRating != null }"><fmt:formatNumber var="formattedRating" type="number" minFractionDigits="1" maxFractionDigits="1" value="${ recipe.averageRating }"/><span class="rating-number">${ formattedRating }</span></c:if><span class="star" uk-icon="star"></span><c:if test="${ user != null }"><div data-ingredient-id="${ ingredient.id }" class="star-rating-drop" uk-drop="pos: center-left; offset: -40; delay-hide: 1;"><span><button class="star star-1" onClick="rateRecipe(${ recipe.id }, 1)" uk-icon="star"></button><span><button class="star star-2" onClick="rateRecipe(${ recipe.id }, 2)" uk-icon="star"></button><span><button class="star star-3" onClick="rateRecipe(${ recipe.id }, 3)" uk-icon="star"></button><span><button class="star star-4" onClick="rateRecipe(${ recipe.id }, 4)" uk-icon="star"></button><span><button class="star star-5" onClick="rateRecipe(${ recipe.id }, 5)" uk-icon="star"></button></span></span></span></span></span></div></c:if></span>
						            <c:if test="${ user != null }">
							            <button class="heart <c:if test="${ recipe.getFavoritedUserIds().contains(user.id) }">active</c:if>" onClick="toggleFavorite(${ recipe.id })" uk-icon="icon: heart"></button>
						            </c:if>
					            </div>
					            <h4>Ingredients</h4>
					            <ul id="drink-ingredients">
					            	<c:forEach items="${ recipe.ingredients }" var="ingredient">
					            		<li data-ingredient="${ ingredient.getFullIngredient(true) }" data-optional="${ ingredient.optional }"><span></span><div data-ingredient-id="${ ingredient.id }" class="uk-card uk-card-default uk-card-body ingredient-drop uk-hidden@m uk-hidden" uk-dropdown="">LOADING</div>${ ingredient.name }<c:if test="${ ingredient.optional }"> (Optional)</c:if><c:if test="${ ingredient.amount.length() > 0 }"> (${ ingredient.amount })</c:if></li><div data-ingredient-id="${ ingredient.id }" class="uk-card uk-card-default uk-card-body ingredient-drop uk-visible@m uk-hidden" uk-dropdown="pos: right-center"></div>
					            	</c:forEach>
					            </ul>
					            <h4>Method</h4>
					            <p id="source" style="white-space: pre-line;">${ recipe.instructions }<c:if test="${ recipe.source.length() > 0 }"><br><br><b>Source:</b> ${ recipe.source }</c:if></p>
					        </div>
					    </div>
					</div>
				</c:when>
				<c:otherwise>
					<div style="padding: 20px;" class="uk-card uk-card-default">
						<div class="drink-actions">
			            	<span id="rating" class="text-muted"><c:if test="${ recipe.averageRating != null }"><fmt:formatNumber var="formattedRating" type="number" minFractionDigits="1" maxFractionDigits="1" value="${ recipe.averageRating }"/><span class="rating-number">${ formattedRating }</span></c:if><span class="star" uk-icon="star"></span><c:if test="${ user != null }"><div data-ingredient-id="${ ingredient.id }" class="star-rating-drop" uk-drop="pos: center-left; offset: -40; delay-hide: 1;"><span><button class="star star-1" onClick="rateRecipe(${ recipe.id }, 1)" uk-icon="star"></button><span><button class="star star-2" onClick="rateRecipe(${ recipe.id }, 2)" uk-icon="star"></button><span><button class="star star-3" onClick="rateRecipe(${ recipe.id }, 3)" uk-icon="star"></button><span><button class="star star-4" onClick="rateRecipe(${ recipe.id }, 4)" uk-icon="star"></button><span><button class="star star-5" onClick="rateRecipe(${ recipe.id }, 5)" uk-icon="star"></button></span></span></span></span></span></div></c:if></span>
				            <c:if test="${ user != null }">
					            <button class="heart <c:if test="${ recipe.getFavoritedUserIds().contains(user.id) }">active</c:if>" onClick="toggleFavorite(${ recipe.id })" uk-icon="icon: heart"></button>
				            </c:if>
			            </div>
						<div class="uk-grid-collapse uk-child-width-1-2@s uk-margin" uk-grid>
				            <h3 style="display: inline-block;" class="uk-card-title">${ recipe.name }<c:if test="${ recipe.creator.id == user.id }"> <a href="/drinks/${ recipe.id }/edit" class="uk-link-text uk-text-muted uk-text-small">Edit</a></c:if></h3>
				            <div>
				            	<h4>Ingredients</h4>
				            	<ul id="drink-ingredients">
				            		<c:forEach items="${ recipe.ingredients }" var="ingredient">
				            			<li data-ingredient="${ ingredient.getFullIngredient(true) }" data-optional="${ ingredient.optional }"><span></span><div data-ingredient-id="${ ingredient.id }" class="uk-card uk-card-default uk-card-body ingredient-drop uk-hidden@m uk-hidden" uk-dropdown="">LOADING</div>${ ingredient.name }<c:if test="${ ingredient.optional }"> (Optional)</c:if><c:if test="${ ingredient.amount.length() > 0 }"> (${ ingredient.amount })</c:if></li><div data-ingredient-id="${ ingredient.id }" class="uk-card uk-card-default uk-card-body ingredient-drop uk-visible@m uk-hidden" uk-dropdown="pos: right-center"></div>
				            		</c:forEach>
				            	</ul>
				            </div>
				            <div>
				            	<h4>Method</h4>
				            	<p id="source" style="white-space: pre-line;">${ recipe.instructions }<c:if test="${ recipe.source.length() > 0 }"><br><br><b>Source:</b> ${ recipe.source }</c:if></p>
				            </div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>