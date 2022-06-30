package lection3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowHTML extends JApplet{

	JButton send = new JButton("Go");
	JLabel l = new JLabel();
	
	public void init(){
		//getContentPane повертає контейнер верхнього рівня
		Container cp = getContentPane();
		//FlowLayout в цьому режимі компоненти розташовуються
		//за попереднім з ліва на право і зверху вниз
		//при цьому виконується центрування копонентів по горизонталі
		cp.setLayout(new FlowLayout());
		send.addActionListener(new Al());
		cp.add(send);
		cp.add(l);
	}
	
	class Al implements ActionListener {
		public void actionPerformed(ActionEvent ae){
			try {
				URL u = new URL("http://distedu.ukma.kiev.ua");
				getAppletContext().showDocument(u);
			} catch (MalformedURLException e) {
				l.setText(e.toString());
			}
		}
	}
	
	public static void main(String[] args) {
		
	}

}
