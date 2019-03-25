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
  	for (key in s2.constraints){
			if (!(key in s1.constraints)){
  			s1.constraints[key] = ""
  		}
		}
  	return s1
  },

  negate: function(s){
    result = {
      vars:{},
      constraints:{}
    }
    result.vars = s.vars

    for (i in s.constraints){
      if (i.includes("<=")){
    		result.constraints[i.replace("<=",">=") + "+{0}".format(decimalOffset)] = ""
      }
    	else if (i.includes(">=")){
    		result.constraints[i.replace(">=","<=") + "-{0}".format(decimalOffset)] = ""
      }
    	else if (i.includes("=")){
        result.constraints[i.replace("=",">=") + "+{0}".format(decimalOffset)] = ""
        result.constraints[i.replace("=","<=") + "-{0}".format(decimalOffset)] = ""
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

    for (key in s1.constraints){
			//build system
			let temp = {vars:s1.vars, constraints:{}}
			temp.constraints[key] = ""
      let system = this.merge(temp, s2)
			let flag = await this.solve(system)
      //if 1 case is possible then s2 is not subset of s1
      if (flag == true){
        return false
      }
    }
    //if none of them is true then it is subset
    return true
  },

	trimRedundant: async function(s){
		for (key in s.constraints){
			//father inequality to check
			let s1 = {vars: s.vars, constraints: {}}
			s1.constraints[key] = ""

			//children set to check
			let s2 = JSON.parse(JSON.stringify(s))
			delete s2.constraints[key]

			//true means s1 is father, bigger set
			//now we can remove father from s
			if (await this.isSubset(s1,s2)){
				for (key in s1.constraints){
					delete s.constraints[key]
				}
			}
		}
		return s
	}




}
