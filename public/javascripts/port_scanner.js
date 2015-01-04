$( document ).ready(function() {
    SCANNER.initialize($("#port-scan-form"))
});

SCANNER =
{
    form:null,
    error_target:null,
    initialize:function(form)
    {
        SCANNER.form = form;
        SCANNER.error_target = $("#error-target");

        SCANNER.form.submit(SCANNER.submit);

        $body = jQuery("body");

        jQuery(document).on({
            ajaxStart: function() { $body.addClass("loading");    },
            ajaxStop: function() { $body.removeClass("loading"); }
        });

        SCANNER.log("port scanner is online");
    },
    submit:function(event)
    {
        event.preventDefault();

        SCANNER.error_target.html("");

        var text = $('textarea#hosts').val();

        if(text.length < 1)
        {
            alert("Please enter at least one hostname or IP to scan.");
        }
        else
        {
            var hosts = text.split(",");

            var error = [];
            var validHosts = [];

            $.each(hosts, function( index, value )
            {
                var host = $.trim(value);
                var valid = SCANNER.validate(host);

                if(valid)
                {
                    validHosts.push(host);
                }
                else
                {
                    error.push("<p class=\"error\">\"" + value +"\" is not a valid host or IP address</p>")
                }
            });

            if(error.length > 0)
            {
                SCANNER.error_target.html("<p>Please fix the following errors and try again:</p>\n" + error.join("\n"));
            }
            else
            {
                SCANNER.log(validHosts);
                SCANNER.scan(validHosts);
            }
        }
    },
    scan:function(hosts)
    {
        jQuery.ajax({
            type: "POST",
            url: SCANNER.form.attr("action"),
            data: JSON.stringify({hosts:hosts}),
            dataType: 'json',
            beforeSend: setHeader,
            success: function(data)
            {
                alert("success");
                //jQuery( "div#auto-quote-form-div").html(data['html']);
                //
                //if(jQuery("form#auto-quote-form").length > 0)
                //{
                //    autodealio_init_form(jQuery("form#auto-quote-form"));
                //}
                //
                //jQuery('html,body').animate({
                //    scrollTop: jQuery("div#auto-quote-form-main").offset().top - 80
                //}, 'slow');

            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                SCANNER.error_target.html(XMLHttpRequest.responseText);
            }
        });
    },
    validate:function(host)
    {
        if(true == validateIPaddress(host))
            return true;

        return validateHost(host);
    },
    log:function(message)
    {
        if (window.console) {
            console.log(message);
        }
    }
};

function setHeader(xhr) {
    xhr.setRequestHeader('Content-Type', 'application/json');
}

function validateHost(host)
{
    var pat = /(?:\w+\.)+\w+/gm;
    return (pat.test(host));
}

function validateIPaddress(ipaddress)
{
    if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipaddress))
    {
        return (true)
    }
    return (false)
}
