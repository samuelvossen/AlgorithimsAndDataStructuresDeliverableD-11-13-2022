import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

public class DelivD {

	private File inputFile;
	private File outputFile;
	private PrintWriter output;
	private Graph graph;

	// Constructor - DO NOT MODIFY
	public DelivD(File in, Graph gr) {
		inputFile = in;
		graph = gr;

		// Set up for writing to a file
		try {
			// Use input file name to create output file in the same location
			String inputFileName = inputFile.toString();
			String outputFileName = inputFileName.substring(0, inputFileName.length() - 4).concat("_out.txt");
			outputFile = new File(outputFileName);

			// A Printwriter is an object that can write to a file
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}

		// Calls the method that will do the work of deliverable D
		runDelivD();

		output.flush();
	}

	// *********************************************************************************
	// This is where your work starts
	static int tourDistance = 0;
	Stack<Node> stack = new Stack<Node>();

	private void runDelivD() {
		Collections.sort(graph.getNodeList(), new nodeComparer());
		bitonic(graph);
		System.out.println("Shortest bitonic tour has distance " + tourDistance);
		output.println("Shortest bitonic tour has distance " + tourDistance);
		System.out.print("Tour is ");
		output.print("Tour is ");

		final int c = stack.size();
		for (int i = 0; i < c; i++) {
			String xx = stack.pop().getAbbrev();
			if (i == 0) {
				System.out.print(xx);
				output.print(xx);
			} else {
				System.out.print(" -> " + xx);
				output.print(" -> " + xx);
			}
		}
		tourDistance = 0;

	}

	private class nodeComparer implements Comparator<Node> {
		public int compare(Node node1, Node node2) {
			if (Float.parseFloat(node1.getValue()) > Float.parseFloat(node2.getValue())) {
				return -1;
			} else if (Float.parseFloat(node1.getValue()) < Float.parseFloat(node2.getValue())) {
				return 1;
			} else {
				if (Float.parseFloat(node1.getValue()) < Float.parseFloat(node2.getValue())) {
					return 1;
				} else if (Float.parseFloat(node1.getValue()) < Float.parseFloat(node2.getValue())) {
					return -1;
				} else {
					return node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev());
				}
			}

		}
	}

	private void bitonic(Graph graph) {
		Node headNode = graph.getNodeList().get(0);
		Node nextNode = headNode;
		boolean destinationReached = false;
		for (int i = 0; i < graph.getNodeList().size(); i++) {
			int minDistance = Integer.MAX_VALUE;
			if (!destinationReached) {
				nextNode.setDescoveryTime(i);
				for (Edge someEdge : nextNode.getOutgoingEdges()) {

					if (someEdge.getDistance() < minDistance && someEdge.getHead().getDescoveryTime() == 0
							&& Float.parseFloat(someEdge.getHead().getValue()) <= Float
									.parseFloat(someEdge.getTail().getValue())) {
						minDistance = someEdge.getDistance();
						nextNode = someEdge.getHead();
					} else if (someEdge.getDistance() == minDistance && someEdge.getHead().getDescoveryTime() == 0) {
						if (someEdge.getHead().getName().compareTo(nextNode.getName()) > -1) {
							minDistance = someEdge.getDistance();
							nextNode = someEdge.getHead();
						}

					}
				}
				tourDistance += minDistance;
				if (nextNode.equals(graph.getNodeList().get(graph.getNodeList().size() - 1))) {
					destinationReached = true;
				}
			} else {
				nextNode.setDescoveryTime(i);
				for (Edge someEdge : nextNode.getOutgoingEdges()) {

					if (someEdge.getDistance() < minDistance && someEdge.getHead().getDescoveryTime() == 0
							&& Float.parseFloat(someEdge.getHead().getValue()) >= Float
									.parseFloat(someEdge.getTail().getValue())) {
						minDistance = someEdge.getDistance();
						nextNode = someEdge.getHead();
					} else if (someEdge.getDistance() == minDistance && someEdge.getHead().getDescoveryTime() == 0) {
						if (someEdge.getHead().getName().compareTo(nextNode.getName()) > -1) {
							minDistance = someEdge.getDistance();
							nextNode = someEdge.getHead();
						}

					}
				}
				tourDistance += minDistance;

			}

			if (i == 0) {
				stack.add(headNode);
				stack.add(nextNode);
			} else {
				stack.add(nextNode);
			}

		}

	}

}
