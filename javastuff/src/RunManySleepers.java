
public class RunManySleepers {
	
	public static class Sleeper implements Runnable{
		
		public Sleeper(int id){
			this.id = id;
		}
		
		public int id;

		@Override
		public void run()  {
			try {
				Thread.sleep(1000*10);
				System.out.println("Thread done. " + id);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
		}
		
	}
		
		public static void main(String[] args){
			for (int i = 0; i<10000;i++){
				Thread t = new Thread(new Sleeper(i));
				t.start();
			}
			System.out.println("All threads running");
		}

}
