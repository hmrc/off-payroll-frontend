$(document).ready(function() {
    $(":checkbox").not("#businessSize\\.noneOfAbove").change(function() {
        if(this.checked) {
            $("#businessSize\\.noneOfAbove").removeAttr("checked");
        }
    });
    $("#businessSize\\.noneOfAbove").change(function() {
        if(this.checked) {
            $(":checkbox").not("#businessSize\\.noneOfAbove").removeAttr("checked");
        }
    });
});
