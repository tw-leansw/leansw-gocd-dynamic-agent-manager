$(function () {
    //
    var templates = {};
//

    var loadTemplate = function (tempName, path) {
        $.ajax({
            url: path,
            method: 'GET',
            dataType: "text",
            success: function (data) {
                templates[tempName] = Handlebars.compile(data);
            }
        });
    }
    loadTemplate("edit", "/xht/settings.xhtml");

    $(".agent-setting").click(function (e) {
        //
        $("#settingsModal").remove();
        //
        var parentTr = $(e.target).parents("tr");
        var selectJob = {
            id: parentTr.find(".agent-id").val(),
            name: parentTr.find(".agent-name").val(),
            scaleStep: parentTr.find(".agent-scale-step").val(),
            maxInstances: parentTr.find(".agent-max-instances").val(),
            minIdles: parentTr.find(".agent-min-idles").val(),
            resources: parentTr.find(".agent-resources").val(),
            autoConfig: parentTr.find(".agent-auto-config").val()
        }
        //
        $("#modalContainder").append(templates.edit(selectJob));
        if(selectJob.autoConfig === 'true'){
            $("#settingsModal input[name=autoConfig]").attr("checked","checked");
        }
        setTimeout(function () {
            $("#settingsModal").modal("show")
        }, 200);
    });

    $("body").on("click","#agent-settings-confirm",function(){
        function formVal(name){
            return $("#settingsModal input[name='"+name+"']").val();
        }
        $.ajax("/api/gocd-agent/update",{
            type: "POST",
            headers:{
                "Content-Type":"application/json"
            },
            data:JSON.stringify({
                id: formVal("id"),
                name: formVal("name"),
                scaleStep: formVal("scaleStep"),
                maxInstances: formVal("maxInstances"),
                autoConfig: $("#settingsModal input[name=autoConfig]")[0].checked,
                minIdles: formVal("minIdles"),
                resources: formVal("resources").replace(/\s*/,"").split(",")
            }),
            dataType:"json",
            success: function(){
                location.reload();
            }
        });
    });


});
