package OCliente;

import OCliente.ClientManager;
import OCliente.LoginFrame;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        ClientManager clientManager=new ClientManager();
        LoginFrame loginFrame=new LoginFrame(clientManager);
        loginFrame.setVisible(true);
    }

}
