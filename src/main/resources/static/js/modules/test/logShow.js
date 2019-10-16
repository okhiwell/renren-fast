//$(".log").click(function () {
//    //iframe层
//    layer.open({
//        type: 1,
//        title: '<span class="laytit">接口实时日志</span>',
//        shadeClose: false,
//        shade: 0.7,
//        maxmin: true,
//        area: ['80%', '70%'],
//        content: $("#logdiv").html(), //iframe的url
//        cancel: function(index){
//            closeSocket();
//        }
//    });
//});
//
//<!-- 日志实时推送业务处理 -->
//var stompClient = null;
//
//function openSocket() {
//    if (stompClient == null) {
//        if($("#log-container").find("span").length==0){
//            $("#log-container div").after("<span>通道连接成功,静默等待.....</span><img src='images/loading.gif'>");
//        }
//        var socket = new SockJS('websocket?token=kl');
//        stompClient = Stomp.over(socket);
//        stompClient.connect({token: "kl"}, function (frame) {
//            stompClient.subscribe('/topic/pullLogger', function (event) {
//                var content = JSON.parse(event.body);
//                var leverhtml = '';
//                var className = '<span class="classnametext">' + content.className + '</span>';
//                switch (content.level) {
//                    case 'INFO':
//                        leverhtml = '<span class="infotext">' + content.level + '</span>';
//                        break;
//                    case 'DEBUG':
//                        leverhtml = '<span class="debugtext">' + content.level + '</span>';
//                        break;
//                    case 'WARN':
//                        leverhtml = '<span class="warntext">' + content.level + '</span>';
//                        break;
//                    case 'ERROR':
//                        leverhtml = '<span class="errortext">' + content.level + '</span>';
//                        break;
//                }
//                $("#log-container div").append("<p class='logp'>" + content.timestamp + " " + leverhtml + " --- [" + content.threadName + "] " + className + " ：" + content.body + "</p>");
//                if (content.exception != "") {
//                    $("#log-container div").append("<p class='logp'>" + content.exception + "</p>");
//                }
//                if (content.cause != "") {
//                    $("#log-container div").append("<p class='logp'>" + content.cause + "</p>");
//                }
//                $("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
//            }, {
//                token: "kltoen"
//            });
//        });
//    }
//}
//
//function closeSocket() {
//    if (stompClient != null) {
//        stompClient.disconnect();
//        stompClient = null;
//    }
//}