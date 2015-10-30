package closedqueue;

public class ClosedQueue_main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//拠点間距離表
		double d[][] = {{1000,5,8,10,15},{5,1000,3,5,3},{8,3,1000,2,4},{10,5,2,1000,3},{15,3,4,3,1000}};//行を入力
		double p[] = {10,15,5,5,5};//拠点人口表
		double mu[] = {1,2,2,1,2};//サービス率
		double alpha[] = new double[mu.length],alpha1[] = new double[mu.length];
		int N = 10, K = 5;
				
		ClosedQueue_lib clib = new ClosedQueue_lib(1,1,1,d,p, mu, N, K);
		double f[][] = new double [p.length][p.length];
		f = clib.calcGravity();
		
		for(int i = 0; i < p.length; i++){
			for(int j = 0; j < p.length; j++){
				System.out.print("f["+i+"]["+j+"]="+f[i][j]);
			}
			System.out.println("");
		}
		
		//トラフィック方程式を解く準備
		double ff[][] = new double[p.length -1][p.length -1];
		double bb[] = new double[p.length -1];
		for(int i = 0; i < p.length -1; i++){
			for(int j = 0; j < p.length -1; j++){
				if( i == j ) {
					ff[i][j] = f[j + 1][i + 1] - 1; 
				}else {
					ff[i][j] = f[j + 1][i + 1];
				}
			}
		}
		for(int i = 0;i < p.length -1; i++){
			bb[i] = -f[0][i+1];
		}
		for(int i = 0; i < p.length-1; i++){
			for(int j = 0; j < p.length-1; j++){
				System.out.print("ff["+i+"]["+j+"]="+ff[i][j]);
			}
			System.out.println("");
		}
		for(int i = 0; i < p.length-1; i++){
			System.out.println("bb["+i+"]="+bb[i]);
		}
		
		//alphaを求める
		clib.setA(ff);
		clib.setB(bb);
		alpha = clib.calcGauss();
		
		//alphaの配列の大きさが-1になってしまうので、元の大きさのalpha1に入れ直す
		for(int i = 0 ; i < alpha1.length; i++){
			if( i == 0) alpha1[i] = 1;
			else alpha1[i] = alpha[i-1];
		}
		for(int i = 0; i < alpha1.length; i++){
			System.out.println("alpha1["+i+"]="+alpha1[i]);
		}
		
		clib.setAlpha(alpha1);
		
		//平均値解析法で平均系内人数を求める
		clib.calcAverage();
		double[] L = clib.getL();
		double[] R = clib.getR();
		double[] lambda = clib.getLambda();
		
		for(int i = 0;i < K; i++){
			System.out.println("L["+i+"]="+L[i]);
			System.out.println("R["+i+"]="+R[i]);
			System.out.println("Lambda["+i+"]="+lambda[i]);
		}
	}

}
