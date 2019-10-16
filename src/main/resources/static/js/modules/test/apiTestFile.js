$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'test/apiFile/list',
        datatype: "json",
        colModel: [
            {label: '文件ID', name: 'fileId', width: 30, key: true},
            // {label: '用例ID', name: 'caseId', width: 35},
            {label: '用例名称', name: 'caseName', width: 35, sortable: false},
            {
                label: '文件名称',
                name: 'originName',
                width: 100,
                sortable: false
            },
            {label: '接口类型', name: 'apiType', width: 35, sortable: false},
            {label: '添加时间', name: 'addTime', width: 70},
            {
            	label: '执行操作', name: '', width: 95, sortable: false, formatter: function (value, options, row) {
                    var btn = '';

                    if (row.status == 1) {
                        btn = "<a href='#' class='btn btn-danger' onclick='stop(" + row.fileId + ")' ><i class='fa fa-stop-circle'></i>&nbsp;停止</a>";
                    } else {
                        btn = "<a href='#' class='btn btn-primary' onclick='run(" + row.fileId + ")' ><i class='fa fa-arrow-circle-right'></i>&nbsp;启动</a>";
                    }

                    // var stopBtn = "<a href='#' class='btn btn-primary' onclick='stop(" + row.fileId + ")' ><i class='fa fa-stop'></i>&nbsp;停止</a>";
                    // var stopNowBtn = "<a href='#' class='btn btn-primary' onclick='stopNow(" + row.fileId + ")' ><i class='fa fa-times-circle'></i>&nbsp;强制停止</a>";
                    var downloadFileBtn = "&nbsp;&nbsp;<a href='" + baseURL + "test/apiFile/downloadFile/" + row.fileId + "' class='btn btn-primary'><i class='fa fa-download'></i>&nbsp;下载</a>";
                    return btn + downloadFileBtn;
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

    $("#jqGridNode").jqGrid({
            url: baseURL + 'test/node/list',
            datatype: "json",
            colModel: [
                {
                    label: '节点IP',
                    name: 'realip',
                    width: 30,
                    key: true,
                    sortable: false,
                    formatter: function (value, options, row) {
//                        return "<a href='javascript:void(0);' onclick='" +
//                            "ShowDetailNodeInfo(" + row.ip + ")'>" + value + "</a>";
                        return "<a href='" + baseURL + "test/nodedetail/info/" + row.ip + "'>" + value + "</a>";

                    }
                },
                {label: '节点VIP', name: 'vip', width: 35, sortable: false},
                {label: '用户名', name: 'userName', width: 35, sortable: false},
                {label: '密码', name: 'password', width: 70},
                {label: '描述', name: 'description', width: 70}
            ],
            viewrecords: true,
            height: 385,
            rowNum: 50,
            rowList: [10, 30, 50, 100, 200],
            rownumbers: true,
            rownumWidth: 25,
            autowidth: true,
            multiselect: true,
            pager: "#jqGridPagerNode",
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
                $("#jqGridNode").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
            }
        });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            originName: null
        },
        apiTestFile: {},
        node: {},
        title: null,
        showChart: false,
        showList: true,
        showEdit: false
    },
    methods: {
        query: function () {
            if (vm.q.fileId != null) {
                vm.reload();
            }
            if (vm.q.ip != null) {
                vm.reloadnode();
            }
        },
        queryByApiType: function () {
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'originName': vm.q.originName},
                page: 1
            }).trigger("reloadGrid");
        },
        del: function () {
            var fileIds = getSelectedRows();
            if (fileIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "test/apiFile/delete",
                    contentType: "application/json",
                    data: JSON.stringify(fileIds),
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
        back: function () {
            history.go(-1);
        },
        reload: function (event) {
            vm.showChart = false;
            vm.showList = true;
            vm.showEdit = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'originName': vm.q.originName},
                page: page
            }).trigger("reloadGrid");
        }
    }
});

function run(fileIds) {
    if (!fileIds) {
        return;
    }


    var testapitype="http";

    var temp=document.getElementsByName("env");
      for (i=0;i<temp.length;i++){
      //遍历Radio
        if(temp[i].checked)
          {
          //获取Radio的值
          testenv=temp[i].value;
          console.log("env: %s", testenv);
          }
      }
//      用于实现下拉框存在bug，返回null
//       var obj = document.getElementById("selectip");
//       var testenv1=obj.value;
//       -----------------------------------------

//     var tempapitype=document.getElementsByName("apitype");
//           for (i=0;i<tempapitype.length;i++){
//           //遍历Radio
//             if(tempapitype[i].checked)
//               {
//               testapitype=tempapitype[i].value;
//               console.log("apitype：%s", testapitype);
//               //获取Radio的值
//               }
//           }
//    var testenv = "localhost"
    var reqdata={"fileIds":numberToArray(fileIds),"env":testenv};
//    var reqdata={"fileIds":numberToArray(fileIds),"apitype":testapitype,"env":testenv};
    $.ajax({
        type: "POST",
        url: baseURL + "test/apiFile/run",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(reqdata),
        success: function (r) {
            if (r.code == 0) {
                vm.reload();
                console.log(r.msg+JSON.stringify(reqdata));
            }
        }
    });
}

function stop(fileIds) {
    if (!fileIds) {
        return;
    }
    $.ajax({
        type: "POST",
        url: baseURL + "test/apiFile/stop",
        contentType: "application/json",
        data: JSON.stringify(numberToArray(fileIds)),
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


function ShowDetailNodeInfo(ip) {
    if (!ip) {
        return;
    }
    $.ajax({
        type: "POST",
        url: baseURL + "test/nodedetail/info",
        contentType: "application/json",
        data: JSON.stringify(ip),
        success: function (r) {
            if (r.code == 0) {
                vm.reload();
                console.log("%s", ip);
                alert(r.msg, function () {
                                });
            } else {
                alert(r.msg);
            }
        }
    });
}
