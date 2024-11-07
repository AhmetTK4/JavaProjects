package com.example.deneme;

public class Deneme {
    public static void main(String[] args) {
        int [][] dizi1 = new int[3][4];


        int[][] dizi = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };




        for (int i = 0; i < dizi.length; i++) { // Satırlar
            for (int j = 0; j < dizi[i].length; j++) { // Sütunlar
                System.out.print(dizi[i][j] + " ");
            }
            System.out.println();
        }

       /* dizi [0][0] = 1;
        dizi [0][1] = 1;
        dizi [0][2] = 1;
        dizi [0][3] = 1;
       dizi [1] = new int[]{1,2,3,4};
        dizi [2] = new int[]{4,3,2,1};


        System.out.println(dizi[0][0]);
        System.out.println(dizi[0][3]);
        System.out.println(dizi[1][2]);
        System.out.println(dizi[2][1]);

        */
    }


}
