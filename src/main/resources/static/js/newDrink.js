// File Upload
async function uploadFile() {

	const file = document.querySelector('#fileupload').files[0];

	const recipeId = $('#upload-button').html();
	
	if (file) {
	
		let formData = new FormData();
		formData.append("file", file);
	
		let response = await fetch("/recipe/" + recipeId + "/upload", {
			method: "POST",
			body: formData
		})
	}

	window.location.replace('/drinks/' + recipeId);
}

$(function() {
	// Add new Ingredients on click
	const ingredientFields = '<div class="uk-width-1-3@s"><input class="uk-input ingredient" type="text" placeholder="Preferred Ingredient"/></div><div class="uk-width-1-2@s"><input class="uk-input selectize-init" multiple="multiple" placeholder="Acceptable substitutes..."/></div><div class="uk-width-1-6@s"><input class="uk-input amount" placeholder="Amount"/></div>'
	$('#addIngredient').click(function() {
		$(ingredientFields).insertBefore('#addIngredient');
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
		$(this).attr('disabled', 'true');
		$(this).html('PLEASE WAIT...');
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
		var url = null;
		var uploadStatus = $('#upload-button').html();
		if (uploadStatus == 'UPLOAD') {
			url = '/drinks/new';
		} else {
			url = '/drinks/' + uploadStatus + '/edit';
		}
		$.ajax(url,
			{
				method: "POST",
				contentType: 'application/json',
				data: JSON.stringify(data)
			})
		.done(function(data) {
			$('#upload-button').html(data);
			$('#upload-button').trigger('click');
		});
	})
});