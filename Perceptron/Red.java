package redneuronal;

import java.io.Serializable;

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

import java.util.ArrayList;

/**
 * Representa a una red neuronal (ver Perceptron Multicapa)
 * 
 * @author AIR
 * @version 1.1
 * @date Noviembre 2016
 * @see Neurona
 * @see Sinapsis
 */
public class Red implements Serializable{
	private static final long serialVersionUID = 3286199902474469057L;
	private double RAZON_APRENDIZAJE;
	private double MOMENTO; 
	
	//Cada posicion representa una capa de la red. En cada capa va un array con las neuronas de la capa.
	private ArrayList<Neurona[]> network; 
	
	/**
	 * Crea una red de neuronas con el parametro que se pase. Se crean todas las neuronas y las sinapsis.
	 * @param inputs numero de neuronas que actuaran como inputs a la red.
	 * @param capas array de longitud = numero de capas ocultas y que contiene el numero de neuronas en cada capa (en cada posicion).
	 * @param outputs numero de neuronas que produciran el resultado final.
	 * @param razon_Aprendizaje Razon de aprendizaje de la red.
	 * @param momento momento de la red.
	 * @throws RuntimeException si alguno de los parametros no es valido
	 */
	public Red(int inputs, int[] capas, int outputs, double razon_Aprendizaje, double momento) throws RuntimeException{
		if(inputs <1 || outputs<1 || capas.length<1 || razon_Aprendizaje <=0 || momento<=0) {
			throw new RuntimeException();
		}
		this.RAZON_APRENDIZAJE = razon_Aprendizaje;
		this.MOMENTO = momento;
		this.network = new ArrayList<Neurona[]>(inputs + capas.length + outputs);
		
		//Inicializacion de las neuronas
		Neurona[] capa_temporal = new Neurona[inputs];
		for(int i=0; i<inputs; i++) {
			capa_temporal[i] = new Neurona(false); //Sin umbral (es de entrada)				
		}
		network.add(capa_temporal);
		
		Neurona[] array;
		for(int i=0; i<capas.length; i++) {
			array = new Neurona[capas[i]];
			for(int j=0;j<array.length;j++) {
				array[j] = new Neurona(true); //Con umbral
			}
			network.add(array); //Añadir la capa oculta creada
		}
		
		capa_temporal = new Neurona[outputs];
		for(int i=0; i<outputs; i++) {
			capa_temporal[i] = new Neurona(true);			
		}
		network.add(capa_temporal);
		
		//Realizar interconexion
		for(int i=1; i<network.size(); i++) {
			for(int j=0; j<network.get(i).length; j++) {
				for(int k=0; k<network.get(i-1).length; k++) {
					//Crear sinapsis entre neuronas.
					Sinapsis s = new Sinapsis(network.get(i-1)[k], network.get(i)[j], Math.random());
					network.get(i)[j].entradas.add(s); 
					network.get(i-1)[k].salidas.add(s);
				}
			}
		}
	}
	
	/**
	 * Simula una epoca en la red neuronal.
	 * @param inputs Valores que se le pasan a las neuronas de entrada. Tiene que tener la misma longitud que el num. de neuronas
	 * @return array con los valores de salida.
	 */
	public double[] epoca(ArrayList<Double> inputs) {
		double[] outs = new double[network.get(network.size()-1).length];
		if(inputs.size()!=network.get(0).length) {
			return null;
		}
		//Recorrer la red para que cada Neurona vaya generando su valor y pasando a la siguiente.
		for(int i=0; i<inputs.size();i++) {
			network.get(0)[i].setResultado(inputs.get(i));
		}
		
		for(int i=1; i<network.size();i++) {
			for(int j=0; j<network.get(i).length; j++) {
				network.get(i)[j].salidaNeurona(); //Actualiza su valor de salida en funcion de las entradas
			}
		}
		
		for(int i=0; i<network.get(network.size()-1).length;i++) {
			outs[i] = network.get(network.size()-1)[i].getResultado(); //Actualiza el valor de salida de las neuronas de salida.
																       //Se guardan en un array para retornar (por comodidad)
		}
		return outs;
	}
	
	/**
	 * Recalibra los pesos en funcion del error.
	 * @param valoresObjetivo Array con los valores correctos de salida
	 */
	public void calibrar(double[] valoresObjetivo) {
		if(valoresObjetivo.length != network.get(network.size()-1).length)
			return; //Se acaba por error.
		
		//Calcular el gradiente en las neuronas de salida
		for(int i=0; i<network.get(network.size()-1).length;i++) {
			network.get(network.size()-1)[i].calculaGradientesNeuronaSalida(valoresObjetivo[i]);
		}
		//Calcular el gradiente de las neuronas en las capas ocultas
		for(int i = network.size()-2; i>0; i--) {
			for(int j = 0; j<network.get(i).length; j++){
				network.get(i)[j].calculaGradientesNeuronaOculta();
			}
		}
		//Actualizar los pesos de las sinapsis (desde las neuronas de salida hasta la primera capa oculta, ya que cada 
		//neurona actualiza sus sinapsis de entrada)
		for(int i=network.size()-1; i>0; i--) {
			for(int j=0; j<network.get(i).length;j++) {
				network.get(i)[j].actualizarPesos(RAZON_APRENDIZAJE, MOMENTO);
			}
		}
	}
	
	
	public double getMOMENTO() {return MOMENTO;}
	public double getRAZON_APRENDIZAJE() {return RAZON_APRENDIZAJE;}

}
