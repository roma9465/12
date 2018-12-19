package com.example.roma9465.projekt1;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;


public class TempFetch {

    public TempFetch() {

    }

    public String getTemp() {
        String text = run("tdtool -l").toString();
        return text.split("\\n")[9].split("\\t")[3].substring(0, text.length() - 1);


    }
    public StringBuilder run (String command) {
        String hostname = "130.237.177.204";
        String username = "pi";
        String password = "raspberry";

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuilder text = new StringBuilder();
        try
        {


            Connection conn = new Connection(hostname); //init connection
            conn.connect(); //start connection to the hostname
            boolean isAuthenticated =
                    conn.authenticateWithPassword(username, password);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");

            Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(stdout)); //reads text


            while (true)
            {
                String line = br.readLine(); // read line
                text.append(line + "\n");
                if (line == null)
                    break;
                System.out.println(line);

                //skriver ut värdena från sensorerna i run
            }

            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close(); // Close this session
            conn.close();
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            System.exit(2);
        }
        return text;
    }
}


