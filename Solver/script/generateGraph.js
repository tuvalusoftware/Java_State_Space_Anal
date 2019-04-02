const dotParser = require("./dot-parser")
const fs = require("fs")
const Viz = require('viz.js')
const { Module, render } = require('viz.js/full.render.js')
const viz = new Viz({ Module, render })

function print(s){
  console.log(s)
}

String.prototype.format = function() {
  a = this;
  for (k in arguments) {
    a = a.replace("{" + k + "}", arguments[k])
  }
  return a
}

function main(){
    // let data = "digraph G {start [shape=Mdiamond];}"
    // let result = await viz.renderString(data,{engine:"dot"})
    // console.log(result)
    // fs.writeFile("./cac.svg",result,err=>{
    //   console.log(err)
    // })
    // console.log("The file was saved!");
    let path = "../src/main/java/PetrinetJson{0}"
    fs.readdir(path.format(""), function (err, files) {
      if (err) return "Error in reading path"
      for(filename of files){
        fs.readFile(path.format("/"+filename),"utf8",(err,petrinet)=>{
          if (err) return "Error in reading file"
          petrinet = JSON.parse(petrinet)
          print(petrinet)
        })
      }
    })
}

print(main())
