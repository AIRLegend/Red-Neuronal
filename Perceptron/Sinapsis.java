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

/**
 * Clase que representa a la sinapsis (conexion) entre dos neuronas.
 * Cada neurona tiene una o varia conexiones. Por cada conexion se transmite un dato. Cada conexion tiene un peso
 * que va variando para que en la siguiente epoca la red se comporte de manera diferente (si es necesario que sea asi).
 * 
 * @author AIR
 * @version 1.0
 * @date Julio 2016
 * @see Neurona
 */
public class Sinapsis implements Serializable{
	private static final long serialVersionUID = 7597241645279827278L;
	public Neurona inicio, fin;        //Neuronas de inicio y fin
	public double peso;                //Peso asociado a la conexion
	public double deltaWeight = 0.0;   //Variacion del peso con respecto al de la epoca anterior

	public Sinapsis(Neurona inicio, Neurona fin, double peso) {
		this.inicio = inicio;
		this.fin = fin;
		this.peso = peso;
	}
}
