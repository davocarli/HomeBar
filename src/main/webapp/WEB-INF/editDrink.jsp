<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('application.version')" var="appversion" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <meta name="theme-color" content="#1e87f0">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${ recipe.name }: Edit</title>
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
	<script src="/js/home-bar.js?${ appversion }"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css?${ appversion }">
	<script>
		$(function() {
			const allIngredients = [<c:forEach items="${ ingredientOptions }" var="ingredient">{text: "${ ingredient }", value: "${ ingredient }"},</c:forEach>];
			initSelectize(allIngredients);
			initDrinkForm(allIngredients);
			initSubstituteSuggestions();
		})
	</script>
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
		<div style="padding: 2% 5%;">
			<h3 style="display: inline-block;">Edit Recipe</h3>
			<button id="upload-button" onclick="uploadFile()">${ recipe.id }</button>
			<form:form class="uk-grid-small" modelAttribute="recipe" uk-grid="true">
				<div class="uk-width-5-6@s">
					<form:input class="uk-input" type="text" path="name" id="name" placeholder="Recipe Name"/>
				</div>
				<div class="uk-width-1-6@s">
						<div uk-form-custom="target: true">
							<input type="file" id="fileupload" name="fileupload" accept="image/*">
							<input class="uk-input" type="text" placeholder="Replace/Upload Image" disabled>
						</div>
				</div>
				<div class="uk-width-1-1 uk-grid-small ingredient-list" style="padding-right: 0px;" uk-grid>
					<div class="uk-width-5-6">
						<h4>Ingredients</h4>
					</div>
				</div>
				<c:forEach items="${ recipe.ingredients }" var="ingredient">
					<div class="uk-width-1-1 uk-grid-small ingredient-list" style="padding-right: 0px;" uk-grid>
						<div class="uk-width-1-3@s">
							<select class="uk-input selectize-single ingredient-name" type="text" placeholder="Preferred Ingredient">
								<option value="${ ingredient.name }">${ ingredient.name }</option>
							</select>
						</div>
						<div class="uk-width-2-5@s">
							<input class="uk-input selectize substitute-names" multiple="multiple" placeholder="Acceptable substitutes..." value="${ ingredient.substituteNames }"/>
						</div>
						<div class="uk-width-1-1 uk-width-1-4@s uk-grid-small" style="padding-right: 0px;" uk-grid>
							<div class="uk-width-1-2">
								<input class="uk-input amount" placeholder="Amt." value="${ ingredient.amount }"/>
							</div>
							<div style="vertical-align: middle; line-height: 2.25" class="uk-width-1-2" style="vertical-align: middle;">
								<label><input type="checkbox" class="uk-checkbox optional-field" <c:if test="${ ingredient.optional }">checked</c:if>/><span style="margin-left: 2px;" class="uk-hidden@m">Opt.</span> <span class="uk-visible@m">Optional</span></label>
							</div>
							<div class="uk-card uk-card-body uk-card-default" uk-drop="pos: left-center">
								If selected, users will not need to add this ingredient to their bar to see they are able to make this recipe. Recommended for garnishes and common ingredients (like water).
							</div>
						</div>
					</div>
				</c:forEach>
				<a id="addIngredient">Add Ingredient</a>
				<div class="uk-width-1-1">
					<form:textarea class="uk-textarea" id="instructions" path="instructions" placeholder="Drink Instructions..." rows="10"></form:textarea>
				</div>
				<div class="uk-width-1-1-">
					<form:input class="uk-input" type="text" id="source" path="source" placeholder="Source (Give credit if this is not your original recipe)."/>
				</div>
			</form:form>
			<button class="uk-button uk-button-primary" id="submit">SUBMIT</button>
		</div>
	</body>
</html>