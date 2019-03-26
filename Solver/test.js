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
			vars: {
					x:"",
					y:""
			},
			constraints: {
				"-5.7*x+5.9*y<=5.5":"",
				"-8.7*x-3.4*y>=-5.5":"",
				"-2.6*x+3.1*y<=9.5":"",
				"9.8*x-3.1*y>=4.8":"",
				"3.6*x+2.2*y>=-1.8":""
			}
	},
	{
		vars:{
			x:"",
			y:"",
			z:""
		},
		constraints:{
			"-6.9*x+8.2*y-4.4*z<=6.1":"",
			"8.2*x-3.4*y+8.4*z>=2.6":"",
			"7.2*x+5.5*y-4.5*z<=-9.3":"",
			"8.6*x+1.0*y+8.9*z<=-5.6":"",
			"2.8*x-2.6*y+3.2*z>=-4.0":"",
			"2.2*x-7.8*y-7.3*z>=-7.1":""
		}
	},
	{
		vars:{
			x:"",
			y:"",
			z:""
		},
		constraints:{
			"8.8*x-4.3*y+2.5*z<=-2.0":"",
			"-0.4*x+3.3*y-8.6*z>=7.6":"",
			"-1.3*x-0.6*y-0.3*z<=-4.5":"",
			"0.8*x-2.2*y-3.8*z<=-2.1":"",
			"3.9*x+2.1*y+6.3*z<=-9.9":"",
			"-4.0*x-5.9*y-2.3*z<=-4.6":""
		}
	},
	{
		vars:{
			x:"",
			y:"",
			z:""
		},
		constraints:{
			"-5.5*x+1.5*y-9.9*z>=-4.5":"",
			"4.5*x+6.9*y-7.5*z>=2.0":"",
			"5.3*x-6.0*y-9.0*z>=-3.9":""
		}
	},
	{
		vars:{
			x:"",
			y:"",
			z:""
		},
		constraints:{
			"1.9*x-7.2*y+6.9*z<=-6.2":"",
			"7.3*x-7.9*y-0.3*z>=4.1":"",
			"-7.0*x+0.6*y-9.3*z<=-3.8":""
		}
	}
]


//merge s[3],s[4]

async function main(){
	// let s =  Solver.generateSystemLinear(["x","y","z"],6)
	// Solver.printSystem(s)
	// s = await Solver.trimRedundancy(s)
	// print(await Solver.solve(system))

	let system = s[0]
	Solver.printSystem(system)

	let reduced = await Solver.trimRedundancy(system)
	Solver.printSystem(reduced)

	print(await Solver.solve(reduced))


	// let system = Solver.merge(s[3],s[4])
	// Solver.printSystem(system)
	//
	// let reduced = await Solver.trimRedundancy(system)
	// Solver.printSystem(reduced)
	//
	// print(await Solver.solve(reduced))
}

main()








//
