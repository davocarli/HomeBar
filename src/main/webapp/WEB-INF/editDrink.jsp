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
	<script src="/js/newDrink.js"></script>
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
						<li><a href="/">Make a Drink</a></li>
						<li class="uk-active"><a href="/drinks/new">Add a Drink</a></li>
					</ul>
				</div>
			</div>
			<div class="uk-navbar-right">
				<a href="/profile" class="uk-button uk-button-default">Profile</a>
			</div>
		</nav>
		<!-- CONTENT -->
		<div style="padding: 50px;">
			<h3 style="display: inline-block;">Edit Recipe</h3>
			<button id="upload-button" onclick="uploadFile()">${ recipe.id }</button>
			<form:form modelAttribute="recipe">
				<fieldset class="uk-fieldset">
					<form:input class="uk-input uk-form-width-large" type="text" path="name" id="name" placeholder="Recipe Name"/>
					<div uk-form-custom="target: true">
						<input type="file" id="fileupload" name="fileupload" accept="image/*">
						<input class="uk-input uk-form-width-large" type="text" placeholder="Replace/Upload Image" disabled>
					</div>
				</fieldset>
				<div id="ingredients">
				<c:forEach items="${ recipe.ingredients }" var="ingredient">
					<fieldset class="uk-fieldset ingredient-list"><input class="uk-input uk-form-width-large ingredient" type="text" placeholder="Preferred Ingredient" value="${ ingredient.name }"/><input class="uk-input uk-form-width-large selectize-init" multiple="multiple" placeholder="Acceptable substitutes..." value="${ ingredient.substituteNames }"/><input class="uk-input uk-form-width-small amount" placeholder="Amount" value="${ ingredient.amount }"/></fieldset>
				</c:forEach>
				</div>
				<a id="addIngredient">Add Ingredient</a>
				<fieldset class="uk-fieldset">
					<form:textarea class="uk-textarea uk-form-width-large" id="instructions" path="instructions" placeholder="Drink Instructions..." rows="10" style="width: 1134px;"></form:textarea>
					<form:input class="uk-input" type="text" id="source" path="source" placeholder="Source (Give credit if this is not your own recipe)." style="width: 1134px; margin-top: 10px;"/>
				</fieldset>
			</form:form>
			<button class="uk-button uk-button-primary" id="submit">SUBMIT</button> <a class="uk-button uk-button-danger" href="/drinks/${ recipe.id }/delete">DELETE DRINK</a>
		</div>
	</body>
</html>