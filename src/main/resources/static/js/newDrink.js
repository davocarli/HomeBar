// File Upload
async function uploadFile() {

	var file = fileupload.files[0];

	const recipeId = $('#upload-button').html();

	if (file) {
		console.log("FILE");
		const reader = new FileReader();

		reader.readAsDataURL(file);

		reader.onload = function(event) {
			const imgElement = document.createElement('img');
			imgElement.src = event.target.result;
			$('#input').attr('src', event.target.result);

			imgElement.onload = function(e) {
				const canvas = document.createElement("canvas");
				const MAX_WIDTH = 200;

				const scaleSize = MAX_WIDTH / e.target.width;
				canvas.width = MAX_WIDTH;
				canvas.height = e.target.height * scaleSize;

				const ctx = canvas.getContext("2d");

				ctx.drawImage(e.target, 0, 0, canvas.width, canvas.height);

				const srcEncoded = ctx.canvas.toDataURL(e.target, "image/jpeg");

				file = srcEncoded;
			}
		}
		let formData = new FormData();
		formData.append("file", file);
		let response = await fetch("/recipe/"+recipeId+"/upload", {
			method: "POST",
			body: formData
		})
	}

	window.location.replace('/drinks/'+recipeId);
}

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
			})
		.done(function(data) {
			$('#upload-button').html(data);
			$('#upload-button').trigger('click');
		});
	})
});