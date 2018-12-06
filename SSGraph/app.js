const fs = require('fs')
const bodyParser = require('body-parser')
const app = require('express')()
const server = require('http').Server(app)
const io = require('socket.io')(server);
const dotParser = require('./dot-parser.js')
const Viz = require('viz.js');
const { Module, render } = require('viz.js/full.render.js');
const viz = new Viz({ Module, render });

String.prototype.format = function() {
  a = this
  for (k in arguments) {
    a = a.replace("{" + k + "}", arguments[k])
  }
  return a
}

server.listen(2000)
const port = 3000
ss = JSON.parse(`
  {"node":{"22":"22\\n1, 1, 2, ","23":"23\\n1, 1, 2, ","24":"24\\n1, 1, 2, ","25":"25\\n1, 1, 2, ","26":"26\\n1, 1, 2, ","27":"27\\n1, 1, 2, ","28":"28\\n0, 0, 3, ","29":"29\\n0, 0, 3, ","30":"30\\n0, 0, 3, ","31":"31\\n0, 0, 3, ","10":"10\\n1, 1, 2, ","32":"32\\n0, 0, 3, ","11":"11\\n1, 1, 2, ","33":"33\\n0, 0, 3, ","12":"12\\n1, 1, 2, ","13":"13\\n1, 1, 2, ","14":"14\\n1, 1, 2, ","15":"15\\n1, 1, 2, ","16":"16\\n1, 1, 2, ","17":"17\\n1, 1, 2, ","18":"18\\n1, 1, 2, ","19":"19\\n1, 1, 2, ","0":"0\\n3, 3, 0, ","1":"1\\n2, 2, 1, ","2":"2\\n2, 2, 1, ","3":"3\\n2, 2, 1, ","4":"4\\n2, 2, 1, ","5":"5\\n2, 2, 1, ","6":"6\\n2, 2, 1, ","7":"7\\n2, 2, 1, ","8":"8\\n2, 2, 1, ","9":"9\\n2, 2, 1, ","20":"20\\n1, 1, 2, ","21":"21\\n1, 1, 2, "},"arc":{"22":[32],"23":[30],"24":[33],"25":[28],"26":[31],"27":[29],"10":[28],"11":[29],"12":[29],"13":[28],"14":[30],"15":[31],"16":[31],"17":[30],"18":[32],"19":[33],"0":[1,2,3,4,5,6,7,8,9],"1":[10,11,12,13],"2":[16,17,14,15],"3":[18,19,20,21],"4":[18,22,23,14],"5":[19,24,25,10],"6":[26,11,27,15],"7":[16,20,24,26],"8":[21,22,27,12],"9":[17,23,25,13],"20":[33],"21":[32]}}
`
)

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));

app.listen(port, () => {
  console.log(`Interface listening on port ${port}!`)
})

app.get('/', (req,res) =>{
  res.sendFile(__dirname + '/app.html')
})

io.on('connect', socket =>{
  let graph = dotParser.graph(ss)
  viz.renderString(graph,{engine: 'dot'})
  .then(result => {
    socket.emit('graph',result)
  })
  .catch(error => {
    console.error(error)
  });
  socket.emit('graph',result)
})
