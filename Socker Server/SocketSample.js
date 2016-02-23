var express=require('express');
var app=express();
var http= require('http').createServer(app);
var io=require('socket.io').listen(http);
//var GCM = require('gcm').GCM;
app.get('/',function(req,res)
{
	res.send("This is Push notification Server");
});

//socket.io listener for events
io.on('connection', function(socket){

console.log(socket.id);
//this what you get from Client(Android)
socket.on('send',function(data)
{
console.log(data);
//this what you sent to client(Android)
socket.emit('send_response',"i got you");
//this will generate push notification
socket.emit('notification',data);
});
socket.on('send_all',function(data)
{
//this is what you send to all connected clients
socket.broadcast.emit('notification',data);
console.log("broadcast"+data);
});
});

//port
http.listen(3000, function(){
console.log('listening on *:3000');

});