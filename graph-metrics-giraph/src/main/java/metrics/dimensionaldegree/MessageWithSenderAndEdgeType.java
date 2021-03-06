package metrics.dimensionaldegree;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import util.MessageWrapper;

public class MessageWithSenderAndEdgeType extends MessageWrapper<LongWritable, Text, Text> {

	@Override
	public Class<LongWritable> getVertexIdClass() {
		return LongWritable.class;
	}

	@Override
	public Class<Text> getMessageClass() {
		return Text.class;
	}

	@Override
	public Class<Text> getEdgeValueClass() {
		return Text.class;
	}

}