$(function () {
    //
    $("body").on("click","#env-settings-confirm",function(){
        function formVal(name){
            return $("#env-settings-page input[name='"+name+"']").val();
        }
        $.ajax("/api/gocd-agent/env",{
            type: "PUT",
            headers:{
                "Content-Type":"application/json"
            },
            data:JSON.stringify({
                scaleStep: formVal("scaleStep"),
                maxInstances: formVal("maxInstances"),
                minIdles: formVal("minIdles"),
                managedServiceResource: formVal("managedServiceResource").replace(/\s*/,""),
                agentStack: formVal("agentStack").replace(/\s*/,""),
                agentEnvironment: formVal("agentEnvironment").replace(/\s*/,"")

            }),
            dataType:"json",
            success: function(data){

                location.reload();
            }
        });
    });


});
