package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
	
	
	private List<List<NNNode>> layers = new ArrayList<>();
	
	/**
	 * Layers[0]: Number of input variables, 
	 * Last one: Number of outputs
	 * In between: Number of layers. 
	 * 
	 * @param layerSizes
	 */
	public NeuralNetwork(int... layerSizes) {
		
		
		for (int layerIndex = 1;layerIndex<layerSizes.length;layerIndex++){
			List<NNNode> layer = new ArrayList<>();
			for (int nodeIndex=0;nodeIndex<layerSizes[layerIndex];nodeIndex++){
				layer.add(new NNNode(layerSizes[layerIndex-1]));
			}
			
			layers.add(layer);
		}		
	}
	
	
	public double[] forwardPropagate(double[] values){

		for (int layerIndex = 0;layerIndex<layers.size();layerIndex++){
			final double[] oldVals = values;
			values = layers.get(layerIndex).stream()
					.mapToDouble(node->node.calculate(oldVals))
					.toArray();												
		}
		return values;
	}
	
	public NNNode getNode(int layerIndex, int nodeIndex){
		return this.layers.get(layerIndex).get(nodeIndex); 
	}

}
