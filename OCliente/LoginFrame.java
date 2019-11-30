package OCliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;
import static OCliente.ClientConstant.*;

public class LoginFrame extends javax.swing.JFrame {

    //cria formas
    LogInPanel loginP;
    ClientListPanel buddyList;
    ClientManager clientManager;
    ClientStatusListener clientStatus;
    ClientListListener clientListListener;
    ClientWindowListener clientWindowListener;
    String userName;
    int messagingFrameNo=0;
    MessagingFrame [] messagingFrames;

    public LoginFrame(ClientManager getClientManager)
    {
        clientStatus=new myClientStatus();
        clientListListener=new myClientListListener();
        clientWindowListener=new MyClientWindowListener();
        messagingFrames=new MessagingFrame[10000];
        initComponents();
        clientManager=getClientManager;
        myPanel.setLayout(new BorderLayout());
        addLogInPanel();
    }

    void addLogInPanel()
    {
        loginP=new LogInPanel();
        myPanel.add(loginP,BorderLayout.CENTER);
        setVisible(true);

        loginP.but_signin.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                but_signinActionPerformed();
            }
        });
    }

    void addBuddyList()
    {
            buddyList=new ClientListPanel();
            myPanel.add(buddyList,BorderLayout.CENTER);
            setVisible(true);

            buddyList.list_online_clients.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent me)
                {
                    if(me.getClickCount()==2)
                    {
                        String to=(String)buddyList.list_online_clients.getSelectedValue();

                        boolean isWindowOpen=false;
                        for(int i=0;i<messagingFrameNo;i++)
                        {
                            if(messagingFrames[i].to.equalsIgnoreCase(to))
                            {
                                isWindowOpen=true;
                                break;
                            }
                        }
                        if(!isWindowOpen)
                        {
                            messagingFrames[messagingFrameNo]=new MessagingFrame(to,userName,clientManager);
                            messagingFrames[messagingFrameNo].setVisible(true);
                            messagingFrameNo++;
                        }
                    }
                }
            });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myPanel = new javax.swing.JPanel();
        lb_status = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mi_sign_in = new javax.swing.JMenuItem();
        mi_sign_out = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("User");

        javax.swing.GroupLayout myPanelLayout = new javax.swing.GroupLayout(myPanel);
        myPanel.setLayout(myPanelLayout);
        myPanelLayout.setHorizontalGroup(
            myPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 182, Short.MAX_VALUE)
        );
        myPanelLayout.setVerticalGroup(
            myPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );

        lb_status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_status.setText("Você nao está conectado ao servidor");

        jMenu1.setText("User");

        mi_sign_in.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mi_sign_in.setText("Entrar");
        jMenu1.add(mi_sign_in);

        mi_sign_out.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mi_sign_out.setText("Sair");
        mi_sign_out.setEnabled(false);
        mi_sign_out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_sign_outActionPerformed(evt);
            }
        });
        jMenu1.add(mi_sign_out);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ajuda");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lb_status, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(myPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_status)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void but_signinActionPerformed()
    {
        if(!loginP.tf_user_name.getText().isEmpty())
        {
            myPanel.remove(loginP);
            clientManager.connect(clientStatus);
            addBuddyList();
            userName=loginP.tf_user_name.getText();
            setTitle("Chat de POO UDESC ("+userName+")");
            clientManager.sendMessage("login "+userName);
            clientManager.receiveMessage(clientListListener, clientWindowListener);
            mi_sign_in.setEnabled(false);
            mi_sign_out.setEnabled(true);
        }
        else
            javax.swing.JOptionPane.showMessageDialog(this,"Digite seu nome: ");
    }

    private void mi_sign_outActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_sign_outActionPerformed
        // TODO add your handling code here:
        myPanel.remove(buddyList);
        clientManager.sendMessage(DISCONNECT_STRING+" "+userName);
        clientManager.flageoutput=true;
        addLogInPanel();
        mi_sign_out.setEnabled(false);
        mi_sign_in.setEnabled(true);
        clientManager.disconnect(clientStatus);
    }//GEN-LAST:event_mi_sign_outActionPerformed

    class myClientStatus implements ClientStatusListener
    {
        public void loginStatus(String status)
        {
            lb_status.setText(status);
        }
    }

    class myClientListListener implements ClientListListener
    {
        public void addToList(String usersName)
        {
            if(!usersName.equalsIgnoreCase(userName))
                buddyList.list_model.addElement(usersName);
        }
        public void removeFromList(String userName)
        {
            buddyList.list_model.removeElement(userName);
        }
    }

    class MyClientWindowListener implements ClientWindowListener
    {
    
        public void openWindow(String message)
        {
            boolean isWindowOpen=false;
            int openWindowNo=0;

            StringTokenizer tokens=new StringTokenizer(message);
            String to=tokens.nextToken();
            String from=tokens.nextToken();
            for(int i=0;i<messagingFrameNo;i++)
            {
                if(messagingFrames[i].to.equalsIgnoreCase(from))
                {
                    isWindowOpen=true;
                    openWindowNo=i;
                    break;
                }
            }

            if(isWindowOpen)
            {
                messagingFrames[openWindowNo].ta_view_message.append(message.replaceFirst(to,"")+"\n");
            }
            else
            {
                messagingFrames[messagingFrameNo]=new MessagingFrame(from,userName,clientManager);
                messagingFrames[messagingFrameNo].setVisible(true);
                messagingFrames[messagingFrameNo].ta_view_message.append(message.replaceFirst(to,"")+"\n");
                messagingFrameNo++;
            }
        }

        public void closeWindow(String getMessage)
        {
            myPanel.remove(buddyList);
            addLogInPanel();
            mi_sign_out.setEnabled(false);
            mi_sign_in.setEnabled(true);
            lb_status.setText(getMessage);
        }

        public void fileStatus(String filesStatus)
        {
            lb_status.setText(filesStatus);
        }
    }

    /**
    * @param args the command line arguments
    */

    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JLabel lb_status;
    private javax.swing.JMenuItem mi_sign_in;
    private javax.swing.JMenuItem mi_sign_out;
    private javax.swing.JPanel myPanel;

}
