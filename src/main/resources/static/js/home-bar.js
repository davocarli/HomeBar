function anyCommon(arr1, arr2) {
	for (var i = 0; i < arr1.length && i < arr2.length; i++) {
		s1 = arr1[i];
		if (s1 != null && s1[s1.length-1] == 's') {
			arr1[i] = s1.slice(0, -1);
		}
		s2 = arr2[i];
		if (s2 != null && s2[s2.length-1] == 's') {
			arr2[i] = s2.slice(0, -1);
		}
	}
	return arr1.some(item => arr2.includes(item));
}

function getCommon(arr1, arr2) {
	var finalArr = [];

	for (var i = 0; i < arr2.length; i++) {
		if (anyCommon(arr1, arr2[i])) {
			finalArr.push(arr2[i][0]);
		}
	}
	return finalArr;
}

// Provide a string for ingredient(s) and processes. If a list of \n separated ingredients
// is provided, will return an array of arrays. If a single ingredient is provided, will just
// return a single array.
function splitIngredient(ingredient) {
	var arr = ingredient.split('\n');
	if (arr.length == 1) {
		return ingredient.toLowerCase().split('|').filter(function(el) {return el.split('|').join().length != 0});
	} else {
		var finalArr = [];
		for (var i = 0; i < arr.length; i++) {
			finalArr.push(splitIngredient(arr[i]));
		}
		return finalArr;
	}
}

function dropdownFilters() {
	$('div.filter-menu').height($('ul.filter-menu').height()+6);
	$('ul.filter-menu').toggleClass('expand');
	$('#filter-dropdown-button').toggleClass('expand');
	$('div.filter-menu').animate({'height': $('ul.filter-menu').height()+6}, 150,
		function() {
			$('div.filter-menu').css('height', 'auto');
			$('.uk-sticky-placeholder').height($('div.filter-menu').height());
		})
}

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

function initDrinkFilters() {

	var filterArrays = [];

	$('.drink-filter:not(.drink-filter-disable)').each(function() {
		filterArrays.push(splitIngredient($(this).attr('data-filter-text')));
	});

	$('.drink-card').each(function() {
		var card = $(this);
		var ingredients = card.attr('data-ingredients').toLowerCase().split('\n').filter(function(el) {return el.replaceAll('|', '').length != 0});

		var filters = '';

		var presentIngredients = [];

		for (var i = 0; i < filterArrays.length; i++) {
			innerLoop:
			for (var j = 0; j < ingredients.length; j++) {
				var fullIngredient = ingredients[j].split('|');

				if (anyCommon(fullIngredient, filterArrays[i])) {
					filters += filterArrays[i][0] + 'S|';
					if (!presentIngredients.includes(j)) {
						presentIngredients.push(j);
					}
					break innerLoop;
				}
			}
		}
		if (presentIngredients.length == ingredients.length) {
			card.attr('data-can-make', 'true');
		};
		card.attr('data-ingredients', filters.toUpperCase());
	});

	$('.drink-filter').click(function() {
		let group = $(this).attr('data-filter-group');
		$('.drink-filter.' + group).toggleClass('filter-visible');
	})
}

function initDrinkForm() {
	// Add new Ingredients on click
	const ingredientFields = '<div class="uk-width-1-1 uk-grid-small ingredient-list" style="padding-right: 0px;" uk-grid><div class="uk-width-1-3@s"><input class="uk-input ingredient" type="text" placeholder="Preferred Ingredient"/></div><div class="uk-width-1-2@s"><input class="uk-input selectize-init" multiple="multiple" placeholder="Acceptable substitutes..."/></div><div class="uk-width-1-6@s"><input class="uk-input amount" placeholder="Amount"/></div></div>'
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
}

function initSelectize() {
	$('.selectize').selectize({
		plugins: ['remove_button'],
		delimiter: '|',
		create: true
	});
	$('.selectize-single').selectize({
		create: true,
		plugins: ['restore_on_backspace'],
		sortField: 'value'
	});
}

function initSubstituteSuggestions() {
	$('.ingredient-name').on('change', function() {
		const s = $('.substitute-names')[0].selectize;
		const selections = s.items;
		const options = Object.keys(s.options);
		for (var i = 0; i < options.length; i++) {
			if (!selections.includes(options[i])) {
				s.removeOption(options[i]);
			}
		}
		const ingredientName = $(this).val();
		if (ingredientName.length > 0) {
			$.ajax('/suggestions/substitutes',
			{
				method: "GET",
				data: {ingredient: ingredientName}
			})
			.done(function(data) {
				const s = $('.substitute-names')[0].selectize
				for (var i = 0; i < data.length; i++) {
					if (data[i] != ingredientName) {
						s.addOption({value: data[i], text: data[i]});
					}
				}
			})
		}
	});
	$('.ingredient-name').trigger('change');
}

function detailReplacements(fullStock) {
	const stockedIngredients = splitIngredient(fullStock);

	$('#drink-ingredients').addClass('uk-iconnav uk-iconnav-vertical drink-ingredients');

	$('#drink-ingredients li').each(function() {
		var li = $(this);
		var bigDrop = li.next();
		var smallDrop = li.find('div');
		var ingredient = splitIngredient(li.attr('data-ingredient'));
		var commonIngredients = getCommon(ingredient, stockedIngredients);

		if (commonIngredients.length === 0) {
			bigDrop.html('<a class="uk-link uk-text-warning" href="#" onclick="cloneIngredient(' + bigDrop.attr('data-ingredient-id') + ')">Add to shopping list</a>')
			smallDrop.html('<a class="uk-link uk-text-warning" href="#" onclick="cloneIngredient(' + bigDrop.attr('data-ingredient-id') + ')">Add to shopping list</a>')
			li.find('span').attr('uk-icon', 'close');
			// li.prepend('<span uk-icon="close"></span>')
		} else if (commonIngredients.length === 1) {
			bigDrop.html('Use ' + commonIngredients[0]);
			smallDrop.html('Use ' + commonIngredients[0]);
			li.find('span').attr('uk-icon', 'check');
			// li.prepend('<span uk-icon"check"></span>')
		} else {
			bigDrop.html('Use one of:<br>- ' + commonIngredients.join('<br>- '));
			smallDrop.html('Use one of:<br>- ' + commonIngredients.join('<br>- '));
			li.find('span').attr('uk-icon', 'check');
			// li.prepend('<span uk-icon="check"></span>')
		}

		bigDrop.removeClass('uk-hidden');
		smallDrop.removeClass('uk-hidden');
	});
}

function cloneIngredient(id) {
	$('div[data-ingredient-id="' + id + '"]').html('<span class="uk-text-warning">Adding...</span>');
	$.ajax('/shopping/clone',
		{
			method: "POST",
			contentType: "application/json",
			data: JSON.stringify({id: id})
		})
	.done(function() {
		$('div[data-ingredient-id="' + id + '"]').html('Added to shopping list');
	})
	.fail(function() {
		$('div[data-ingredient-id="' + id + '"]').html('<a class="uk-link uk-text-danger" href="#" onclick="cloneIngredient(' + id + ')">Error. Click to try again.</a>')
	})
}












