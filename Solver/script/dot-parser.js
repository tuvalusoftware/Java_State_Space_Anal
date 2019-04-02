String.prototype.format = function() {
  a = this;
  for (k in arguments) {
    a = a.replace("{" + k + "}", arguments[k])
  }
  return a
}

function decodeTP(TP){
  let i = 0;
  let j = 0;
  let inPlaces = []
  let outPlaces = []
  let temp = []
  while (i < TP.length){
    let m = TP[i]
    let n = TP[i+1]
    i += 2
    temp = []
    for (j=0; j<m; j++){
      temp.push(TP[i+j])
    }
    inPlaces.push(temp)
    i += j
    temp = []
    for (j=0; j<n; j++){
      temp.push(TP[i+j])
    }
    outPlaces.push(temp)
    i += j
  }
  return [inPlaces, outPlaces]
}

module.exports = {
  graph: function(TP, marking){
    let [inPlaces, outPlaces] = decodeTP(TP);
    let dot = ''

    let T = 'T{0} [shape=rectangle, fontcolor="#cccccc", color="#cccccc", style="rounded"]'
    for (let i=0; i<inPlaces.length; i++){
      dot += T.format(i) + '\n'
    }

    let POn = '{0} [shape=record, fontcolor=white, color=white, fillcolor="#4CAF50", style="filled,rounded", label="{ P{1} | {2}}"]'
    let POff = '{0} [shape=record, style="rounded", label="{ P{1} | {2}}"]'

    for (let P in marking){
      let text = ''
      for (let token in marking[P]){
        text += token + ": " + marking[P][token] + "\\n"
      }
      if (text != ''){
        dot += POn.format(P,P,text) + '\n'
      }
      else{
        dot += POff.format(P,P,text) + '\n'
      }
    }

    let InArc = '{0}->T{1}'
    for (let i=0; i<inPlaces.length; i++){
      for (let j=0; j< inPlaces[i].length; j++){
        dot += InArc.format(inPlaces[i][j],i) + '\n'
      }
    }
    let OutArc = 'T{0}->{1}'
    for (let i=0; i<outPlaces.length; i++){
      for (let j=0; j< outPlaces[i].length; j++){
        dot += OutArc.format(i,outPlaces[i][j]) + '\n'
      }
    }
    result = 'digraph g {\ngraph[rankdir=LR]\nnodesep=1\nranksep=1\n{0}}'.format(dot)

    return result.format(dot);
  }
  ,
  table: function (group, marking){
    let dot = ''
    let i = 0;
    let j = 0;
    let count = 0;
    cluster = `subgraph cluster_{0} {
      {1}
    }\n`

    for (i=0; i<group.length; i++){
      groupNodes = ''
      let POn = '{0} [label= {1}, shape=rectangle, style=filled, color="#4CAF50", fontcolor=white, fillcolor="#4CAF50"];'
      let POff = '{0} [label= {1}, shape=rectangle, style=filled, color="#cccccc", fontcolor=white];'
      for (j=0; j<group[i]; j++){
        if (marking[count+j]>0){
          groupNodes += POn.format(count+j,marking[count+j])
        }
        else{
          groupNodes += POff.format(count+j,marking[count+j])
        }
      }
      count += j
      dot += cluster.format(i,groupNodes)
    }
    result = 'digraph g {\n{0}}'
    return result.format(dot);
  }
  ,
  marbleGraph: function (marking){
    let dot = ''
    let node = `{0} [image="marble/{1}.png",color=white, label="", fixedsize=true, scale=both, width={2}, height={3}];\n`
    let cluster = `subgraph cluster_{0} {
      label={1}
      style=rounded
      color=lightgrey
      {2}
    }\n`
    let id = 0
    let ownerList = {}

    //marble parsing
    for (key in marking[0]){
      key = JSON.parse(key.replace(/'/g, '"'))
      let [color, size, owner] = key
      if (!(owner in ownerList)){
        ownerList[owner] = [id]
      }
      else{
        ownerList[owner].push(id)
      }
      dot += node.format(id,color,size,size)
      id += 1
    }

    //transaction parsing
    let transaction = '{0} [label="{1}",shape=box,style=rounded];\n'
    transactionList = []
    for (key in marking[1]){
      dot += transaction.format(id,key)
      transactionList.push(id)
      id += 1
    }

    //subgraph grouping
    for (owner in ownerList){
      let nodescript = ''
      for (node in ownerList[owner]){
        nodescript += ownerList[owner][node] + '; '
      }
      dot += cluster.format(owner,owner,nodescript)
    }

    nodescript = ''
    for (i in transactionList){
      nodescript += transactionList[i] + '; '
    }
    dot += cluster.format("transaction","transaction",nodescript)
    let result = 'digraph g {\n{0}}'

    return result.format(dot);
  }
}
