
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author whoami
 */
public class GUIChatClient extends Application{
    private static Stage mainStage;

    private static BorderPane createLoginLabelsPane(){
        BorderPane loginLabels = new BorderPane();
        
        loginLabels.setTop(new Label("Username"));
        loginLabels.setBottom(new Label("Server IP"));
        
        return loginLabels;
    }
    
    private static BorderPane createLoginTextFieldsPane(){
        BorderPane loginTextFieldsPane = new BorderPane();
        TextField usernameTextField = new TextField();
        TextField serverIPTextField = new TextField();
        
        loginTextFieldsPane.setTop(usernameTextField);
        loginTextFieldsPane.setBottom(serverIPTextField);
        
        return loginTextFieldsPane;
    }
    
    private static BorderPane createLoginFieldsPane(){
        BorderPane loginFields = new BorderPane();
        
        loginFields.setLeft(createLoginLabelsPane());
        loginFields.setRight(createLoginTextFieldsPane());
        
        return loginFields;
    }
    
    private static HBox createLoginButtonsPane(){
        HBox loginButtonPane = new HBox();
        Button connectButton = new Button("Connect");
        Button cancelButton = new Button("Cancel");
        
        cancelButton.setOnAction((ActionEvent t) -> {
            mainStage.close();
        });
        
        loginButtonPane.getChildren().addAll(cancelButton, connectButton);
        
        return loginButtonPane;
    }

    private static BorderPane createGUILoginClient() {
        BorderPane guiLoginClient = new BorderPane();
        
        guiLoginClient.setTop(createLoginFieldsPane());
        guiLoginClient.setBottom(createLoginButtonsPane());
        
        guiLoginClient.getBottom().prefWidth(guiLoginClient.getWidth());
        
        return guiLoginClient;
    }
    
    private static MenuBar createChatMenuPane(){
        MenuItem logoutMenuItem = new MenuItem("Logout");
        MenuItem exitMenuItem = new MenuItem("Exit");
        Menu fileMenu = new Menu("File");
        MenuBar menuBar = new MenuBar(fileMenu);
        
        fileMenu.getItems().addAll(logoutMenuItem, exitMenuItem);
        
        logoutMenuItem.setOnAction((ActionEvent e) -> {
            logout();
        });
        
        exitMenuItem.setOnAction((ActionEvent e) -> {
            exit();
        });
    
        return menuBar;
    }
    
    private static BorderPane createChatUserListPane(){
        BorderPane chatUserListPane = new BorderPane();
        ListView chatUserListView = new ListView();
        
        chatUserListPane.setCenter(chatUserListView);
    
        return chatUserListPane;
    }
    
    private static BorderPane createChatPromptPane(){
        BorderPane chatPromptPane = new BorderPane();
        TextField chatPromptText = new TextField();
        Button sendMessageButton = new Button("Send");
        
        chatPromptPane.setCenter(chatPromptText);
        chatPromptPane.setRight(sendMessageButton);
    
        return chatPromptPane;
    }
    
    private static BorderPane createGUIChatClient(){
        BorderPane guiChatClient = new BorderPane();
        TextArea chatTextArea = new TextArea();
        
        guiChatClient.setTop(createChatMenuPane());
        guiChatClient.setCenter(chatTextArea);
        guiChatClient.setRight(createChatUserListPane());
        guiChatClient.setBottom(createChatPromptPane());
        
        return guiChatClient;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        mainStage = primaryStage;
        Scene scene = new Scene(createGUIChatClient());
        //Scene scene = new Scene(createGUILoginClient());
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("RMI Chat Client");
        primaryStage.show();
    }
    
    private static void logout(){
        
    }
    
    private static void exit(){
        
    }
    
}
