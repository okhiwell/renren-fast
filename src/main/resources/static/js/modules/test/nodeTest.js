$(function () {
    $("#jqGrid").jqGrid({
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
                        return "<a href='" + baseURL + "test/nodedetail/info/" + row.realip + "'>" + value + "</a>";

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
            realip: null
        },
        testNode: {},
        title: null,
        showChart: false,
        showList: true,
        showEdit: false
    },
    methods: {
        query: function () {
            if (vm.q.realip != null) {
                vm.reload();
            }
        },
        add: function () {
            vm.showList = false;
            vm.showEdit= true;
            vm.title = "新增节点";
            vm.testNode = {};
        },
        update: function () {
            var nodeIp = getSelectedRows();
            if (nodeIp == null) {
                return;
            }
            vm.showList = false;
            vm.showEdit = true;
            vm.title = "节点修改";

            $.get(baseURL + "test/node/info/" + nodeIp, function (r) {
                vm.testNode = r.testNode;
            });

        },
        saveOrUpdate: function () {
//            var url = (vm.testNode.ip == null) ? "test/node/save" : "test/node/update";
            var url = "test/node/saveorupdate";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.testNode),
                success: function (r) {
                    if (r.code === 0) {
                        vm.reload();
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        delnode: function () {
            var nodeips = getSelectedRows();
            if (nodeips == null) {
                return;
            }
            confirm('确定要删除选中的节点记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "test/node/delete",
                    contentType: "application/json",
                    data: JSON.stringify(nodeips),
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
            vm.showList = true;
            vm.showEdit = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'vip': vm.q.vip},
                page: page
            }).trigger("reloadGrid");
        }
    }
});

function ShowDetailNodeInfo(realip) {
    if (!realip) {
        return;
    }
    $.ajax({
        type: "POST",
        url: baseURL + "test/nodedetail/info",
        contentType: "application/json",
        data: JSON.stringify(realip),
        success: function (r) {
            if (r.code == 0) {
                vm.reload();
                console.log("%s", realip);
                alert(r.msg, function () {
                                });
            } else {
                alert(r.msg);
            }
        }
    });
}
