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

function readDir(path){
  return new Promise((resolve, reject)=>{
    fs.readdir(path,(err,files)=>{
      if (err) reject(err)
      resolve(files)
    })
  })
}

function readFile(path){
  return new Promise((resolve, reject)=>{
    fs.readFile(path,"utf8",(err,petrinet)=>{
      if (err) reject(err)
      resolve(JSON.parse(petrinet))
    })
  })
}

function writeFile(path,data){
  return new Promise((resolve, reject)=>{
    fs.writeFile(path,data,err=>{
      if (err) reject(err)
      resolve()
      print("Graph generated!")
    })
  })
}

async function main(){
    let path = "../src/main/java/PetrinetJson{0}"
    let files = await readDir(path.format(""))
    // loop through files in directory
    for(filename of files){
      //check extension to read only json
      if(filename.substring(filename.length-4)=="json"){
        //read file
        let petrinet = await readFile(path.format("/"+filename))
        let graph = dotParser.graph(petrinet)
        let result = await viz.renderString(graph,{engine:"dot"})
        //build path and filename.svg to write
        let writeTo = path.format("/"+filename.substring(0,filename.length-4)+"svg")
        print(writeTo)
        await writeFile(writeTo,result)
      }
    }
}

main()
