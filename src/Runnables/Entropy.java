package Runnables;

import java.util.*;

public class Entropy {
	static double getEntroy(String message) {
		Vector alphabet = new Vector();
		double probability[];
		
		//add unique characters
		for(int i = 0; i < message.length(); i++) {
			if(alphabet.size() == 0) {
				alphabet.add(message.charAt(0));
			}
			else if(!alphabet.contains(message.charAt(i))) {
				alphabet.add(message.charAt(i));
			}
		}
		
		//initialize probability
		probability = new double[alphabet.size()];
		for(int i = 0; i < probability.length; i++) {
			probability[i] = 0;
		}
		
		//adds occurances of each unique character to probabiltiy
		for(int i = 0; i < message.length(); i++) {
			for(int j = 0; j < alphabet.size(); j++) {
				if(alphabet.elementAt(j).toString().equals(message.charAt(i) + "")) {
					probability[j]++;
				}
			}
		}
		
		//compute probability and Entropy
		for(int i = 0; i < probability.length; i++) {
			probability[i] /= message.length();
		}
		
		//compute entropy
		double Entropy = 0;
		for(int i = 0; i < probability.length; i++) {
			Entropy += probability[i] * logBase(2, (1 / probability[i]));
		}
		
		return Entropy;
	}
	static double logBase(int base, double x) {
		return (Math.log(x) / Math.log(base));
	}
}
