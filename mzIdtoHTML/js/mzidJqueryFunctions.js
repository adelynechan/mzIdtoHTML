$( document ).ready(function() {
    $("h2").nextUntil("h2").slideToggle(); 
    $("h2").click(function() {$(this).nextUntil("h2").slideToggle();});
});

jQuery(function($) {
    // Grab whatever we need to paginate
    var pageParts = $(".peptidepaginate");

    // How many parts do we have?
    var numPages = pageParts.length;
    // How many parts do we want per page?
    var perPage = 20;

    // When the document loads we're on page 1
    // So to start with... hide everything else
    pageParts.slice(perPage).hide();
    // Apply simplePagination to our placeholder
    $("#peptide-nav").pagination({
        items: numPages,
        itemsOnPage: perPage,
        cssStyle: "light-theme",
        // We implement the actual pagination
        //   in this next function. It runs on
        //   the event that a user changes page
        onPageClick: function(pageNum) {
            // Which page parts do we show?
            var start = perPage * (pageNum - 1);
            var end = start + perPage;

            // First hide all page parts
            // Then show those just for our page
            pageParts.hide()
                     .slice(start, end).show();
        }
    });
});

jQuery(function($) {
    // Grab whatever we need to paginate
    var pagePartsProtein = $(".proteinpaginate");

    // How many parts do we have?
    var numPagesProtein = pagePartsProtein.length;
    // How many parts do we want per page?
    var perPageProtein = 20;

    // When the document loads we're on page 1
    // So to start with... hide everything else
    pagePartsProtein.slice(perPageProtein).hide();
    // Apply simplePagination to our placeholder
    $("#protein-nav").pagination({
        items: numPagesProtein,
        itemsOnPage: perPageProtein,
        cssStyle: "light-theme",
        // We implement the actual pagination
        //   in this next function. It runs on
        //   the event that a user changes page
        onPageClick: function(pageNumProtein) {
            // Which page parts do we show?
            var start = perPageProtein * (pageNumProtein - 1);
            var end = start + perPageProtein;

            // First hide all page parts
            // Then show those just for our page
            pagePartsProtein.hide()
                     .slice(start, end).show();
        }
    });
});


