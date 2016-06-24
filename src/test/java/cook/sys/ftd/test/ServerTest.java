package cook.sys.ftd.test;

import javax.swing.JFrame;

import cook.sys.ftd.messenger.Server;

public class ServerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server mv = new Server();
		mv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mv.strartRunning();
	}

}
