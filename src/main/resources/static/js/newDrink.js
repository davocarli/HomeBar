// // Compress Image File
// function compressImage(file, width) {
// 	const reader = new FileReader();

// 	reader.readAsDataURL(file);

// 	reader.onload = function(event) {
// 		const imgElement = document.createElement('img');
// 		imgElement.src = event.target.result;

// 		imgElement.onload = function(e) {
// 			const canvas = document.createElement("canvas");

// 			const scaleSize = width / e.target.width;
// 			canvas.width = width;
// 			canvas.height = e.target.height * scaleSize;

// 			const ctx = canvas.getContext("2d");

// 			ctx.drawImage(e.target, 0, 0, canvas.width, canvas.height);

// 			return canvas;
// 		}
// 	}
// }

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

// function uploadFile() {
// 	alert('Starting method');
// 	const file = document.querySelector('#fileupload').files[0];

// 	const recipeId = $('#upload-button').html();

// 	const WIDTH = 200;

// 	if (file) {
// 		alert('There is a file');
// 		const reader = new FileReader();
// 		reader.readAsDataURL(file);

// 		reader.onload = function(event) {
// 			alert('Event one');
// 			const imgElement = document.createElement("img");
// 			imgElement.src = event.target.result;

// 			imgElement.onload = function(e) {
// 				alert('Event 2');
// 				const canvas = document.createElement("canvas");
// 				const scaleSize = WIDTH / e.target.width;
// 				canvas.width = WIDTH;
// 				canvas.height = e.target.height * scaleSize;

// 				const ctx = canvas.getContext("2d");

// 				ctx.drawImage(e.target, 0, 0, canvas.width, canvas.height);

// 				canvas.toBlob((blob) => {
// 					alert('blob & post');
// 					let formData = new FormData();
// 					formData.append('file', blob, 'file.png');

// 					$.ajax('/recipe/'+recipeId+'/upload', {
// 						method: "POST",
// 						contentType: 'multipart/form-data; boundary=------WebKitFormBoundary7MA4YWxkTrZu0gW',
// 						processData: false,
// 						body: formData
// 					}).done((data) => {
// 						alert(data);
// 					})
// 				})
// 			}
// 		}
// 	}
// }

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