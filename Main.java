import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String commandLine;
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        List<String> history = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder();
        File startDir = new File(System.getProperty("user.dir"));
        pb.directory(startDir);
        Integer i = 1;
        while (true){
            System.out.print(pb.directory()+">");
            commandLine = console.readLine();
            String []cmds = commandLine.split(" ");
            if (commandLine.matches(""))
                continue;
            try{

                if (commandLine.matches("!!")) {
                    if (history.isEmpty() == false){
                        commandLine = history.get(history.size() - 1).substring(i.toString().length() + 1);
                        cmds = commandLine.split(" ");
                        System.out.println(commandLine);
                    }else{
                        System.out.println("there is no cmds in the history");
                        continue;
                    }
                }
                if (commandLine.matches("\\d+")){
                    Integer index = Integer.parseInt(commandLine);
                    if (history.isEmpty()){
                        System.out.println("there is no command in the history");
                        continue;
                    }
                    if (index >= i || index < 1) {
                        System.out.println("Error: Invalid number in the commands history");
                        continue;
                    }
                    commandLine = history.get(index - 1).substring(index.toString().length() + 1);
                    cmds = commandLine.split(" ");
                }
                if (commandLine.equals("history")){
                    for (String h : history)
                        System.out.println(h);
                    continue;
                }
                if (commandLine.contains("cd")){
                    if (commandLine.trim().equals("cd") || commandLine.trim().equals("cd ~")){
                        pb.directory(new File(System.getProperty("user.home")));
                        continue;
                    }
                    else if (commandLine.equals("cd ..")){
                        pb.directory(new File(pb.directory().getParent()));
                        continue;
                    }
                    else {
                        File checkpath = new File(cmds[1]);
                        File newDir;
                        if (checkpath.getPath().toString().contains("/") == false) {
                            newDir = new File(pb.directory() + File.separator + cmds[1]);
                            pb.directory(newDir);
                            continue;
                        } else {
                            if (checkpath.isDirectory() && checkpath.exists() && checkpath.getPath().toString().contains("/")){
                                pb.directory(checkpath);
                                continue;
                            }
                            else{
                                System.out.println("cd: no such file or directory: "+checkpath);
                                continue;
                            }
                        }
                    }
                }
                if (commandLine.contains("|")){
                    String cmd1 = commandLine.substring(0,commandLine.indexOf("|")).trim();
                    String cmd2 = commandLine.substring(commandLine.indexOf("|") + 1).trim();
                    new Pipe(cmd1,cmd2).displayPipeResult();
                    continue;
                }
                pb.command(cmds);
                Process p = pb.start();
                BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s;
                while ((s = bf.readLine()) != null)
                    System.out.println(s);
                bf.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            history.add(i.toString() + " " + commandLine);
            i++;
        }
    }
}