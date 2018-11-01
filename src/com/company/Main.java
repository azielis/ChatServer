package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    ArrayList strumienieWyjsciowe;

    public static void main(String[] args) {
	// write your code here
        new Main().doRoboty();
    }

    private void doRoboty() {
        strumienieWyjsciowe = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(5000);
            while(true) {
                Socket gniazdoKlienta = serverSock.accept();
                PrintWriter pisarz = new PrintWriter(gniazdoKlienta.getOutputStream());
                strumienieWyjsciowe.add(pisarz);
                rozeslijDoWszystkich("Mamy polaczenie");
                Thread t = new Thread(new ObslugaKlientow(gniazdoKlienta));
                t.start();
                System.out.println("mamy polaczenie");
            }
        } catch(Exception ex) {
            ex.printStackTrace ();
        }
    }

    public class ObslugaKlientow implements Runnable {
        BufferedReader czytelnik;
        Socket gniazdo;
        public ObslugaKlientow(Socket clientSocket) {
            try {
                gniazdo = clientSocket;
                InputStreamReader isReader = new InputStreamReader(gniazdo.getInputStream());
                czytelnik = new BufferedReader(isReader);
            } catch(Exception ex) {ex.printStackTrace();}
        } // koniec konstruktora
        public void run() {
            String wiadomosc;
            try {
                while ((wiadomosc = czytelnik.readLine()) != null) {
                    System.out.println("Odczytano: " + wiadomosc);
                    rozeslijDoWszystkich(wiadomosc);
                } // koniec ptli
            } catch(Exception ex) {ex.printStackTrace();}
        } // koniec metody
    } // koniec klasy wewntrznej

    public void rozeslijDoWszystkich(String message) {
        Iterator it = strumienieWyjsciowe.iterator();
        while(it.hasNext()) {
            try {
                PrintWriter pisarz = (PrintWriter) it.next();
                pisarz.println(message);
                pisarz.flush();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
