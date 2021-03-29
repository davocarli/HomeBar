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
    <script src="/js/swipe.js"></script>
    <script src="/js/home-bar.js"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
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
		<div style="padding: 2% 5%; background-color: #F8F8F8">
			<div class="uk-card uk-card-default uk-grid-collapse uk-margin" uk-grid>
			    <div>
			    	<form:form method="POST" action="/profile/edit" modelAttribute="user">
				        <div class="uk-card-footer">
							<h3>
								<c:choose>
									<c:when test="${ user.showName }">
										${ user.firstName } ${ user.lastName }'s Profile
									</c:when>
									<c:otherwise>
										${ user.username }'s Profile
									</c:otherwise>
								</c:choose>
							</h3>
							<div class="uk-grid uk-grid-small uk-child-width-1-3@m">
								<label><form:checkbox class="uk-checkbox" name="showName" path="showName"/> Show Full Name Instead of Username</label>
								<form:input class="uk-input" type="text" name="firstName" path="firstName" placeholder="First Name"/>
								<form:input class="uk-input" type="text" name="lastName" path="lastName" placeholder="Last Name"/>
							</div>
							<h4>ABOUT</h4>
							<form:textarea class="uk-textarea" placeholder="ABOUT INFO" rows="10" path="bio"></form:textarea>
				        </div>
				        <div class="uk-card-footer">
			        		<h3 style="display: inline-block;">
			        			This User's Bar
			        		</h3>
			        		<label style="display: inline-block;"><form:checkbox class="uk-checkbox" name="showBar" path="showBar"/> Show by Bar Contents Publicly</label>
			        		<ul class="uk-child-width-1-1 uk-child-width-1-3@s uk-child-width-1-4@m uk-child-width1-5@l uk-text-center" uk-grid="true">
			        			<c:forEach items="${ user.ingredients }" var="ingredient">
			        				<c:if test="${ ingredient.status == 'stock' }">
			        					<li>${ ingredient.name }</li>
			        				</c:if>
			        			</c:forEach>
			        		</ul>
			        		<input class="uk-button uk-button-primary" style="margin-top: 10px;" type="submit" value="UPDATE PROFILE"/>
				        </div>
			        </form:form>
			    </div>
			</div>
       		<h4>Drinks Added By This User</h4>
       		<ul class="uk-child-width-1-1 uk-child-width1-2@s uk-child-width1-3@m uk-child-width-1-4@l uk-child-width-1-5@xl uk-text-center home-list" uk-grid="masonry: true">
       			<c:forEach items="${ user.recipes }" var="drink">
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