package Perceptron;

//This file is part of Red-Neuronal.
//
//Red-Neuronal is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License.
//
//Red-Neuronal is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with Red-Neuronal.  If not, see <http://www.gnu.org/licenses/>.
//	
import java.util.ArrayList;

/**
 * Clase de pruebas para la red.
 * @author AIR
 * @version 1.0
 * @date Julio 2016
 */
public class Main {
	/*
	 * Se simula una red neuronal (perceptron) de la siguiente forma
	 * 
	 *  
	 *   input    hidden     out
	 *               *
	 *  	x        *        y
	 *      x        *        
	 *               *
	 *  
	 *  Una vez creada la red se entrena para resolver operaciones XOR
	 *  
	 *  XOR
	 *     0  0 --> 0
	 *     0  1 --> 1
	 *     1  0 --> 1
	 *     1  1 --> 0
	 * 
	 */

	public static void main(String[] args) {
		//Para llevar un seguimiento de errores.
		int errores=0;
		int intentos=0;
		
		/* PARAMETROS DE LA RED----------*/
		double MOMENTO = 0.5;
		double RAZON_APRENDIZAJE = 0.2;
		int inputs = 2;
		int[] array_ocultas = {4};
		int outputs = 1;
		/* ------------------------------*/
		
		Red red = new Red(inputs,array_ocultas,outputs,RAZON_APRENDIZAJE,MOMENTO);
		
		//Tabla XOR
		double entradas[][] = {{0,0},{0,1},{1,0},{1,1}}; //Entradas
		double salidas[] = {0.0, 1.0, 1.0, 0.0};         //Salidas esperadas
		
		//Simular entrenamiento
		for(int etapa =0 ; etapa<8000; etapa++){
			for(int i=0; i<4; i++) {
				ArrayList<Double> ins = new ArrayList<Double>(); //Entradas
				for(int j=0;j<2;j++) { //Se colocan las entradas en la lista que se le pasa como parametro a la red
					ins.add(entradas[i][j]);
				}
				double[] arr = red.epoca(ins); //Valores retornados
				for(int j = 0; j<arr.length; j++) {
					double error = salidas[i]-arr[j];
					System.out.println("Ciclo "+etapa+" Epoca: "+i+" Entrada:"+entradas[i][0]+
								"  "+entradas[i][1]+"  Salida :"+arr[j]+ " Esperado: "+ salidas[i]+ " ERROR: "+error);
					
					if(error>=0.1)
						errores++;
						
					intentos++;
							
					double[] salidaEsperada = new double[1];
					salidaEsperada[0] = salidas[i];
					red.calibrar(salidaEsperada);
				}
			}
		}			
		System.out.println("\n\nERRORES TOTALES:"+errores+", intentos:"+intentos*entradas.length );
	}

}
