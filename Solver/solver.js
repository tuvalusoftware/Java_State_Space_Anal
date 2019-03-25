const request = require('request')

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

let decimalOffset = 0.00001
let url = 'http://localhost:5000/solver'

module.exports = {
  solve: function(system){
  	return new Promise(function(resolve, reject) {
  		request.post(url, {json: system}, (err, res, body) => {
  			if (err) reject(err)
  			if (res.statusCode == 200){
          if (body.status == 2 || body.status == 5){
            resolve(true)
          }
          else{
            resolve(false)
          }
  			}
  		})
    })
  },

  merge: function(s1,s2){
  	for (key in s2.vars){
  		if (!(key in s1.vars)){
  			s1.vars[key] = s2.vars[key]
  		}
  	}
  	for (i in s2.constraints){
  		s1.constraints.push(s2.constraints[i])
  	}
  	return s1
  },

  mergeMultiple: function(s){
  	//1 system then return
  	if (s.length == 1){
  		return s[0]
  	}
  	//concat systems into 1st system
  	for (i in s){
  		for (key in s[i].vars){
  			if (!(key in s1.vars)){
  				s[0].vars[key] = s[i].vars[key]
  			}
  		}
  		for (i in s[i].constraints){
  			s[0].constraints.push(s[i].constraints[i])
  		}
  	}
  	return s[0]
  },

  negate: function(s){
    result = {
      vars:{},
      constraints:[]
    }
    result.vars = s.vars
    let index = 0
    for (i in s.constraints){
      if (s.constraints[i].includes("<=")){
    		result.constraints.push(s.constraints[i].replace("<=",">=") + "+{0}".format(decimalOffset))
      }
    	else if (s.constraints[i].includes(">=")){
    		result.constraints.push(s.constraints[i].replace(">=","<=") + "-{0}".format(decimalOffset))
      }
    	else if (s.constraints[i].includes("=")){
        result.constraints.push(s.constraints[i].replace("=",">=") + "+{0}".format(decimalOffset))
        result.constraints.push(s.constraints[i].replace("=","<=") + "-{0}".format(decimalOffset))
      }
    }
    return result
  },

	//is s1 father of s2
  isSubset: async function(s1,s2){
		//both systems need to be resolvable
		if (!(await this.solve(s1) && (await this.solve(s2)))){
			return false
		}

		//if !A AND B == empty then B is subset
    s1 = this.negate(s1)
    for (i in s1.constraints){
      let system = this.merge({vars:s1.vars, constraints:[s1.constraints[i]]}, s2)
			let temp = await this.solve(system)
      //if 1 case is possible then s2 is not subset of s1
      if (temp == true){
        return false
      }
    }
    //if none of them is true then it is subset
    return true
  }




}
