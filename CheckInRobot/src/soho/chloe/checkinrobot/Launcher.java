package soho.chloe.checkinrobot;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.http.ParseException;
import org.htmlparser.util.ParserException;

import soho.chloe.checkinrobot.bean.UserBean;
import soho.chloe.checkinrobot.service.XiamiService;

public class Launcher {
	private static void checkin(JTextArea textArea) {
		XiamiService xiami = new XiamiService();
		for (UserBean user : UserPreferrenceReader.getUserList()) {
			if (user.getSite() == CheckInWebsite.XIAMI) {
				try {
					String response = xiami.goXiami(user);
					textArea.append("用户" + user.getUserName() + response);
				} catch (ParseException | ParserException | IOException e) {
					e.printStackTrace();
					textArea.append("用户" + user.getUserName() + "在虾米签到失败\n"
							+ e.getStackTrace());
				}
			}
			textArea.append("\n");
		}
	}

	public static void main(String[] args) {
		final JFrame notice = new JFrame();
		notice.setTitle("签到Robot - 0.0.1 :: Made by Chloe's Studio");
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(textArea);
		JPanel panel = new JPanel();
		checkin(textArea);
		JButton okButton = new JButton("确定");
		okButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -3738743161637541965L;

			@Override
			public void actionPerformed(ActionEvent e) {
				notice.dispose();
				System.exit(0);
			}
		});
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setSize(300, 150);
		notice.setLocationRelativeTo(null);
		panel.add(scroll);
		panel.add(okButton);
		scroll.setPreferredSize(new Dimension(250, 150));
		notice.add(panel);
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		notice.setVisible(true);
	}
}
