
import inspect
import json
import pyspark
from pyspark import SparkContext
from pyspark.sql import SQLContext
from pyspark.sql.types import *
from graphframes import GraphFrame

sc = SparkContext("local", "graph")
sqlContext = SQLContext(sc)


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

data = [
('id467', [('a',),('b',),('c',)], [(1,),(2,),(3,)], [('aaa',5),('bbb',28)])
]

node = sqlContext.createDataFrame(data,schema)
node.printSchema()

node.show(1000,False)
