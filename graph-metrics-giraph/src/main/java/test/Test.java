package test;

import java.io.IOException;

import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class Test extends BasicComputation<Text, Text, Text, LongWritable>{
	@Override
	public void compute(Vertex<Text, Text, Text> vertex, Iterable<LongWritable> messages) throws IOException {
		vertex.setValue(new Text(String.valueOf(vertex.getNumEdges())));
		vertex.voteToHalt();
	}
}
