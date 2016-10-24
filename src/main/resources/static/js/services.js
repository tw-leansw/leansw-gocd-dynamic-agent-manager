$(function(){
    //
    var delModalTemplate ;
//
    var editModalTemplate ;

    $.ajax({
        url: "/xht/delete.xhtml",
        method: 'GET',
        dataType: "text",
        success:function(data){
            delModalTemplate = Handlebars.compile(data);
        }
    });

    $.ajax({
        url: "/xht/edit.xhtml",
        method: 'GET',
        dataType: "text",
        success: function(data){
            editModalTemplate = Handlebars.compile(data);
        }
    });

    $(".del-job").click(function (e) {
        //
        $("#deleteModal").remove();
        //
        var parentTr = $(e.target).parents("tr");
        var selectJob = {
            name: parentTr.find(".job-name").html(),
            group: parentTr.find(".job-group").html(),
            cron: parentTr.find(".job-cron").html(),
            type: parentTr.find(".job-type").html(),
            dataMap: parentTr.find(".job-data-map").html()
        }
        //
        $("#modalContainder").append(delModalTemplate(selectJob));
        setTimeout(function(){$("#deleteModal").modal("show")},200);
    });

    //leansw_quartz_job_api_url
    $(".edit-job").click(function(e){
        //
        $("#editModal").remove();
        var parentTr = $(e.target).parents("tr");
        var selectJob = {
            name: parentTr.find(".job-name").html(),
            group: parentTr.find(".job-group").html(),
            cron: parentTr.find(".job-cron").html(),
            type: parentTr.find(".job-type").html(),
            apiUrl: parentTr.find(".job-data-map").attr("api-url")
        }
        $("#modalContainder").append(editModalTemplate(selectJob));
        setTimeout(function(){$("#editModal").modal("show")},200);
    });



    $("body").on("click","#job-del-confirm",function(){
        $.ajax({
            type: "DELETE",
            url: "/api/quartz/job?name="+$("#deleteModal input[name='name']").val()+"&group="+$("#deleteModal input[name='group']").val(),
            success: function(){
                location.reload();
            }
        });
    });


    $("#job-create").click(function(){
        $("#editModal").remove();
        $("#modalContainder").append(editModalTemplate({}));
        setTimeout(function(){$("#editModal").modal("show")},200);
    });



    $("body").on("click","#job-edit-confirm",function(){
        function formVal(name){
            return $("#editModal input[name='"+name+"']").val();
        }
        $.ajax("/api/quartz/job",{
            type: "POST",
            headers:{
                "Content-Type":"application/json"
            },
            data:JSON.stringify({
                group: formVal("group"),
                name: formVal("name"),
                cron: formVal("cron"),
                type: formVal("type"),
                dataMap: {
                    "leansw_quartz_job_api_url": formVal("url")
                }
            }),
            dataType:"json",
            success: function(){
                location.reload();
            }
        });
    });
});
