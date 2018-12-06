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

module.exports = {
  graph: function(ss){
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
