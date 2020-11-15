$( document ).ready(function() {
	

       


                $.ajax({
                    url: '../metadata/'+movie,
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    dataType: 'json'
                }).done(function() {
                    $("#metadata").find('.save').hide('slow');
                });
           
        

});