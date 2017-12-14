class Main {

    //Checks if the correct number of arguments have been passed
    static boolean CLAHandler(String args[]) {

        //Print args info        
        System.out.print("args = { ");
        int argLength = args.length;
        for(int i = 0, lastArg = argLength - 1; i < argLength; i++) {
            System.out.print(args[i] + ((i != lastArg)? ", ": (" }\nargs.length = " + argLength + "\n")));
        }   //end for;

        //Validation
        if ( argLength != 1 ) {
            System.err.println("Please use format <PROGNAME> <KEY>");
            return false;
        }
        return true;
        
    }   //end CLAHandler
    public static void main (String args[]) {
        
        //Handle Command Line Arguments
        CLAHandler(args);

        Dash_bigB dg = new Dash_bigB("PARTY");
        System.out.println("dg.originalText: " + dg.originalText);
        System.out.println("dg.modifiedText: " + dg.modifiedText);

        blockPrint(3, 4, dg.originalText, dg.modifiedText);
        
    }   //end main

    static void blockPrint(int blockLength, int blocksPerLine, String originalText, String modifiedText) {

        //Make private copies to prevent accidental changes
        String tempOriginalText = originalText;
        String tempModifiedText = modifiedText;

        //Padding

        //Make both strings the same length
        while(tempOriginalText.length() < tempModifiedText.length()) {
            tempOriginalText += " ";
        }   //end while

        while(tempModifiedText.length() < tempOriginalText.length() ) {
            tempModifiedText += " ";
        }   //end while

        while (tempOriginalText.length() % blockLength ) {
            tempOriginalText += " ";
            tempModifiedText += " ";
        }   //end while


        for(int currentChar = 0; currentChar < tempOriginalText.length(); currentChar += (blockLength * blocksPerLine)) {
            for(int blockNumber = 0; blockNumber < blocksPerLine; blockNumber++ ) {
                
            }
        }
        
    }   //end blockPrint

}