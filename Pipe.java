import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Pipe {
    private String cmd1;
    private String cmd2;
    public Pipe(String cmd1,String cmd2){
        setCmd1(cmd1);
        setCmd2(cmd2);
    }
    public String getCmd1() {
        return cmd1;
    }

    public void setCmd1(String cmd1) {
        this.cmd1 = cmd1;
    }

    public String getCmd2() {
        return cmd2;
    }

    public void setCmd2(String cmd2) {
        this.cmd2 = cmd2;
    }

    public void displayPipeResult(){
        try {
            ProcessBuilder pb1 = new ProcessBuilder(getCmd1().split(" "));
            ProcessBuilder pb2 = new ProcessBuilder(getCmd2().split(" "));
            File pb1Output = new File("pb1Output.txt");
            pb1.redirectOutput(pb1Output);
            pb2.redirectInput(pb1Output);
            Process p1 = pb1.start();
            Process p2 = pb2.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null)
                System.out.println(s);
            bufferedReader.close();

            p1.waitFor();
            p2.waitFor();
            Process p3 = new ProcessBuilder("rm -rf pb1Output.txt".split(" ")).start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }



}
