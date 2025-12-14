public class Compiler {
    public static void run(){
        try{
            Lang.second.setLength(0);
        } catch (Exception _){}
        while(true){
            String w = Files.readWord(Lang.first);
            switch(w){
                case "+", "-", "out", "*", "jump", "delay", "quit":{
                    switch(w){
                        case "+" -> Files.writeWithDebug(Lang.second, 0);
                        case "-" -> Files.writeWithDebug(Lang.second, 1);
                        case "out" -> Files.writeWithDebug(Lang.second, 3);
                        case "*" -> Files.writeWithDebug(Lang.second, 6);
                        case "jump" -> Files.writeWithDebug(Lang.second, 7);
                        case "delay" -> Files.writeWithDebug(Lang.second, 8);
                        case "quit" -> Files.writeWithDebug(Lang.second, 10);
                    }
                    Files.writeWithDebug(Lang.second, switchTypeOfData(Files.readWord(Lang.first), 1));
                    Files.writeWithDebug(Lang.second, Integer.parseInt(Files.readWord(Lang.first)));
                    break;
                }
                case "save": Files.writeWithDebug(Lang.second, 9); break;
                case "draw": Files.writeWithDebug(Lang.second, 11); break;
                case "load": Files.writeWithDebug(Lang.second, 12); break;
                case "=", "in":{
                    switch(w){
                        case "=" -> Files.writeWithDebug(Lang.second, 2);
                        case "in" -> Files.writeWithDebug(Lang.second, 4);
                    }
                    Files.writeWithDebug(Lang.second, switchTypeOfData(Files.readWord(Lang.first), 1));
                    Files.writeWithDebug(Lang.second, Integer.parseInt(Files.readWord(Lang.first)));
                    Files.writeWithDebug(Lang.second, switchTypeOfData(Files.readWord(Lang.first), 3));
                    Files.writeWithDebug(Lang.second, Integer.parseInt(Files.readWord(Lang.first)));
                    break;
                }
                case "if":{
                    Files.writeWithDebug(Lang.second, 5);
                    Files.writeWithDebug(Lang.second, switchTypeOfData(Files.readWord(Lang.first), 1));
                    Files.writeWithDebug(Lang.second, Integer.parseInt(Files.readWord(Lang.first)));
                    switch(Files.readWord(Lang.first)){
                        case "=": Files.writeWithDebug(Lang.second, 0); break;
                        case ">": Files.writeWithDebug(Lang.second, 1); break;
                    }
                    Files.writeWithDebug(Lang.second, switchTypeOfData(Files.readWord(Lang.first), 4));
                    Files.writeWithDebug(Lang.second, Integer.parseInt(Files.readWord(Lang.first)));
                    Files.writeWithDebug(Lang.second, switchTypeOfData(Files.readWord(Lang.first), 6));
                    Files.writeWithDebug(Lang.second, Integer.parseInt(Files.readWord(Lang.first)));
                    break;
                }
                case ";": Files.readLine(Lang.first); break;
                case "\n", "\t": break;
                case "": Files.close(Lang.first); Files.close(Lang.second); System.exit(0);
                default: Err.sendErrAndExit("unknown command \"" + w + "\"", 1);
            }
            if(Lang.mode == 1) System.out.println();
        }
    }
    private static int switchTypeOfData(String w, int place){
        int res = -1;
        switch(w){
            case "b" -> res = 0;
            case "c" -> res =  1;
            case "cc" -> res =  2;
        }
        if(res == -1) Err.sendErrAndExit("unknown type of data \"" + w + "\"", place);
        return res;
    }
}
