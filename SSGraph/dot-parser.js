String.prototype.format = function() {
  a = this;
  for (k in arguments) {
    a = a.replace("{" + k + "}", arguments[k])
  }
  return a
}

function print(s){
  console.log(s)
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
  petrinet: function (TP, marking){
    let [inPlaces, outPlaces] = decodeTP(TP);

    let dot = ''
    let T = 'T{0} [shape=rectangle, fontcolor="#cccccc", color="#cccccc", style="rounded"]'
    for (let i=0; i<inPlaces.length; i++){
      dot += T.format(i) + '\n'
    }

    let POn = '{0} [shape=record, fontcolor=white, color=white, fillcolor="#4CAF50", style="filled,rounded", label="{ P{1} | {2}}"]'
    let POff = '{0} [shape=record, style="rounded", label="{ P{1} | {2}}"]'
    for (let i=0; i<marking.length; i++){
      if (marking[i]>0){
        dot += POn.format(i,i,marking[i]) + '\n'
      }
      else{
        dot += POff.format(i,i,marking[i]) + '\n'
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
    result = 'digraph g {\ngraph[rankdir=LR]\n{0}}'
    return result.format(dot);
  }
  ,
  statespace: function(ss){
    let dot = ''
    let T = 'T{0} [shape=rectangle, style="rounded", label="{1}"]'

    for (let n in ss.node){
      dot += T.format(n,ss.node[n]) + '\n'
    }

    let Arc = 'T{0}->T{1}'
    for (i in ss.arc){
      for (j in ss.arc[i]){
        dot += Arc.format(i,ss.arc[i][j]) + '\n'
      }
    }

    result = 'digraph g {\nnodesep=1\nranksep=1\n{0}}'
    return result.format(dot);
  }
}
