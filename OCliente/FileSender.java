package OCliente;

import java.net.*;
import java.io.*;

public class FileSender implements Runnable
{
    String fileName;
  FileSender(String file)
  {
      fileName=file;
    // socket criação
    
    }

  public void run()
  {
        try {
            ServerSocket servsock = new ServerSocket(13267);
            System.out.println("Aguarde..");
            Socket sock = servsock.accept();
            System.out.println("Conexão aceita : " + sock);
            // envia o arquivo
            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            OutputStream os = sock.getOutputStream();
            System.out.println("Enviando...");
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            sock.close();
        } 
        catch (IOException ex)
        {
        }
      }
}
