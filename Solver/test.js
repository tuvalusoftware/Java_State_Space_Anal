const Solver = require('./solver.js')

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

s = [
	{
			"vars": {
					"x":"",
					"y":""
			},
			"constraints": {
					"x>=5":"",
					"x>=7":"",
					"x>=6":""
			}
	},
	{
		"vars": {
				"x":"",
				"y":""
		},
		"constraints": {
				"x>=4":"",
				"x>=5":"",
				"x<=7":"",
				"x<=6":""
		}
	},
	{
			"vars": {
					"x":"",
					"y":"",
					"z":"",
					"t":""
			},
			"constraints": {
					"x+y>=5*x-1.2*y":"",
					"x-2*y<=-1":"",
					"x+2*y-z>=42-t+2*y":"",
					"x+y-z-t>=2*t-0.6*z-3*t":"",
					"x+0.5*y-1.3*z<=45*x-4*t":"",
					"x+y>=6*x-1.2*y":"",
					"x-2*y<=-1":"",
					"x+4*y-z>=42-t+2*y":"",
					"x+4*y-z-t>=2*t-0.6*z-3*t":"",
					"x+0.51*y-1.3*z<=5*x-4*t":"",
					"2*x+y-0.1*z-t>=2*t-0.6*z-3*t":"",
					"x+0.5*y-1.3*z<=45*x-4*t":"",
					"x+0.17*y<=6*x-1.2*y":"",
					"x-2*y+z<=-1.4":"",
					"x+4*y-z>=-t+2*y-z+0.1*x":"",
					"x-y-z+t>=-t+z-y":"",
					"x-2*y-3*t+4*z>=0":"",
					"z>=0":"",
			}
	}
]

//2: optimized
//3: infeasible
//5: unbounded
async function main(){
	print(await Solver.trimRedundant(s[1]))
}

main()








//
