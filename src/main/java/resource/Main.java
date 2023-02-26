package resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        Re re = new Re();
        re.out();
    }
}

class Re{
    public void out() {
        InputStream resourceAsStream =
                this.getClass().getResourceAsStream("/log4j.properties");
        assert resourceAsStream != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        while (true) {
            try {
                if (!bufferedReader.ready()) break;
                String s = bufferedReader.readLine();
                System.out.println(s+"\tend");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}