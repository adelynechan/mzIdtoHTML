$( document ).ready(function() {
    $("h2").nextUntil("h2").slideToggle();
    $("h2").click(function() {$(this).nextUntil("h2").slideToggle();});
 
});