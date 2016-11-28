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
 * Clase que contiene los metodos necesarios para representar y manejar una neurona de la red
 * asi como los pesos de las sinapsis que tiene asociadas.
 * @author AIR
 * @version 1.0
 * @date Julio 2016
 * @see Sinapsis
 */
public class Neurona implements Serializable{
	private static final long serialVersionUID = 8729854615844306332L;
	//Son conexiones a otras neuronas. En el caso de que sea neurona de entrada, se obvia la lista de entradas.
	public ArrayList<Sinapsis> entradas, salidas;
	private double resultado;
	//Se usa el gradiente para evaluar el error que ha tenido la neurona y poder avanzar en la
	//disminucion del error en la siguiente epoca (ver "backpropagation").
	private double gradiente; 
	//Esto es para simular la neurona umbral. Asi no hay que crear una sinapsis extra por cada
	//neurona. Menos objetos.
	private double p_umbral;
	private double delta_p_umbral = 0.0;
	
	/**
	 * Construir varios tipos de neurona diferente:
	 * 
	 * Neuronas de entrada: No tienen asociada una neurona umbral y las sinapsis de entrada estan vacias.
	 * Neuronas ocultas: Tienen asociada una neurona umbral y tienen sinapsis de entrada y de salida
	 * Neuronas de salida: Tienen asociadas solo sinapsis de entrada y neuronas umbral.
	 * @param conUmbral
	 */
	public Neurona(boolean conUmbral) {
		if(conUmbral) {
			p_umbral=Math.random();
		}
		this.entradas = new ArrayList<Sinapsis>();
		this.salidas = new ArrayList<Sinapsis>();
	}
	
	/**
	 * Retorna y asigna el valor que crea una neurona en funcion de sus entradas.
	 * Se implementa este metodo con la funcion "tanh" que devuelve valores entre [-1,1].
	 * @return Valor de salida de la neurona en funcion de sus entradas.
	 */
	public double salidaNeurona() {
		double suma = p_umbral; //Resultado neurona umbral = 1 * p_umbral
		for(int i=0; i<entradas.size();i++) {
			suma += entradas.get(i).inicio.resultado * entradas.get(i).peso;
		}
		/* FUNCION DE LA NEURONA */
		resultado = Math.tanh(suma); //La neurona usa la funcion tangente hiperbolica
		return resultado;
	}
	/**
	 * @return La salida derivada del resultado.
	 */
	public double salidaNeuronaDerivada() {
		return 1-(resultado*resultado); //Aproximacion a la derivada de tanh
	}
	
	/**
	 * Es llamado SOLAMENTE en el caso de que sea una neurona en una capa oculta.
	 */
	public void calculaGradientesNeuronaOculta(){
		//Sumar las contribuciones en los errores de las neuronas que estan conectadas a esta
		double suma = 0.0;
		for(int i=0; i<salidas.size(); i++) {
			suma+=salidas.get(i).peso*salidas.get(i).fin.gradiente;
		}
		gradiente = suma*salidaNeuronaDerivada();
	}
	
	/**
	 * Es llamado SOLAMENTE en el caso de uqe sea una neurona en la capa de salida.
	 * @param valorEsperado Valor que se esperaba en la salida correspondiente a esta neurona.
	 */
	public void calculaGradientesNeuronaSalida(double valorEsperado) {
		gradiente = (valorEsperado-resultado) * salidaNeuronaDerivada();
	}
	
	/**
	 * Actualiza los pesos de todas las sinapsis de entrada de la neurona en base a los parametros de la red.
	 * @param razonAprendizaje Parametro de la red (ver Neural Network en Wikipedia). Un numero mas bajo disminuye los errores, un numero mas alto aumenta la rapidez de aprendizaje (cambio del peso)
	 * @param momento "Fuerza" con la que cambia el peso
	 */
	public void actualizarPesos(double razonAprendizaje, double momento) {
		for(int i=0; i<entradas.size(); i++) {
			double deltaPesoViejo = entradas.get(i).deltaWeight;
			double deltaPesoNuevo = razonAprendizaje * entradas.get(i).inicio.resultado * gradiente + momento * deltaPesoViejo;
			entradas.get(i).deltaWeight = deltaPesoNuevo;
			entradas.get(i).peso += deltaPesoNuevo;
		}
		//Actualizar peso de la neurona umbral
		double deltaPesoViejo = delta_p_umbral;
		double deltaPesoNuevo = razonAprendizaje * resultado * gradiente + momento * deltaPesoViejo;
		delta_p_umbral = deltaPesoNuevo;
		p_umbral += deltaPesoNuevo;
	}
	
	public double getResultado() {
		return resultado;
	}
	
	public void setResultado(double resultado) {
		this.resultado = resultado;
	}
	
	public double getGradiente() {
		return gradiente;
	}
	
	public ArrayList<Sinapsis> getEntradas(){
		return entradas;
	}
	
	public ArrayList<Sinapsis> getSalidas(){
		return salidas;
	}
}
