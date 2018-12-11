sc = SparkContext("local", "graph")
sqlContext = SQLContext(sc)

./bin/spark-submit --packages graphframes:graphframes:0.6.0-spark2.3-s_2.11 b.py



##query
#sort:
graph.vertices.sort('id',ascending=True).show()
graph.edges.orderBy(['dst','src'], ascending=[0, 1]).show()
graph.edges.orderBy(['src','dst'], ascending=[0, 1]).show()

#filter:
graph.vertices.filter('id>20 and id<30').show()
graph.vertices\
.filter('id>20 and id<30')\
.filter('id%2 == 1').show()

graph.vertices.filter(graph.vertices.marking['0']=="[['c']]").show()


#select:
graph.vertices\
.filter('id>20 and id<30')\
.filter('id%2 == 1')\
.select('id')\
.show()

graph.edges\
.select('src','transition')\
.show()

#findpath:
#find all possible paths have 4 nodes: a->b->c->d
#name of node cannot be number
path = graph.find('(a)-[e1]->(b); (b)-[e2]->(c); (c)-[e3]->(d)')
path.filter('a.id == 0 and d.id == 29').show()

#node properties:
graph.vertices.filter(graph.vertices.marking['0']=="[['c']]").show()
