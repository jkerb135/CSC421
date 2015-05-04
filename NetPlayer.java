import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Josh on 4/22/2015.
 */
public class NetPlayer extends Player{
    private DatagramSocket socket;
    private Thread clientThread;
    public InetAddress ipAddress;
    public int port;
    public String username;

    /**
     * Constructs the player with the defined playerName and sets won round
     * to false
     *
     * @param playerName the players name
     */
    protected NetPlayer(String playerName, InetAddress ipAddress, int port) {
        super(playerName);
        this.username = playerName;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getUsername(){
        return this.username;
    }

    @Override
    public boolean doTurn(Deck theDeck) throws IOException {
        return false;
    }

    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
