package autobackup;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class MessageBox extends JFrame{
	JLabel label = new JLabel();
	
	
	public MessageBox(String message){
		this.setTitle("AutoBackup");
		this.setLayout(null);
		
		label.setText("<html><body style='text-align:center;'>"+message.replaceAll("\n", "<br>")+"</body></html>");
		label.setLocation(10, 10);
		label.setSize(label.getPreferredSize());
		this.add(label);
		
		this.setSize(label.getWidth()+25, label.getHeight()+50);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
}
