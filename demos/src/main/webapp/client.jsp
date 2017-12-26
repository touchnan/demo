
<script type="text/javascript">

    // Create a socket
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var socket = new WS('@@{WebSocket.ChatRoomSocket.join(user)}')
    
    // Display a message
    var display = function(event) {
        $('#thread').append(tmpl('message_tmpl', {event: event}));
        $('#thread').scrollTo('max')
    }
    
    // Message received on the socket
    socket.onmessage = function(event) {
        var parts = /^([^:]+):([^:]+)(:(.*))?$/.exec(event.data)
        display({
            type: parts[1],
            user: parts[2],
            text: parts[4]
        })
    }
    
    $('#send').click(function(e) {
        var message = $('#message').val()
        $('#message').val('')
        socket.send(message)
    });
    
    $('#message').keypress(function(e) {
        if(e.charCode == 13 || e.keyCode == 13) {
            $('#send').click()
            e.preventDefault()
        }
    })
    
</script>
