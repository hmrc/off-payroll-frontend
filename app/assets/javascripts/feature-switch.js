$(document).ready(function() {
    $("#feature-switch\\.optimisedFlow").click(function() {
        if(this.checked) {
            $("#feature-switch\\.decisionServiceVersion-2\\.2").prop("disabled", false);
            $("#feature-switch\\.decisionServiceVersion-2\\.4").prop("disabled", false);
        } else {
            $("#feature-switch\\.decisionServiceVersion-1\\.5\\.0-final").prop("checked", true);
            $("#feature-switch\\.decisionServiceVersion-2\\.2").prop("disabled", true);
            $("#feature-switch\\.decisionServiceVersion-2\\.4").prop("disabled", true);
        }
    });
});
