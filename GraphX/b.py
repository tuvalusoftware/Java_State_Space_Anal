
import inspect
import json
import collections
import pyspark
import pyspark.sql.functions as F
from pyspark import SparkContext
from pyspark.sql import SQLContext
from pyspark.sql.types import *
from graphframes import GraphFrame

sc = SparkContext("local", "graph")
sc.setLogLevel('WARN')
sqlContext = SQLContext(sc)

#read graph data and node schema
schema = sqlContext.read.json('./schema.json').schema
with open('./graph.json') as f:
    raw = json.load(f,object_pairs_hook=collections.OrderedDict)

T = raw['T']
P = raw['P']
# build a graph from json input
temp = []
for node in raw['node']:
    marking = []
    for p in node:
        if p == 'id':
            marking.append(node[p])
        else:
            marking.append(eval(node[p]))
    temp.append(tuple(marking))

v = sqlContext.createDataFrame(temp,schema)

temp = []
for i in raw['arc']:
    src,dst = eval(i)
    temp.append((src,dst,raw['arc'][i]))

e = sqlContext.createDataFrame(temp, ['src','dst','transition'])
graph = GraphFrame(v,e)

#basic query
graph.vertices.agg(F.max(F.size(graph.vertices.P0))).show()










#
