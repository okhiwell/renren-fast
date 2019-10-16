$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'test/tool/sublist/helper',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'toolsId', width: 25, key: true},
            {label: '工具名称', name: 'toolsName', sortable: false, width: 75},
            {label: '启动命令', name: 'startCommand', sortable: false, width: 150},
            {label: 'Web链接', name: 'toolsUrl', sortable: false, width: 120},
            {label: '进程ID', name: 'status', sortable: false, width: 30},
            {label: '添加时间', name: 'addTime', width: 75},
            {label: '操作人', name: 'operator', sortable: false, width: 30},
            {label: '备注', name: 'remark', sortable: false, width: 110 },
            {
                label: '执行操作', name: '', width: 95, sortable: false, formatter: function (value, options, row) {
                    var btn = '';

                    if (row.status > 1) {
                        btn = "<a href='#' class='btn btn-danger' onclick='stop(" + row.toolsId + ")' ><i class='fa fa-stop-circle'></i>&nbsp;停止</a>";
                    } else {
                        btn = "<a href='#' class='btn btn-primary' onclick='run(" + row.toolsId + ")' ><i class='fa fa-arrow-circle-right'></i>&nbsp;启动</a>";
                    }
                    var linkBtn = "&nbsp;&nbsp;<a href='"+row.toolsUrl+"' class='btn btn-primary'><i class='fa fa-download'></i>&nbsp;跳转</a>";
                    return btn + linkBtn;
                }
            }
         
        ],
        viewrecords: true,
        height: 385,
        rowNum: 50,
        rowList: [10, 30, 50, 100, 200],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            toolsName: null
        },
        showList: true,
        showEdit: false,
        showUpload: false,
        title: null,
        toolsMoniter: {}
    },
    methods: {
        query: function () {
            if (vm.q.toolsName != null) {
                vm.reload();
            }
        },
        add: function () {
            vm.showList = false;
            vm.showEdit= true;
            vm.showUpload = false;
            vm.title = "新增";
            vm.toolsMoniter = {};
        },
        update: function () {
            var toolsId = getSelectedRow();
            if (toolsId == null) {
                return;
            }

            $.get(baseURL + "test/tool/info/" + toolsId, function (r) {
                vm.showList = false;
                vm.showEdit= true;
                vm.showUpload = false;
                vm.title = "修改";
                vm.toolsMoniter = r.toolsMoniter;
            });
        },
        saveOrUpdate: function () {
            if (vm.validator()) {
                return;
            }

            var url = vm.toolsMoniter.toolsId == null ? "test/tool/save" : "test/tool/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.toolsMoniter),
                success: function (r) {
                    if (r.code === 0) {
                        // alert('操作成功', function(){
                        vm.reload();
                        // });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function () {
            var toolsIds = getSelectedRows();
            if (toolsIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "test/tool/delete",
                    contentType: "application/json",
                    data: JSON.stringify(toolsIds),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.showEdit= false;
            vm.showUpload = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'toolsName': vm.q.toolsName},
                page: page
            }).trigger("reloadGrid");
        },
        validator: function () {
            if (isBlank(vm.toolsMoniter.toolsName)) {
                alert("工具名称不能为空");
                return true;
            }

            if (isBlank(vm.toolsMoniter.toolsUrl)) {
                alert("工具链接不能为空");
                return true;
            }


            if (isBlank(vm.toolsMoniter.operator)) {
                alert("操作人不能为空");
                return true;
            }
        }
    }
});




function run(toolsId) {
    if (!toolsId) {
        return;
    }
//    var reqdata={"toolsId":numberToArray(fileIds),"apitype":testapitype,"env":testenv}
    $.ajax({
        type: "POST",
        url: baseURL + "test/tool/run",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(numberToArray(toolsId)),
        success: function (r) {
            if (r.code == 0) {
                vm.reload();
                alert('操作成功', function () {
                });
            }else{
                alert(r.msg);
            }
        }
    });
}

function stop(toolsId) {
    if (!toolsId) {
        return;
    }
    $.ajax({
        type: "POST",
        url: baseURL + "test/tool/stop",
        contentType: "application/json",
        data: JSON.stringify(numberToArray(toolsId)),
        success: function (r) {
            if (r.code == 0) {
                vm.reload();
                console.log("%o", r.msg);
                alert('操作成功', function () {
                });
            } else {
                alert(r.msg);
            }
        }
    });
}
