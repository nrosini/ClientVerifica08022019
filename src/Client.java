import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;

public class Client {
    private Socket socket;                  //SOCKET PER L'INVIO E LA RICEZIONE DEI DATI

    public Client() {}

    /**
     * Questo metodo si interfaccia con l'utente al quale chiede quante banconote vuole ritirare, controllando che
     * la richiesta non ecceda i 500€. Una volta fatto ciò invia il numero delle banconote al server.
     */
    public void richiediBanconote() {
        Scanner input = new Scanner(System.in);
        boolean sommaNo = true;
        int banconote = 0;
        while (sommaNo) {
            System.out.println("Quante bancote vuoi prelevare");
            banconote = input.nextInt();
            if (banconote > 5) {
                System.out.println("Non puoi ritirare più di 500€ in una singola sessione");
                sommaNo = true;
            } else {
                sommaNo = false;
            }
        }
        try {
            this.socket = new Socket("localhost",7520);
            DataOutputStream dOut = new DataOutputStream(this.socket.getOutputStream());
            dOut.writeInt(banconote);
            dOut.flush();
        } catch (IOException erroreFlusso) {
            System.err.println("Errore del flusso in uscita");
        }
        this.attendiRisposta();
    }

    /**
     * Questo metodo rimane in attesa dell'esito della transazione dal server.
     */
    private void attendiRisposta() {
        try {
            ServerSocket server = new ServerSocket(1408);
            this.socket = server.accept();
            DataInputStream dIN = new DataInputStream(this.socket.getInputStream());
            System.out.println(dIN.readUTF());
        } catch (IOException erroreFlusso) {
            System.err.println("Errore del flusso in ingresso");
        }

        System.out.println("Grazie per aver usato i nostri ATM");
    }
}
