function anyCommon(arr1, arr2) {
	return arr1.some(item => arr2.includes(item));
}

$(function() {

	var filterArrays = [];

	$('.drink-filter:not(.drink-filter-disable)').each(function() {
		filterArrays.push($(this).attr('data-filter-text').split('|').filter(function(el) {return el.length != 0}));
	});


	$('.drink-card').each(function() {
		var card = $(this);
		var ingredients = card.attr('data-ingredients').split('\n').filter(function(el) {return el.replaceAll('|', '').length != 0});
		console.log(ingredients);

		var filters = '';

		var presentIngredients = [];

		for (var i = 0; i < filterArrays.length; i++) {
			innerLoop:
			for (var j = 0; j < ingredients.length; j++) {
				var fullIngredient = ingredients[j].split('|');

				if (anyCommon(fullIngredient, filterArrays[i])) {
					filters += filterArrays[i][0] + '|';
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
		card.attr('data-ingredients', filters);
	});

	$('.drink-filter').click(function() {
		let group = $(this).attr('data-filter-group');
		$('.drink-filter.' + group).toggleClass('filter-visible');
	})
});