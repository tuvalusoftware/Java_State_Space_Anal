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
  {"node":{"11":"11\\n1, 0, 1, 1, ","12":"12\\n1, 0, 0, 2, ","13":"13\\n0, 1, 2, 0, ","14":"14\\n0, 1, 1, 1, ","15":"15\\n0, 1, 0, 2, ","16":"16\\n0, 0, 3, 0, ","17":"17\\n0, 0, 2, 1, ","18":"18\\n0, 0, 1, 2, ","19":"19\\n0, 0, 0, 3, ","0":"0\\n3, 0, 0, 0, ","1":"1\\n2, 1, 0, 0, ","2":"2\\n1, 2, 0, 0, ","3":"3\\n2, 0, 1, 0, ","4":"4\\n2, 0, 0, 1, ","5":"5\\n0, 3, 0, 0, ","6":"6\\n1, 1, 1, 0, ","7":"7\\n1, 1, 0, 1, ","8":"8\\n0, 2, 1, 0, ","9":"9\\n0, 2, 0, 1, ","10":"10\\n1, 0, 2, 0, "},"arc":{"11":[3,7,14],"12":[4,15],"13":[16,17,8],"14":[17,18,6,9],"15":[18,19,7],"16":[13],"17":[10,14],"18":[11,15],"19":[12],"0":[1],"1":[2,3,4],"2":[5,6,7],"3":[1,6],"4":[0,7],"5":[8,9],"6":[2,8,10,11],"7":[1,9,11,12],"8":[5,13,14],"9":[2,14,15],"10":[6,13]},"TP":[1,1,0,1,1,1,1,2,1,1,2,1,1,1,1,3,1,1,3,0]}
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
  let ssGraph = dotParser.statespace(ss)
  let netGraph = dotParser.petrinet(ss.TP,[])

  viz.renderString(ssGraph,{engine: 'dot'})
  .then(result => {
    socket.emit('statespace',result)
  })
  .catch(error => {
    console.error(error)
  });

  viz.renderString(netGraph,{engine: 'dot'})
  .then(result => {
    socket.emit('petrinet',result)
  })
  .catch(error => {
    console.error(error)
  });





})
