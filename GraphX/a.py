#reference: http://spark.apache.org/docs/2.1.0/api/python/pyspark.sql.html#pyspark.sql.GroupedData.agg
sc = SparkContext("local", "graph")
sqlContext = SQLContext(sc)
#command
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
#sort:
graph.vertices.sort('id',ascending=True).show()
graph.edges.orderBy(['dst','src'], ascending=[0, 1]).show()
graph.edges.orderBy(['src','dst'], ascending=[0, 1]).show()

#filter
graph.vertices.filter("P2[0].m0=='c' and size(P2)==1").show()

#select:
graph.vertices\
.filter('id>20 and id<30')\
.filter('id%2 == 1')\
.select('id')\
.show()

graph.edges\
.select('src','transition')\
.show()

#motif
path = graph.find('(a)-[e1]->(b); (b)-[e2]->(c); (c)-[e3]->(d)')
path.filter('a.id == 0 and d.id == 29').show()

path = graph.find('(a)-[e1]->(b); (b)-[e2]->(c); (c)-[e3]->(d)')
path.filter('a.id == 0 and d.id == 29')\
.filter('c.P0[0].m0 == \'a\'')\
.select('c','e1','e2','e3')\
.show(1000,False)


##state space properties:
#boundedness/k-safe:
graph.vertices.agg(F.max(F.size(graph.vertices.P0))).show()
graph.vertices.agg(F.min(F.size(graph.vertices.P0))).show()

#dead marking/deadlock:
validSrc = set(i.src for i in graph.edges.select('src').distinct().collect())
graph.vertices.filter(~graph.vertices.id.isin(validSrc)).show(1000,False)

#dead tranition:
validTransition = set(i.transition for i in graph.edges.select('transition').distinct().collect())
allTransition = set(i for i in range(T))
deadTransition = allTransition.difference(validTransition)

#invariance + [condition]:
size = graph.vertices.count()
qualified = graph.vertices.filter(F.size(graph.vertices.P0)>0).count()
if (qualified == size):
    print True
else:
    print False

#reachability + [currentmarking] + [condition]:
#from currentmarkingID -> marking qualifies [condition]
path = graph.bfs("id = 0", "id = 33",edgeFilter="transition == 0", maxPathLength=5)
path.show(1000,False)
