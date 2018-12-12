sc = SparkContext("local", "graph")
sqlContext = SQLContext(sc)

./bin/spark-submit --packages graphframes:graphframes:0.6.0-spark2.3-s_2.11 b.py

##construct DataFrame schema
schema  = StructType([
StructField('id',StringType()),
StructField('p0',ArrayType(StructType([
        StructField('s',StringType())
        ]))),
StructField('p1',ArrayType(StructType([
        StructField('n',IntegerType())
        ]))),
StructField('p2',ArrayType(StructType([
        StructField('s',StringType()),
        StructField('n',IntegerType())
        ])))
])

#[] for a list, () for a object, single element object (o,)
data = [
('id467', [('a',),('b',),('c',)], [(1,),(2,),(3,)], [('aaa',5),('bbb',28)])
]

node = sqlContext.createDataFrame(data,schema)
node.printSchema()


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

graph.vertices.printSchema()
graph.vertices\
.filter('p0[0].n == 1 and size(p0)=2')\
.show()

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

path = graph.find('(a)-[e1]->(b); (b)-[e2]->(c); (c)-[e3]->(d)')
path.filter('a.id == 0 and d.id == 29')\
.filter('c.P0[0].m0 == \'a\'')\
.select('c','e1','e2','e3')\
.show(1000,False)

path = graph.find('(b)-[e2]->(c); (c)-[e3]->(d); (c)-[e4]->(e); (d)-[e5]->(f); (e)-[e6]->(f)')\
.select('e2','e3','e4','e5','e6')\
.filter('e2.dst == 1 and e3 != e4')\
.show(1000,False)

#node properties:
.filter('c.P0[0].m0 == \'a\'')\
