$(function() {
	// Add new Ingredients on click
	$('#addIngredient').click(function() {
		$('#ingredients').append(
			'<fieldset class="uk-fieldset ingredient-list"><input class="uk-input uk-form-width-large ingredient" type="text" placeholder="Preferred Ingredient"/><input class="uk-input uk-form-width-large selectize-init" multiple="multiple" placeholder="Acceptable substitutes..."/><input class="uk-input uk-form-width-small amount" placeholder="Amount"/></fieldset>'
		);
		$('.selectize-init').selectize({
			create: true,
			delimiter: '|',
		});
		$('.selectize-init').addClass('selectize').removeClass('selectize-init');
	});
	// Add initial ingredient
	$('#addIngredient').click();

	// Handle Submission
	$('#submit').click(function() {
		var ingredients = [];
		$('.ingredient-list').each(function() {
			ingredients.push({
				name: $(this).find('.ingredient').val(),
				substitutes: $(this).find('.selectize').val(),
				amount: $(this).find('.amount').val()
			})
		})
		var data = {
			name: $('#name').val(),
			ingredients: ingredients,
			instructions: $('#instructions').val(),
			source: $('#source').val()
		}
		$.ajax('/drinks/new',
			{
				method: "POST",
				contentType: 'application/json',
				data: JSON.stringify(data)
			});
	})
});