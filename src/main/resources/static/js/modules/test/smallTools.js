var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            cmdkeyword: null
        },
        testNode: {},
        title: null,
        showList: true
    },
    methods: {
        deldockercontainer: function () {
            var cmdkeyword = vm.q.cmdkeyword;
            if (cmdkeyword == null) {
                return;
            }
            confirm('确定要  删除  匹配关键字的Docker容器？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "test/docker/container/delete/",
                    contentType: "application/json",
                    data: JSON.stringify(cmdkeyword),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function () {
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        startdockercontainer: function () {
            var cmdkeyword = vm.q.cmdkeyword;
            if (cmdkeyword == null) {
                return;
            }
            confirm('确定要  启动  匹配关键字的Docker容器？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "test/docker/container/start/",
                    contentType: "application/json",
                    data: JSON.stringify(cmdkeyword),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function () {
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        runlinuxcommand: function () {
                var cmdkeyword = vm.q.cmdkeyword;
                if (cmdkeyword == null) {
                    return;
                }
                confirm('确定要运行输入的命令？', function () {
                    $.ajax({
                        type: "POST",
                        url: baseURL + "test/docker/container/runlinuxcommand/",
                        contentType: "application/json",
                        data: JSON.stringify(cmdkeyword),
                        success: function (r) {
                            if (r.code == 0) {
                                alert('操作成功', function () {
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                });
              } ,
        stopdockercontainer: function () {
             var cmdkeyword = vm.q.cmdkeyword;
             if (cmdkeyword == null) {
                 return;
             }
             confirm('确定要  停止  匹配关键字的Docker容器？', function () {
                 $.ajax({
                     type: "POST",
                     url: baseURL + "test/docker/container/stop/",
                     contentType: "application/json",
                     data: JSON.stringify(cmdkeyword),
                     success: function (r) {
                         if (r.code == 0) {
                             alert('操作成功', function () {
                             });
                         } else {
                             alert(r.msg);
                         }
                     }
                 });
             });
         } ,
         deldockerimage: function () {
              var cmdkeyword = vm.q.cmdkeyword;
              if (cmdkeyword == null) {
                  return;
              }
              confirm('确定要 删除 匹配关键字的Docker镜像？', function () {
                  $.ajax({
                      type: "POST",
                      url: baseURL + "test/docker/image/delete/",
                      contentType: "application/json",
                      data: JSON.stringify(cmdkeyword),
                      success: function (r) {
                          if (r.code == 0) {
                              alert('操作成功', function () {
                              });
                          } else {
                              alert(r.msg);
                          }
                      }
                  });
              });
          }
    }
});

