package OCliente;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import static OCliente.ClientConstant.*;

public class MessageRecever implements Runnable
{
    ObjectInputStream input;
    boolean keepListening=true;
    ClientListListener clientListListener;
    ClientWindowListener clientWindowListener;
    ClientManager clientManager;
    Socket clientSocket;
    ExecutorService clientExecutor;


    MessageRecever(Socket getClientSocket,ClientListListener getClientListListener ,ClientWindowListener getClientWindowListener,ClientManager getClientManager)
    {
        clientExecutor=Executors.newCachedThreadPool();
        clientManager=getClientManager;
        clientSocket=getClientSocket;
        try
        {
            input = new ObjectInputStream(getClientSocket.getInputStream());
        }
        catch (IOException ex)
        {}
        clientListListener=getClientListListener;
        clientWindowListener=getClientWindowListener;
    }
    public void run()
    {
        String message,name="";
        while(keepListening)
        {
            try
            {
                message = (String) input.readObject();
                System.out.println("Usuário está recebendo "+ message);
                StringTokenizer tokens=new StringTokenizer(message);

                String header=tokens.nextToken();
                if(tokens.hasMoreTokens())
                    name=tokens.nextToken();
                if(header.equalsIgnoreCase("login"))
                {
                    clientListListener.addToList(name);
                }
                else if(header.equalsIgnoreCase(DISCONNECT_STRING))
                {
                    clientListListener.removeFromList(name);
                }
                else if(header.equalsIgnoreCase("server"))
                {
                    clientWindowListener.closeWindow(message);
                }
                else if(name.equalsIgnoreCase("file"))
                {
                    clientWindowListener.fileStatus("Recebendo arquivo...");
                    String address=tokens.nextToken();
                    String fileName=tokens.nextToken();
                    clientExecutor.execute(new FileReceiver(address));
                }
                else
                {
                    clientWindowListener.openWindow(message);
                }
            }
            catch (IOException ex)
            {
                clientListListener.removeFromList(name);
            }
            catch (ClassNotFoundException ex)
            {

            }
        }
    }

    void stopListening()
    {
        keepListening=false;
    }
}
