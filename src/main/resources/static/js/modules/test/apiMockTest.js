$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'test/mock/list',
        datatype: "json",
        colModel: [
            {label: 'url', name: 'url', width: 90, key: true },
            {label: '接口类型', name: 'method', sortable: false, width: 40},
            {label: '请求头', name: 'headers', sortable: false, width: 120},
//            {label: 'Cookies信息', name: 'cookies', sortable: false, width: 100},
//            {label: '表单信息', name: 'forms', sortable: false, width: 100},
            {label: '参数信息', name: 'queries', sortable: false, width: 160},
            {label: '响应', name: 'json', width: 200},
            {label: '状态', name: 'status', sortable: false, width: 30 }
//            ,
//            {
//                label: '执行操作', name: '', width: 45, sortable: false, formatter: function (value, options, row) {
//                    var downloadFileBtn1 = "&nbsp;&nbsp;<a href='" + baseURL + "test/mock/export/confile' class='btn btn-primary'><i class='fa fa-download'></i>&nbsp;导出配置</a>";
//                    var downloadFileBtn2 = "&nbsp;&nbsp;<a href='" + baseURL + "test/mock/export/pdfile' class='btn btn-primary'><i class='fa fa-download'></i>&nbsp;接口文档</a>";
//                    return downloadFileBtn1+downloadFileBtn2;
//                }
//            }
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
            method: null
        },
        showList: true,
        showEdit: false,
        title: null,
        testMockApiinfofull: {}
    },
    methods: {
        query: function () {
            if (vm.q.method != null) {
                vm.reload();
            }
        },
        add: function () {
            vm.showList = false;
            vm.showEdit= true;
            vm.title = "新增";
            vm.testMockApiinfofull = {};
//            vm.testMockApiinfofull.url = null;
        },

        valid: function () {
            var url = getSelectedRow();
            console.log("%s",url)
            if (url == null) {
                return;
            }

            $.get(baseURL + "test/mock/valid/" + url, function (r) {
                vm.showList = True;
                vm.showEdit= false;
                vm.title = "校验";
                vm.testMockApiinfofull = r.testMockApiinfofull;
            });
        },

        genrateconffile: function () {
            var url = "test/mock/genrateconffile"
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.testMockApiinfofull),
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

        exportconfile: function () {
            var url = "test/mock/export/confile";
            window.open(baseURL + url);
        },
        exportpdfile: function () {
           var url = "test/mock/export/pdfile";
           window.open(baseURL + url);
        },
        update: function () {
            var url = getSelectedRow();
            console.log("%s",url)
            if (url == null) {
                return;
            }

            $.get(baseURL + "test/mock/info/" + url, function (r) {
                vm.showList = false;
                vm.showEdit= true;
                vm.title = "修改";
                vm.testMockApiinfofull = r.testMockApiinfofull;
            });
        },
        saveOrUpdate: function () {
            if (vm.validator()) {
                return;
            }

//            var url = vm.testMockApiinfofull.url == null ? "test/mock/save" : "test/mock/update";
            var url = "test/mock/saveorupdate"
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.testMockApiinfofull),
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
            var urls = getSelectedRows();
            if (urls == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "test/mock/delete",
                    contentType: "application/json",
                    data: JSON.stringify(urls),
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
        delfile: function () {
             var urls = "delete conf file";
             confirm('确定要删除历史配置文件？', function () {
                 $.ajax({
                     type: "POST",
                     url: baseURL + "test/mock/deletefile",
                     contentType: "application/json",
                     data: JSON.stringify(urls),
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
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'method': vm.q.method},
                page: page
            }).trigger("reloadGrid");
        },

        validator: function () {
            if (isBlank(vm.testMockApiinfofull.method)) {
                alert("URL不能为空");
                return true;
            }

            if (isBlank(vm.testMockApiinfofull.url)) {
                alert("类型不能为空");
                return true;
            }

            if (isBlank(vm.testMockApiinfofull.json)) {
                alert("Json返回不能为空");
                return true;
            }
        }
    }
});