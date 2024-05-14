package de.carlos.machine;

import org.junit.Test;

public class NeuralNetworkTest {

	@Test
	public void  testForward(){
		
		NeuralNetwork nn = new NeuralNetwork(2,2,1);
		
		nn.getNode(0, 0).setParameters(new double[]{});
		
		
		
	}
}
