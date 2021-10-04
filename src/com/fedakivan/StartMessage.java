package com.fedakivan;

public class StartMessage {
    public static void startMessage(){
        String startMessage = """
                Hello.
                To use an app, please, run commands from below list:
                                
                addproduct         - Create Product.
                addorder           - Create Order with a list of the products specified by id.
                updatequantity     - Update Order Entries quantities.
                showview           - Show different views.
                removeproduct      - Remove product by ID / Remove all products only if you enter a password
                help               - Show list of possible commands
                                
                After executing of commands, app will route you with next steps for particular command.
                """;
        System.out.println(startMessage);
    }
}
