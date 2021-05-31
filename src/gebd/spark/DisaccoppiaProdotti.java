package gebd.spark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;

import scala.Tuple2;

		
public class DisaccoppiaProdotti implements PairFlatMapFunction<Tuple2<String, Integer>, String, String> {
			/*
			 * Per ogni tupla in input nel formato K=(p1,p2) V=frequenza
			 * restiuisce in output due tuple nel formato 
			 * K=p1 V=(p2, frequenza)
			 * K=p2 V=(p1, frequenza)
			 */
	public Iterator<Tuple2<String, String>> call(Tuple2<String, Integer> t) {
		List<Tuple2<String, String>> output = new ArrayList<Tuple2<String, String>>();
		output.add(new Tuple2<String, String>(t._1.split(",")[0],t._1.split(",")[1] + "," + t._2));
		output.add(new Tuple2<String, String>(t._1.split(",")[1],t._1.split(",")[0] + "," + t._2));
		return output.iterator();
	}

}
