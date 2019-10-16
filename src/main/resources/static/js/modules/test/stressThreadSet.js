var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "setId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el:'#rrapp',
    data:{
        showList: true,
        title: null,
        menu:{
            parentName:null,
            parentId:0,
            type:1,
            orderNum:0
        }
    },
    methods: {
        getMenu: function(){
            //加载菜单树
            $.get(baseURL + "test/teststressthreadset/select", function(r){
                ztree = $.fn.zTree.init($("#menuTree"), setting, r.testStressThreadSetList);
                var node = ztree.getNodeByParam("setId", vm.menu.parentId);
                ztree.selectNode(node);

                vm.menu.parentName = node.name;
            })
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.menu = {parentName:null,parentId:0,type:1,orderNum:0};
            vm.getMenu();
        },
        update: function () {
            var setId = getMenuId();
            if(setId == null){
                return ;
            }

            $.get(baseURL + "test/teststressthreadset/info/"+setId, function(r){
                vm.showList = false;
                vm.title = "修改";
                vm.menu = r.testStressThreadSet;

                vm.getMenu();
            });
        },
        del: function () {
            var setId = getMenuId();
            if(setId == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "test/teststressthreadset/delete",
                    data: "setId=" + setId,
                    success: function(r){
                        if(r.code === 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function () {
            if(vm.validator()){
                return ;
            }

            var url = vm.menu.setId == null ? "test/teststressthreadset/save" : "test/teststressthreadset/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.menu),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        menuTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择菜单",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#menuLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {//取消按钮的方法
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.menu.parentId = node[0].setId;
                    vm.menu.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            Menu.table.refresh();
        },
        validator: function () {
           if(vm.menu.type === 2 && isBlank(vm.menu.key)){
                alert("配置项不能为空");
                return true;
            }

            //配置项
            if(vm.menu.type === 2 && isBlank(vm.menu.value)){
                alert("配置项值不能为空");
                return true;
            }
        }
    }
});


var Menu = {
    id: "menuTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Menu.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '配置项ID', field: 'setId', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: '配置名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '200px'},
        {title: '上级配置', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '配置项', field: 'key', align: 'center', valign: 'middle', sortable: true, width: '220px'},
        {title: '配置值', field: 'value', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '类型', field: 'type', align: 'center', valign: 'middle', sortable: true, width: '100px', formatter: function(item, index){
        	if(item.type === 0){
                return '<span class="label label-primary">脚本</span>';
            }
            if(item.type === 1){
                return '<span class="label label-success">线程组</span>';
            }
            if(item.type === 2){
                return '<span class="label label-warning">配置项</span>';
            }
        }},
        {title: '配置说明', field: 'explain', align: 'center', valign: 'middle', sortable: true},
        {title: '配置号', field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '80px'},
        {title: '脚本ID', field: 'fileId', align: 'center', valign: 'middle', sortable: true, width: '80px'},
        {title: '执行操作', align: 'center', valign: 'middle', sortable: true, width: '120px', formatter: function(item, index){
        	if(item.type === 0){
        		var btn = '';
        		btn = "<a href='#' class='btn btn-primary' onclick='synchronizeJmx(" + item.fileId + ")' ><i class='fa fa-arrow-circle-right'></i>&nbsp;同步配置</a>";
                return btn;
            }
        }}]
    return columns;
};

function synchronizeJmx(fileId) {
    if (!fileId) {
        return;
    }
    confirm('确定同步线程组配置到脚本文件？', function () {
        $.ajax({
            type: "POST",
            url: baseURL + "test/teststressthreadset/synchronizeJmx",
            contentType: "application/json",
            data: JSON.stringify(fileId),
            success: function (r) {
                if (r.code == 0) {
                    vm.reload();
                    alert('操作成功', function () {
                    });
                } else {
                    alert(r.msg);
                }
            }
        });
    });
}

function getMenuId () {
    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return false;
    } else {
        return selected[0].id;
    }
}


$(function () {
    var colunms = Menu.initColumn();
    var table = new TreeTable(Menu.id, baseURL + "test/teststressthreadset/list", colunms);
    table.setExpandColumn(2);
    table.setIdField("setId");
    table.setCodeField("setId");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.setHeight("660px");
    table.init();
    Menu.table = table;
});
