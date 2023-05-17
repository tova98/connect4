module hr.tvz.ntovernic.connect4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.xml;
    requires java.naming;


    opens hr.tvz.ntovernic.connect4 to javafx.fxml;
    exports hr.tvz.ntovernic.connect4;
    exports hr.tvz.ntovernic.connect4.common;
    exports hr.tvz.ntovernic.connect4.client;
    exports hr.tvz.ntovernic.connect4.server;
    exports hr.tvz.ntovernic.connect4.rmi to java.rmi;
    exports hr.tvz.ntovernic.connect4.rmi.chat to java.rmi;
    exports hr.tvz.ntovernic.connect4.rmi.lobby to java.rmi;
}