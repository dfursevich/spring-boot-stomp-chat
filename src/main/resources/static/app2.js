angular.module("app", []).controller("home", function ($scope, $http) {
    var self = this;
    var stompClient = null;

    $scope.error = null;

    //connection area
    $scope.connected = false;

    //send to user area
    $scope.userId = null;
    $scope.message = null;

    //inbox
    $scope.inboxItems = [];

    //room area
    $scope.roomId = null;
    $scope.peerId = null
    $scope.messages = [];
    $scope.message = null;
    var subscription = null;

    $scope.connect = function () {
        connect();
    };

    $scope.disconnect = function () {
        disconnect();
    };

    $scope.sendToUser = function () {
        stompClient.send("/api/messages/user/" + $scope.userId, {}, $scope.message);
        $scope.userId = "";
        $scope.message = "";
    }

    $scope.startRoom = function (inboxItem) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        $scope.roomId = inboxItem.roomId;
        $scope.peerId = inboxItem.peerId;
        subscription = subscribeRoomMessages($scope.roomId);
        fetchRoomMessages($scope.roomId);
    }

    $scope.sendToRoom = function () {
        stompClient.send("/api/messages/room/" + $scope.roomId, {}, $scope.message);
        $scope.message = "";
    }

    var subscribeInbox = function () {
        stompClient.subscribe('/user/queue/inbox', function (response) {
            fetchInbox();
        });
    };

    var fetchInbox = function () {
        stompClient.subscribe('/api/inbox', function (response) {
            $scope.inboxItems = JSON.parse(response.body);
            $scope.$digest();
        });
    };

    var subscribeRoomMessages = function (roomId) {
        return stompClient.subscribe('/topic/messages/' + roomId, function (response) {
            fetchRoomMessages(roomId);
        });
    };

    var fetchRoomMessages = function (roomId) {
        stompClient.subscribe('/api/messages/' + roomId, function (response) {
            $scope.messages = JSON.parse(response.body);
            $scope.$digest();
            markReadMessages(roomId);
        });
    };

    var markReadMessages = function (roomId) {
        stompClient.send("/api/messages/room/" + roomId + "/read", {}, {});
    }

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    var connect = function () {
        var socket = new SockJS('/chat-ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            $scope.connected = true;
            $scope.$digest();
            subscribeInbox();
            fetchInbox();
        }, function (error) {
            $scope.error = error.headers.message;
            $scope.$digest();
        });
    };

    var disconnect = function () {
        if (stompClient != null) {
            stompClient.disconnect();
            $scope.inboxItems = [];
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
