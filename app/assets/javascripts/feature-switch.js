$(document).ready(function() {
    $("#feature-switch\\.optimisedFlow").click(function() {
        if(this.checked) {
            $("#feature-switch\\.decisionServiceVersion-2\\.0").prop("disabled", false);
            $("#feature-switch\\.businessOnOwnAccount").prop("disabled", false);
        } else {
            $("#feature-switch\\.decisionServiceVersion-1\\.5\\.0-final").prop("checked", true);
            $("#feature-switch\\.decisionServiceVersion-2\\.0").prop("disabled", true);
            $("#feature-switch\\.businessOnOwnAccount").prop("disabled", true);
            $("#feature-switch\\.businessOnOwnAccount").prop("checked", false);
        }
    });
});
