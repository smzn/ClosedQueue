package closedqueue;

import java.util.Random;

public class ClosedQueue_simulation {
	
	private double p[][], mu[]; //推移確率行列(今回は重力モデルから)
	private int time;
	Random rnd = new Random();
	private int k; //ノード数
	private int n; //系内の客数
	
	public ClosedQueue_simulation(double[][] p, int time, int k, int n, double[] mu) {
		this.p = p;
		this.time = time;
		this.k = k;
		this.n = n;
		this.mu = mu;
	}
	
	public double[][] getSimulation() {
		double service[] = new double[k];
		int queue[] = new int[k]; //各ノードのサービス中を含むキューの長さ
		queue[0] = n; //最初はノード0にn人いるとする
		service[0] = this.getExponential(mu[0]); //先頭客のサービス時間設定
		double elapse = 0;
		double total_queue[] = new double[k]; //各ノードの延べ系内人数
		double result[][] = new double[1][k];
		
		while(elapse < time) {
			double mini_service = 100000; //最小のサービス時間
			int mini_index = -1; //最小のサービス時間をもつノード
			
			for(int i = 0; i < k; i++) { //待ち人数がいる中で最小のサービス時間を持つノードを算出
				if( queue[i] > 0) {
					if( mini_service > service[i]) {
						mini_service = service[i];
						mini_index = i;
					}
				}
			}
			
			for(int i = 0; i < k; i++) { //ノードiから退去
				total_queue[i] += queue[i] * mini_service;
				if( queue[i] > 0) service[i] -= mini_service;
			}
			queue[mini_index] --;
			elapse += mini_service;
			if( queue[mini_index] > 0) service[mini_index] = this.getExponential(mu[mini_index]); //退去後まだ待ち人数がある場合、サービス時間設定
			
			//退去客の行き先決定
			double rand = rnd.nextDouble();
			double sum_rand = 0;
			int destination_index = -1;
			for(int i = 0; i < p[0].length; i++) {
				sum_rand += p[mini_index][i];
				if( rand < sum_rand) {
					destination_index = i;
					break;
				}
			}
			//推移先で待っている客がいなければサービス時間設定(即時サービス)
			if(queue[destination_index] == 0) service[destination_index] = this.getExponential(mu[destination_index]);
			queue[destination_index] ++;
			
		}
		for(int i = 0; i < k; i++) {
			result[0][i] = total_queue[i] / time;
		}
		return result;
		
	}
	
	//指数乱数発生
		public double getExponential(double param) {
			return - Math.log(1 - rnd.nextDouble()) / param;
		}
	
}
