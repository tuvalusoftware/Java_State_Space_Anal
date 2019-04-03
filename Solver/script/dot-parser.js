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
  graph: function(petrinet){
    let marking = petrinet.Markings
    let expression = petrinet.Expressions
    let variable = petrinet.Variables
    let guard = petrinet.Guards

    let dot = ''

    let T = 'T{0} [shape=rectangle, fontcolor=white, color=white, fillcolor="#3f51b5", style="filled,rounded", label="{1}"]'
    for (let i=0; i<guard.length; i++){
      dot += T.format(i,guard[i]) + '\n'
    }

    let POn = '{0} [shape=record, fontcolor=white, color=white, fillcolor="#4CAF50", style="filled,rounded", label="{ P{1} | {2}}"]'
    let POff = '{0} [shape=record, style="rounded", label="{ P{1} | {2}}"]'

    for (let P in marking){
      if (marking[P] != ''){
        dot += POn.format(P,P,"") + '\n'
      }
      else{
        dot += POff.format(P,P,"") + '\n'
      }
    }

    let InArc = '{0}->T{1} [label="{2}"]'
    for (let i=0; i<variable.length; i++){
      for (let j=0; j< variable[i].length; j++){
        dot += InArc.format(variable[i][j][0],i,variable[i][j][1]) + '\n'
      }
    }
    let OutArc = 'T{0}->{1} [label="{2}"]'
    for (let i=0; i<expression.length; i++){
      for (let j=0; j< expression[i].length; j++){
        dot += OutArc.format(i,expression[i][j][0],expression[i][j][1]) + '\n'
      }
    }
    result = 'digraph g {\ngraph[rankdir=LR]\nnodesep=1\nranksep=1\n{0}}'.format(dot)
    return result.format(dot);
  },

}
