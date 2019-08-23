angular.module("app", []).controller("home", function ($scope, $http) {
    var self = this;
    var stompClient = null;
    $scope.messages = [];
    $scope.connected = false;
    $scope.roomId = null;
    $scope.subscriptions = [];
    $scope.subscribed = false;
    $scope.error = null;
    $scope.typings = [];

    $scope.connect = function () {
        connect();
    };

    $scope.disconnect = function () {
        disconnect();
    };

    $scope.join = function () {
        subscribe();
        fetchMessages();
        $scope.subscribed = true;
        setInterval(function() {
            $scope.typings = $scope.typings.filter(t => isTyping(t, new Date().getTime()));
            $scope.$digest();
        },1000);
    };

    $scope.send = function () {
        stompClient.send("/api/room." + $scope.roomId + '/message', {}, $scope.message);
        $scope.message = "";
    };

    $scope.typing = function () {
        stompClient.send("/api/room." + $scope.roomId + '/typing', {}, null);
    };

    var isTyping = function (typing, timestamp) {
        return typing.time + 5000 > timestamp;
    };

    var addTyping = function (typing) {
        var found = $scope.typings
            .filter(t => t.sessionId === typing.sessionId && t.roomId === typing.roomId);
        if (found.length === 0) {
            $scope.typings.push(typing);
        } else {
            found.forEach(t => t.time = typing.time)
        }
    };

    var subscribe = function () {
        stompClient.subscribe('/topic/room.' + $scope.roomId, function (message) {
            var notification = JSON.parse(message.body);
            if (notification.type === 'ChatSubscribeNotification'
                || notification.type === 'ChatUnsubscribeNotification') {
                fetchSubscriptions();
            } else if (notification.type === 'ChatMessageNotification') {
                fetchMessages();
            } else if (notification.type === 'ChatTypingNotification') {
                addTyping(notification.typing);
                $scope.$digest();
            }
        });
    };

    var fetchSubscriptions = function () {
        stompClient.subscribe('/api/room.' + $scope.roomId + '/subscriptions', function (message) {
            $scope.subscriptions = JSON.parse(message.body);
            $scope.$digest();
        });
    };

    var fetchMessages = function () {
        stompClient.subscribe('/api/room.' + $scope.roomId + "/messages", function (message) {
            $scope.messages = JSON.parse(message.body);
            $scope.$digest();
        });
    };

    var connect = function () {
        var socket = new SockJS('/chat-ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            $scope.connected = true;
            $scope.$digest();
        }, function (error) {
            $scope.error = error.headers.message;
            $scope.$digest();
        });
    };

    var disconnect = function () {
        if (stompClient != null) {
            stompClient.disconnect();
            $scope.messages = [];
            $scope.connected = false;
        }
        console.log("Disconnected");
    }
}).directive("userImage", function () {
    return {
        restrict: "E",
        scope: {
            "user": "=",
            "size": "="
        },
        template: '<img title="{{user.displayName}}" height="{{size}}" width="{{size}}" class="img-rounded" ng-src="https://graph.facebook.com/{{user.id}}/picture?type=large" />'
    };
});