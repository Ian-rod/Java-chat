import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientRunnable implements Runnable {

    private Socket socket; 
    private BufferedReader input;
    static Boolean Execute=true;
    ArrayList<String> buffer=new ArrayList<String>();
    public ClientRunnable(Socket s) throws IOException {
        this.socket = s;
        this.input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
    }
    @Override
    public void run() {
            try {
                while(true) {   
                     String response = input.readLine(); 
                    if(Execute){
                        if(buffer.isEmpty())
                        {
                        client.messageview.append(response+"\n");
                        }
                        else{
                            //THE CLIENTS' MESSAGES SHOWS UP FIRST
                            client.messageview.append(response+"\n");
                            for(String s:buffer)
                            {
                                client.messageview.append(s+"\n");
                            }
                            buffer.clear();
                        }
                    } 
                    else{    
                    buffer.add(response);
                    }
                    }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}
