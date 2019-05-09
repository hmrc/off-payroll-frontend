$(document).ready(function() {
    $(":checkbox").change(function() {
        if(this.checked) {
            $("input[type=radio]").removeAttr("checked");
        }
    });
    $("input[type=radio]").change(function() {
        if(this.checked) {
            $(":checkbox").removeAttr("checked");
        }
    });
});
