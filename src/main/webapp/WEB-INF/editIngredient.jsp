<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  <!DOCTYPE html>
<html lang="en">
	<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${ ingredient.name }: Edit</title>
    <link rel="stylesheet" type="text/css" href="/css/uikit.min.css">
    <script src="/js/uikit.min.js"></script>
    <script src="/js/uikit-icons.min.js"></script>
    <script src="/js/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/selectize.css">
    <link rel="stylesheet" type="text/css" href="/css/selectize.bootstrap3.css">
    <script src="/js/selectize.min.js"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	<script>
		$(function() {
			$('.selectize').selectize({
				plugins: ['remove_button'],
				delimiter: '|',
				create: true,
			});
		})
	</script>
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
				</ul>
			</div>
		</div>
		<!-- CONTENT -->
		<div style="padding: 50px">
			<h3>Edit ingredient</h3>
			<form:form method="POST" action="/ingredients/${ ingredient.id }/edit" modelAttribute="ingredient">
				<span class="uk-text-danger">${ formErrors }</span>
				<fieldset class="uk-fieldset">
					<div class="uk-inline uk-width-2-2">
						<form:input class="uk-input uk-form-width-large" path="name" placeholder="Ingredient Name"/>
						<form:input class="uk-input uk-form-width-large selectize" multiple="multiple" path="substituteNames" placeholder="This ingredient can also substitute for..."/>
						<input class="uk-button uk-inline uk-button-primary" type="submit" value="UPDATE"/>
						<!--TODO: DYNAMICALLY ADD SUGGESTED OPTIONS BASED ON OTHER INGREDIENTS IN DATABASE -->
					</div>
				</fieldset>
			</form:form>
		</div>
	</body>
</html>