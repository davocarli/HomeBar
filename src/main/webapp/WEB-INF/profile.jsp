<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Profile: home-bar.app</title>
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
					<li><a href="/drinks/new">Add a Drink</a></li>
					<li class="uk-active"><c:choose><c:when test="${ user != null }"><a href="/profile">Profile</a></c:when><c:otherwise><a href="/login">Log In</a></c:otherwise></c:choose></li>
					<li><c:if test="${ user != null }"><a href="/logout">Log Out</a></c:if></li>
				</ul>
			</div>
		</div>
		<!-- CONTENT -->
		<div style="padding: 2% 5%; background-color: #F8F8F8">
			<div class="uk-card uk-card-default uk-grid-collapse uk-margin" uk-grid>
			    <div>
			        <div class="uk-card-footer">
						<h3>
			        	<c:if test="${ profile.id == user.id }"><a class="uk-text-small uk-card-text uk-card-link uk-text-muted" style="float: right;" href="/profile/edit">Edit Profile Visibility</a></c:if>
							<c:choose>
								<c:when test="${ profile.showName }">
									${ profile.firstName } ${ profile.lastName }'s Profile
								</c:when>
								<c:otherwise>
									${ profile.username }'s Profile
								</c:otherwise>
							</c:choose>
						</h3>
						<h4>ABOUT</h4>
						<c:choose>
							<c:when test="${ profile.bio.length() > 0 }">
								<p>${ profile.bio }</p>
							</c:when>
							<c:otherwise>
								<p class="uk-text-muted">This user has not added any about info...</p>
							</c:otherwise>
						</c:choose>
			        </div>
			        <div class="uk-card-footer">
			        	<c:if test="${ profile.showBar || profile.id == user.id }">
			        		<h3>This User's Bar<c:if test="${ !profile.showBar }"><span class="uk-text-small uk-text-muted"> (Only Visible to You)</span></c:if> <c:if test="${ profile.showBar }"><a class="uk-text-small uk-card-text uk-card-link uk-text-muted" style="float: right;" href="/?assumeduser=${ profile.username }">Browse Drinks as This User</a></c:if></h3>
			        		<ul class="uk-child-width-1-1 uk-child-width-1-3@s uk-child-width-1-4@m uk-child-width1-5@l uk-text-center" uk-grid="true">
			        			<c:forEach items="${ profile.ingredients }" var="ingredient">
			        				<c:if test="${ ingredient.status == 'stock' }">
			        					<li>${ ingredient.name }</li>
			        				</c:if>
			        			</c:forEach>
			        		</ul>
			        	</c:if>
			        </div>
			    </div>
			</div>
       		<h4>Drinks Added By This User</h4>
       		<ul class="uk-child-width-1-1 uk-child-width1-2@s uk-child-width1-3@m uk-child-width-1-4@l uk-child-width-1-5@xl uk-text-center home-list" uk-grid="masonry: true">
       			<c:forEach items="${ profile.recipes }" var="drink">
       				<li class="drink-card">
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
								</span>
       						</a>
       					</div>
       				</li>
       			</c:forEach>
       		</ul>
		</div>
	</body>
</html>