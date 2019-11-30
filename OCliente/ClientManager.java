package OCliente;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import static OCliente.ClientConstant.*;

public class ClientManager
{
    ExecutorService clientExecutor;
    Socket clientSocket ;
    boolean isConnected=false;

    ObjectInputStream input;
    ObjectOutputStream output;
    MessageRecever messageRecever;

    public ClientManager()
    {
        clientExecutor=Executors.newCachedThreadPool();
    }

    public void connect(ClientStatusListener clientStatus)
    {
        try
        {
            if(isConnected)
                return;
            else
            {
                clientSocket=new Socket(SERVER_ADDRESS,SERVER_PORT);
                clientStatus.loginStatus("Você esta conectado a:"+SERVER_ADDRESS);
                isConnected=true;
            }
        }
        catch (UnknownHostException ex)
        {
            clientStatus.loginStatus("Servidor não encontrado");
        }
        catch (IOException ex)
        {
            clientStatus.loginStatus("Servidor não encontrado");
        }
    }

    public void disconnect(ClientStatusListener clientStatus)
    {
        messageRecever.stopListening();
        try
        {
            clientStatus.loginStatus("Você não esta mais conectado ao servidor");
            clientSocket.close();
        }
        catch (IOException ex)
        {
        }
    }

    public void sendMessage(String message)
    {
        clientExecutor.execute(new MessageSender(message));
    }

    public void sendFile(String fileName)
    {
        clientExecutor.execute(new FileSender(fileName));
    }

    boolean flageoutput=true;
    class MessageSender implements Runnable
    {
        String message;
        public MessageSender(String getMessage)
        {
            if(flageoutput)
            {
                try
                {
                    output = new ObjectOutputStream(clientSocket.getOutputStream());
                    output.flush();
                    flageoutput=false;
                }
                catch (IOException ex)
                {
                }
            }
            message=getMessage;
            System.out.println("user esta enviando   "+ message);
        }
        public void run()
        {
            try
            {
                output.writeObject(message);
                output.flush();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void receiveMessage(ClientListListener getClientListListener ,ClientWindowListener getClientWindowListener)
    {
        messageRecever=new MessageRecever(clientSocket,getClientListListener, getClientWindowListener,this);
        clientExecutor.execute(messageRecever);
    }
}
