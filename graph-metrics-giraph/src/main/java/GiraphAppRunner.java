import java.io.IOException;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.GiraphFileInputFormat;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat;
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

public class GiraphAppRunner implements Tool{
	
	private Configuration conf;
	private String inputPath;
	private String outputPath;
	private GiraphConfiguration giraphConf;
	private static final Logger LOG = Logger.getLogger(GiraphAppRunner.class);
	
	@Override
	public Configuration getConf() {
		return conf;
	}

	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	@Override
	public int run(String[] args) throws Exception {
		setInputPath("/home/lehel/workspace/graph-metrics/graph-metrics-giraph/src/main/resources/tiny.txt");
		
		giraphConf = new GiraphConfiguration();
		
		giraphConf.setComputationClass(InDegree.class);
		giraphConf.setVertexInputFormatClass(JsonLongDoubleFloatDoubleVertexInputFormat.class);
		GiraphFileInputFormat.addVertexInputPath(giraphConf, new Path(getInputPath()));
		giraphConf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
		
		giraphConf.setWorkerConfiguration(0, 1, 100);
		giraphConf.setLocalTestMode(true);
		giraphConf.setMaxNumberOfSupersteps(10);
		
		giraphConf.SPLIT_MASTER_WORKER.set(giraphConf, false);
		giraphConf.USE_OUT_OF_CORE_GRAPH.set(giraphConf, true);
		
		countInDegree();
		countOutDegree();
		
		return 1;
		
	}
	
	public void countInDegree() {
		setOutputPath("/home/lehel/workspace/graph-metrics/graph-metrics-giraph/src/main/resources/indegree.txt");
		GiraphJob inDegreeJob;
		try {
			inDegreeJob = new GiraphJob(giraphConf, getClass().getName());
			FileOutputFormat.setOutputPath(inDegreeJob.getInternalJob(), new Path(getOutputPath()));
			inDegreeJob.run(true);
		} catch (IOException | ClassNotFoundException | InterruptedException exception) {
			LOG.error(exception);
		}
	}
	
	public void countOutDegree() {
		setOutputPath("/home/lehel/workspace/graph-metrics/graph-metrics-giraph/src/main/resources/outdegree.txt");
		giraphConf.setComputationClass(OutDegree.class);
		GiraphJob outDegreeJob;
		try {
			outDegreeJob = new GiraphJob(giraphConf, getClass().getName());
			FileOutputFormat.setOutputPath(outDegreeJob.getInternalJob(), new Path(getOutputPath()));
			outDegreeJob.run(true);
		} catch (IOException | ClassNotFoundException | InterruptedException exception) {
			LOG.error(exception);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new GiraphAppRunner(), args);
	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	
}
