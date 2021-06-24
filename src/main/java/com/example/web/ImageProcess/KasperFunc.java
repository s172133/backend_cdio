package com.example.web.ImageProcess;

import java.util.ArrayList;
import java.util.Scanner;

public class KasperFunc {

	public String kasper(String response){
		String newout;
		Scanner input = new Scanner(System.in);
		System.out.println(response);
		System.out.println("This output correct? (y/n)");
		String pass = input.nextLine();
		if(pass.equals("n")){
			System.out.println("provide new string: ");
			newout = input.nextLine();
		} else{
			newout = response;
		}
		return newout;
	}

	public ArrayList<Kort> kasperSet(ArrayList<Kort> game, String newString){
		String[] blocks = newString.split("_");			// 3C,2S,6C_7C,9D,4S,4D,KH,QH,5C_2H,,,2C

		String[] block_1 = blocks[0].split(",");			// 3C,2S,6C
		String[] block_2 = blocks[1].split(",");			// 7C,9D,4S,4D,KH,QH,5C
		String[] block_3 = blocks[2].split(",");			// 2H,,,2C


		char[] map;
		int num;
		for (int i = 0; i < game.size(); i++) {
			if(i < 3){ 											// SET correct char array
				map = block_1[i].toCharArray();					// 3,C
			} else if (i < 7){
				map = block_2[i].toCharArray();
			} else{
				map = block_3[i].toCharArray();
			}

			if(map.length < 2){ // fx 4H
				num = Integer.parseInt(String.valueOf(map[0])); // Ciffer  -    3
				game.get(i).setCiffer(num);
				game.get(i).setFarve(map[1]);					// farve   -    C
			} else {
				String combo = String.valueOf(map[0]+map[1]);	// fx 11
				num = Integer.parseInt(combo); 					// Ciffer
				game.get(i).setCiffer(num);
				game.get(i).setFarve(map[2]);					// farve
			}
		}

		return game;
	}
}
