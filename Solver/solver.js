const request = require('request')

function print(s){
	console.log(s)
}

function random(low,high,interval){
	let range = (high-low)/interval+1
	let result = Math.floor(Math.random()*range)*interval+low
	return result.toFixed(1)
}

String.prototype.format = function() {
  a = this;
  for (k in arguments) {
    a = a.replace("{" + k + "}", arguments[k])
  }
  return a
}

let decimalOffset = 0.00001
let url = 'http://localhost:6000/solve'

module.exports = {
	//2: optimized
	//5: unbounded
	//3: infeasible
  solve: function(system){
  	return new Promise(function(resolve, reject) {
  		request.post(url, {json: system}, (err, res, body) => {
  			if (err) reject(err)
  			if (res.statusCode == 200){
					resolve(body)
  			}
  		})
    })
  },

  merge: function(s1,s2){
		let result = {
			vars:{},
			constraints:{}
		}
  	for (key in s1.vars){
			result.vars[key] = s1.vars[key]
  	}
		for (key in s2.vars){
			result.vars[key] = s2.vars[key]
		}
  	for (key in s1.constraints){
			result.constraints[key] = s1.constraints[key]
		}
		for (key in s2.constraints){
			result.constraints[key] = s2.constraints[key]
		}
		return result
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

	trimRedundancy: async function(s){
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
					print("remove: " + key)
					delete s.constraints[key]
				}
			}
		}
		return s
	},

	printSystem: function(system){
		for (key in system.constraints){
			print(key+",")
		}
	},

	generateSystemLinear: function(vars, len){
		system = {
			vars:{},
			constraints:{}
		}
		//parse var
		for (i in vars){
			system.vars[vars[i]] = ""
		}

		//randomly generate inequalities for system
		let inequality = ""
		for (let i=0; i<len; i++){
			inequality = ""
			for (let j in vars){
				//lhs
				let coeff = random(-10,10,0.1)
				if (j == 0 || coeff<0){
					inequality += ("{0}*{1}".format(coeff,vars[j]))
				} else {
					inequality += ("+{0}*{1}".format(coeff,vars[j]))
				}
			}
			//sense
			let sense = random(0,1,1)
			if (sense == 0){
				inequality += "<="
			}
			else if (sense == 1){
				inequality += ">="
			}
			let constant = random(-10,10,0.1)
			inequality += constant
			system.constraints[inequality] = ""
		}
		return system
	},

	generateSystemFormal: function(vars,len){
		system = {
			vars:{},
			constraints:{}
		}
		//parse var
		for (i in vars){
			system.vars[vars[i]] = ""
		}

		//randomly generate inequalities for system
		let inequality = ""
		for (let i=0; i<len; i++){
			//lhs
			inequality = vars[vars.length-1]
			//sense
			let sense = random(0,1,1)
			if (sense == 0){
				inequality += "<="
			}
			else if (sense == 1){
				inequality += ">="
			}
			//rhs
			for (let j=0; j<vars.length-1; j++){
				let coeff = random(-10,10,0.1)
				if (j == 0 || coeff<0){
					inequality += ("{0}*{1}".format(coeff,vars[j]))
				} else {
					inequality += ("+{0}*{1}".format(coeff,vars[j]))
				}
			}
			//constant
			let constant = random(-10,10,0.1)
			if (constant>=0){
				inequality += "+"+constant
			}
			else{
				inequality += constant
			}
			system.constraints[inequality] = ""
		}
		return system
	}
}
