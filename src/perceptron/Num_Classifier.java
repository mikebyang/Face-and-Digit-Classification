package perceptron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import parser.Data_obj;

public class Num_Classifier {
	private static List<Perceptron_node> num_arr = new ArrayList<Perceptron_node>(10);
	
	public static void num_train(ArrayList<Data_obj> data_arr, double train_per, double lRate) {
		//amount of training set to be used
		int train_size = (int) (data_arr.size() * train_per/100);
		//training imgs are to be randomly selected
		Random rand = new Random();
		int rdm_index = -1;
		Data_obj t_node = null;
		Perceptron_node [] temp_arr = num_arr.toArray(new Perceptron_node[10]);
		for(int i = 0; i < train_size; i++) {
			//get random test sample
			rdm_index = rand.nextInt(data_arr.size());
			t_node = data_arr.get(rdm_index);
			data_arr.remove(rdm_index);
			
			//use label as array index (0-9)
			if(temp_arr[t_node.getLabel()] == null) {
				temp_arr[t_node.getLabel()] = new Perceptron_node(t_node.getData(), lRate);
			}
			else {
				try {
					temp_arr[t_node.getLabel()].mod(t_node.getData(), lRate);
					temp_arr[t_node.getLabel()].setTot();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		//convert back to list
		num_arr = new ArrayList<Perceptron_node>(Arrays.asList(temp_arr));
	}
	
	private static double matmul(Double [][] mat1,Double [][] mat2, Double tot) {
		double total = 0.0;
		if(mat1.length != mat2.length) {
			System.exit(1);
		}
		
		for(int i = 0; i < mat1.length; i++) {
			for(int j = 0; j < mat1[0].length; j++) {
				total += (mat1[i][j] * mat2[i][j]);
			}
		}
		
		return total/tot;
	}
	public static int classify(String [] image) {
		Double [][] img = Perceptron_node.conv(image, 1);
		List<Chance_node> t_arr = new ArrayList<Chance_node>(10);
		for(int i = 0; i < num_arr.size(); i++) {
			t_arr.add(
					new Chance_node(
							matmul(img, num_arr.get(i).getDat(), num_arr.get(i).getTot()), i));
		}
		return Collections.max(t_arr).getLabel();
	}
}
