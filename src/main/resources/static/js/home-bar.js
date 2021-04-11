// HELPER METHODS

var drinkCounter = 0;
var requestingDrinks = false;
var filterArrays = [];

function isElementInViewport (el) {

    // Special bonus for those using jQuery
    if (typeof jQuery === "function" && el instanceof jQuery) {
        el = el[0];
    }

    var rect = el.getBoundingClientRect();

    return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && /* or $(window).height() */
        rect.right <= (window.innerWidth || document.documentElement.clientWidth) /* or $(window).width() */
    );
}

$(function() {
	$('body').onSwipe(function(results) {
		if (results.right && !results.down && !results.up) {
			UIkit.offcanvas('#offcanvas-menu').show();
		}
	});
});

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

// UX Methods
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

	$('.drink-filter:not(.drink-filter-disable)').each(function() {
		filterArrays.push(splitIngredient($(this).attr('data-filter-text')));
	});

	$('.load-drinks').isInViewport(function(status) {
		if (status === 'entered') {
			loadDrinks();
		}
	})

	$('.drink-filter').click(function() {
		let group = $(this).attr('data-filter-group');
		$('.drink-filter.' + group).toggleClass('filter-visible');
	})

	$('#filter-element').on('afterFilter', function() {
		loadDrinks();
	})

	let fiveSecondLoadCheck = setInterval(function() {
		let spinner = $('.load-drinks');
		if (spinner.length) {
			if (isElementInViewport(spinner)) {
				getNextDrinks();
			}
		} else {
			clearInterval(fiveSecondLoadCheck);
		}
	}, 5000)
}

function loadDrinks() {
	let spinner = $('.load-drinks');
	if (spinner.length && isElementInViewport(spinner)) {
		getNextDrinks();
		let interval = setInterval(function() {
			let spinner = $('.load-drinks');
			if (spinner.length && isElementInViewport(spinner)) {
				getNextDrinks();
			} else {
				clearInterval(interval);
			}
		}, 1000)
	}
}

function replaceURLWithHTMLLinks(text) {
    var exp = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
    return text.replace(exp,"<a class=\"uk-link uk-link-muted\" href='$1'>$1</a>"); 
}

function initDrinkForm(ingredientOptions) {
	// Add new Ingredients on click
	const ingredientFields = 
`<div class="uk-width-1-1 uk-grid-small ingredient-list" style="padding-right: 0px;" uk-grid>
	<div class="uk-width-1-3@s">
		<select class="uk-input selectize-single selectize-init ingredient-name" type="text" placeholder="Preferred Ingredient">
			<option value=""></option>
		</select>
	</div>
	<div class="uk-width-2-5@s">
		<input class="uk-input selectize selectize-init substitute-names" multiple="multiple" placeholder="Acceptable substitutes..." value=""/>
	</div>
	<div class="uk-width-1-1 uk-width-1-4@s uk-grid-small" style="padding-right: 0px;" uk-grid>
		<div class="uk-width-1-2">
			<input class="uk-input amount" placeholder="Amt." value=""/>
		</div>
		<div style="vertical-align: middle; line-height: 2.25" class="uk-width-1-2" style="vertical-align: middle;">
			<label><input type="checkbox" class="uk-checkbox optional-field"/><span style="margin-left: 2px;" class="uk-hidden@m">Opt.</span> <span class="uk-visible@m">Optional</span></label>
		</div>
		<div class="uk-card uk-card-body uk-card-default" uk-drop="pos: left-center">
			If selected, users will not need to add this ingredient to their bar to see they are able to make this recipe. Recommended for garnishes and common ingredients (like water).
		</div>
	</div>
</div>`
	$('#addIngredient').click(function() {
		$(ingredientFields).insertBefore('#addIngredient');
		initSelectize(ingredientOptions, '.selectize-init');
		initSubstituteSuggestions('.selectize-init');
		$('.selectize-init').removeClass('selectize-init');
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
				name: $(this).find('.ingredient-name').val(),
				substitutes: $(this).find('.substitute-names').val(),
				amount: $(this).find('.amount').val(),
				optional: $(this).find('.optional-field').is(':checked').toString()
			})
		})
		var data = {
			name: $('#name').val(),
			ingredients: ingredients,
			instructions: $('#instructions').val(),
			source: $('#source').val()
		}
		console.log(data);
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

function initSelectize(ingredientOptions, extraClass='') {
	$(extraClass+'.selectize').selectize({
		plugins: ['remove_button'],
		delimiter: '|',
		create: true
	});
	if (ingredientOptions != undefined) {
		$(extraClass+'.selectize-single').selectize({
			create: true,
			plugins: ['restore_on_backspace'],
			sortField: 'value',
			options: ingredientOptions
		})
	} else {
		$(extraClass+'.selectize-single').selectize({
			create: true,
			plugins: ['restore_on_backspace'],
			sortField: 'value'
		})
	}
}

function initSubstituteSuggestions(extraClass='') {
	$(extraClass+'.ingredient-name').on('change', function() {
		const outerDiv = $(this).parents('.ingredient-list');
		const s = outerDiv.find('.substitute-names')[0].selectize;
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
				// const s = $('.substitute-names')[0].selectize
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
			if (li.attr('data-optional') == 'true') {
				li.find('span').attr('uk-icon', 'icon: minus; ratio: 0.8;');
				li.find('span').attr('style', 'height: 20px; width: 20px; text-align: center;');
			} else {
				li.find('span').attr('uk-icon', 'close');
			}
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

function toggleFavorite(id) {
	$('.drink-actions button.heart').attr('disabled', 'true');
	$.ajax('/favorites/toggle',
		{
			method: "GET",
			data: {id: id}
		})
	.done(function(result) {
		if (result) {
			$('.drink-actions button.heart').addClass('active');
		} else {
			$('.drink-actions button.heart').removeClass('active');
		}
	})
	.always(function() {
		$('.drink-actions button.heart').removeAttr('disabled');
	});
}

function fillStars(rating) {
	if (rating) {
		for (var i = 1; i <= 5; i++) {
			if (rating >= i) {
				$('button.star-'+i).addClass('active');
			} else {
				$('button.star-'+i).removeClass('active');
			}
		}
	}
}

function rateRecipe(id, rating) {
	$('button.star').attr('disabled', 'true');
	$.ajax('/drinks/' + id + '/rate', 
		{
			method: "GET",
			data: {rating: rating}
		})
	.done(function(data) {
		fillStars(data);
	})
	.always(function() {
		$('button.star').removeAttr('disabled');
	})
}

function getNextDrinks() {
	getDrinkCards(drinkCounter, drinkCounter+10);
}

function getDrinkCards(start, end) {
	if (!requestingDrinks) {
		$('.load-drinks').html('<div id="loading-indicator-spinner" uk-spinner></div> Finding some fancy drinks...')
		requestingDrinks = true;
		var urlParams = new URLSearchParams(window.location.search);
		data = {
			start: start,
			end: end
		}
		assumedUser=null;
		if (urlParams.has('assumeduser')) {
			assumedUser = urlParams.get(assumeduser);
		}
		$.ajax('/api/drinks', {
			method: "GET",
			data: {
				start: start,
				end: end
			}
		})
		.done(function(response) {
			if (response.status == 'success') {
				data = response.data;
				for (var i = 0; i < data.length; i++) {
					drink = data[i];
					card = createCard(drink, assumedUser);
					$('#drinks-list').append(card);
				}
				$('li.filter-refresh:not(.uk-active)').trigger('click');
				drinkCounter = response.end;
				if (drinkCounter >= response.totalResults) {
					endHomeLoading();
				}
			}
		}).always(function() {
			$('.load-drinks').html('<a class="uk-link-text" href="#!" onclick="getNextDrinks()">Load more drinks...</a>')
			requestingDrinks = false;
		})
	}
}

function endHomeLoading() {
	$('#loading-area').html('Oh no! We ran out of drinks!<br><a class="uk-link-text" href="/drinks/new">Submit a new one?</a>');
}

function fixHtml(html){
  var div = document.createElement('div');
  div.innerHTML=html
  return (div.innerHTML);
}

function createCard(drink, assumedUser=null, hidden=true) {
	let favorite = drink?.favorite;
	let favoriteActive = ''; 
	if (favorite) {
		favoriteActive = ' active';
	}

	let loggedIn = false;
	if (favorite != undefined) {
		loggedIn = true;
	}

	let ingredientFilters = drink.ingredientsFilters;
	let ingredientList = drink.ingredientsList;

	averageRating = null;

	if (drink.averageRating > 0) {
		let averageRating = Math.round(drink.averageRating * 10) / 10;
		if (averageRating.toString().length === 1 ) {
			averageRating = averageRating.toString() + ".0";
		}
	}

	let canMake = false;

	var filters = '';
	var presentIngredients = [];

	if (loggedIn) {
		let ingredients = ingredientFilters.toLowerCase().split('\n').filter(function(el) {return el.replaceAll('|', '').length != 0});;
		console.log(ingredients);

		for (var i = 0; i < filterArrays.length; i++) {
			innerLoop:
			for (var j = 0; j < ingredients.length; j++) {
				var fullIngredient = ingredients[j].split('|');
				if (anyCommon(fullIngredient, filterArrays[i])) {
					filters += filterArrays[i][0].toUpperCase() + 'S|';
					if (!presentIngredients.includes(j)) {
						presentIngredients.push(j);
					}
					break innerLoop;
				}
			}
		}
		if (presentIngredients.length == ingredients.length) {
			canMake = true;
		}
	}

	let element = '';
	let closing = '';
	// Begin drink card, with filter criteria
	element += '<li '

	if (hidden && loggedIn) {
		element += 'style="display: none"';
	}

	element += `class="drink-card" data-favorite="${favorite}" data-creator="${drink.creator}" data-ingredients="${filters}" data-can-make="${canMake}"><div class="uk-card uk-card-default">`;

	// Favorite heart
	if (loggedIn) {
		element += '<span class="heart home-heart-icon'
		if (favorite) {
			element += ' active';
		}
		element += '" uk-icon="heart"></span>'
	}

	// a tag to link to drink details page
	element += `<a class="uk-link-text link-card-body" href="/drinks/${drink.id}`;
	if (assumedUser != null) {
		element += `?assumeduser=${assumedUser}`
	}
	element += '">'

	// Card Image
	if (drink?.image != undefined) {
		element += '<div class="uk-card-media-top home-image-div">';
		element += `<img class="home-image" loading="lazy" src="https://s3-us-west-2.amazonaws.com/home-bar.app/recipeImages/500/${drink.image}" alt="cocktail" onerror="this.parentElement.style.display='none'">`;
		element += '</div>';
	}

	// Card body
	element += `<span class="uk-card-body"><h5 class="uk-card-title">${drink.name}</h5><span class="card-text">${ingredientList}</span></span>`;

	// close a tag
	element += '</a>';

	// Card footer
	element += `<div class="uk-card-footer home-card-footer"><div class="uk-align-left"><a href="/profile/${drink.creator}" class="uk-text-small uk-text-muted uk-text-left card-link">Added by ${drink.creator}</a></div><div class="uk-align-right">`
	if (averageRating) {
		element += `<span class="rating-number">${averageRating} </span><span class="star" uk-icon="star"></span>`
	}
	element += '</div></div>';

	// Add remaining closing tags
	element = fixHtml(element);

	return element;
}







