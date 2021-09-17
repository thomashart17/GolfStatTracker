import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javafx.stage.Stage;

import java.sql.*;

import java.text.DecimalFormat;

import java.util.Arrays; 

/**
 * JavaFX application that tracks golf stats
 *
 * @author (Thomas Hart)
 * @version (June 16, 2020)
 */
public class Main extends Application
{
    // Setting up fonts
    Font statFont = new Font(48);
    Font inputFont = new Font(24);
    
    // Declaring variables
    Label user, roundStat, girStat, firStat, pphStat, avgScoreStat, parStat, birdieStat, eagleStat, hcStat;
    Label[] hole;
    TextArea[] par, score, putts;
    CheckBox[] girBox, firBox;
    TextArea courseRatingInput, slopeRatingInput;
    Scene statScene, inputScene;
    Button inputButton, statButton, submit;
    
    // Creating stage
    private Stage stage;
    
    // Database URL
    final String DB_URL = "jdbc:derby:GolfStatDB";
   
    // GUI for input screen
    private Scene buildInputGUI()
    {
        // Creating gridpane
        GridPane gp = new GridPane();
        
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        for (int i=0; i<18; i++)
        {
            gp.getColumnConstraints().add(new ColumnConstraints(50));
        }
        
        gp.getRowConstraints().add(new RowConstraints(59));
        for (int i=0; i<6; i++)
        {
            gp.getRowConstraints().add(new RowConstraints(70));
        }
        gp.getRowConstraints().add(new RowConstraints(95));
        
        // Creating top bar
        Rectangle topBar = new Rectangle(1000, 60, Color.BLACK);
        gp.add(topBar, 0, 0, 19, 1);
        
        // Creating label for user
        user = new Label(" Thomas Hart");
        user.setFont(inputFont);
        user.setTextFill(Color.WHITE);
        gp.add(user, 0, 0, 2, 1);
        
        // Creating hole label
        Label holeLabel = new Label(" Hole:");
        holeLabel.setFont(inputFont);
        gp.add(holeLabel, 0, 1);
        
        // Creating par label
        Label parLabel = new Label(" Par:");
        parLabel.setFont(inputFont);
        gp.add(parLabel, 0, 2);
        
        // Creating score label
        Label scoreLabel = new Label(" Score:");
        scoreLabel.setFont(inputFont);
        gp.add(scoreLabel, 0, 3);
        
        // Creating gir label
        Label girLabel = new Label(" GIR:");
        girLabel.setFont(inputFont);
        gp.add(girLabel, 0, 4);
        
        // Creating fir label
        Label firLabel = new Label(" FIR:");
        firLabel.setFont(inputFont);
        gp.add(firLabel, 0, 5);
        
        // Creating putts label
        Label puttsLabel = new Label(" Putts:");
        puttsLabel.setFont(inputFont);
        gp.add(puttsLabel, 0, 6);
        
        // Creating course rating label
        Label courseRatingLabel = new Label("Course Rating:");
        courseRatingLabel.setFont(inputFont);
        gp.add(courseRatingLabel, 1, 7, 4, 1);
        
        // Creating course rating label
        Label slopeRatingLabel = new Label("Slope Rating:");
        slopeRatingLabel.setFont(inputFont);
        gp.add(slopeRatingLabel, 8, 7, 4, 1);
        
        // Creating hole labels
        hole = new Label[18];
        for (int i=0; i<18; i++)
        {
            hole[i] = new Label(Integer.toString(i+1));
            hole[i].setFont(inputFont);
            gp.add(hole[i], i+1, 1);
            gp.setHalignment(hole[i], HPos.CENTER);
        }
       
        // Creating par text areas
        par = new TextArea[18];
        for (int i=0; i<18; i++)
        {
            par[i] = new TextArea();
            par[i].setFont(inputFont);
            gp.add(par[i], i+1, 2);
        }
        
        // Creating score text areas
        score = new TextArea[18];
        for (int i=0; i<18; i++)
        {
            score[i] = new TextArea();
            score[i].setFont(inputFont);
            gp.add(score[i], i+1, 3);
        }
        
        // Creating putts text areas
        putts = new TextArea[18];
        for (int i=0; i<18; i++)
        {
            putts[i] = new TextArea();
            putts[i].setFont(inputFont);
            gp.add(putts[i], i+1, 6);
        }
        
        // Creating GIR checkboxes
        girBox = new CheckBox[18];
        for (int i=0; i<18; i++)
        {
            girBox[i] = new CheckBox();
            gp.add(girBox[i], i+1, 4);
            gp.setHalignment(girBox[i], HPos.CENTER);
        }
        
        // Creating FIR checkboxes
        firBox = new CheckBox[18];
        for (int i=0; i<18; i++)
        {
            firBox[i] = new CheckBox();
            gp.add(firBox[i], i+1, 5);
            gp.setHalignment(firBox[i], HPos.CENTER);
        }
        
        // Creating text area for course rating
        courseRatingInput = new TextArea();
        courseRatingInput.setFont(inputFont);
        gp.add(courseRatingInput, 5, 7, 2, 1);
        
        // Creating text area for slope rating
        slopeRatingInput = new TextArea();
        slopeRatingInput.setFont(inputFont);
        gp.add(slopeRatingInput, 11, 7, 2, 1);
        
        // Creating button to return to stat screen
        statButton = new Button("Stats");
        statButton.setFont(inputFont);
        gp.add(statButton, 16, 0, 3, 1);
        gp.setHalignment(statButton, HPos.RIGHT);
        statButton.setOnAction(this::buttonPressed);
        
        // Creating submit button
        submit = new Button("Submit");
        submit.setFont(inputFont);
        gp.add(submit, 15, 7, 3, 1);
        gp.setHalignment(submit, HPos.RIGHT);
        submit.setOnAction(this::buttonPressed);
        
        // Setting up scene
        Scene scene = new Scene(gp);       
        return scene;
    }
    
    // GUI for stat screen
    private Scene buildStatGUI()
    {
        // Creating gridpane to hold GUI elements
        GridPane gp = new GridPane();
        
        for (int i=0; i<4; i++)
        {
            gp.getColumnConstraints().add(new ColumnConstraints(250));
        }
           
        gp.getRowConstraints().add(new RowConstraints(59));
        for (int i=0; i<4; i++)
        {
            gp.getRowConstraints().add(new RowConstraints(93.75));
        }
        for (int i=0; i<4; i++)
        {
            gp.getRowConstraints().add(new RowConstraints(60));
        }
        
        // Creating top bar
        Rectangle topBar = new Rectangle(1000, 60, Color.BLACK);
        gp.add(topBar, 0, 0, 4, 1);
        
        // Adding background image
        Image bg = new Image("bg.jpg");
        ImageView bgView = new ImageView(bg);
        gp.add(bgView, 0, 1, 4, 4);
        
        // Creating box for handicap
        Rectangle hcBox = new Rectangle(150, 200, Color.WHITE);
        gp.add(hcBox, 1, 2, 2, 2);
        gp.setHalignment(hcBox, HPos.CENTER);
        gp.setValignment(hcBox, VPos.CENTER);
        
        // Creating label for user
        user = new Label(" Thomas Hart");
        user.setFont(inputFont);
        user.setTextFill(Color.WHITE);
        gp.add(user, 0, 0, 2, 1);
        
        // Creating labels for rounds
        Label rounds = new Label(" Rounds:");
        rounds.setFont(statFont);
        gp.add(rounds, 0, 5);
        
        roundStat = new Label();
        roundStat.setFont(statFont);
        gp.add(roundStat, 1, 5);
        gp.setHalignment(roundStat, HPos.RIGHT);
        
        // Creating labels for GIR
        Label gir = new Label(" GIR:");
        gir.setFont(statFont);
        gp.add(gir, 0, 6);
        
        girStat = new Label();
        girStat.setFont(statFont);
        gp.add(girStat, 1, 6);
        gp.setHalignment(girStat, HPos.RIGHT);
        
        // Creating labels for FIR
        Label fir = new Label(" FIR:");
        fir.setFont(statFont);
        gp.add(fir, 0, 7);
        
        firStat = new Label();
        firStat.setFont(statFont);
        gp.add(firStat, 1, 7);
        gp.setHalignment(firStat, HPos.RIGHT);
        
        // Creating labels for putts per hole
        Label pph = new Label(" PPH:");
        pph.setFont(statFont);
        gp.add(pph, 0, 8);
        
        pphStat = new Label();
        pphStat.setFont(statFont);
        gp.add(pphStat, 1, 8);
        gp.setHalignment(pphStat, HPos.RIGHT);
        
        // Creating Labels for average score
        Label avgScore = new Label(" Avg Score:");
        avgScore.setFont(statFont);
        gp.add(avgScore, 2, 5);
        
        avgScoreStat = new Label();
        avgScoreStat.setFont(statFont);
        gp.add(avgScoreStat, 3, 5);
        gp.setHalignment(avgScoreStat, HPos.RIGHT);
        
        // Creating labels for pars
        Label pars = new Label(" Pars:");
        pars.setFont(statFont);
        gp.add(pars, 2, 6);
        
        parStat = new Label();
        parStat.setFont(statFont);
        gp.add(parStat, 3, 6);
        gp.setHalignment(parStat, HPos.RIGHT);
        
        // Creating Labels for birdies
        Label birdies = new Label(" Birdies:");
        birdies.setFont(statFont);
        gp.add(birdies, 2, 7);
        
        birdieStat = new Label();
        birdieStat.setFont(statFont);
        gp.add(birdieStat, 3, 7);
        gp.setHalignment(birdieStat, HPos.RIGHT);
        
        // Creating Labels for eagles
        Label eagles = new Label(" Eagles:");
        eagles.setFont(statFont);
        gp.add(eagles, 2, 8);
        
        eagleStat = new Label();
        eagleStat.setFont(statFont);
        gp.add(eagleStat, 3, 8);
        gp.setHalignment(eagleStat, HPos.RIGHT);
        
        // Creating labels for handicap
        Label hc = new Label("Handicap:");
        hc.setFont(new Font(30));
        gp.add(hc, 1, 2, 2, 1);
        gp.setHalignment(hc, HPos.CENTER);
        
        hcStat = new Label();
        hcStat.setFont(new Font(60));
        gp.add(hcStat, 1, 3, 2, 1);
        gp.setHalignment(hcStat, HPos.CENTER);
        
        gp.setValignment(hcStat, VPos.TOP);
        
        // Creating button to go to input screen
        inputButton = new Button("Input");
        inputButton.setFont(inputFont);
        gp.add(inputButton, 3, 0);
        gp.setHalignment(inputButton, HPos.RIGHT);
        inputButton.setOnAction(this::buttonPressed);
        
        // Creating and returning scene
        Scene scene = new Scene(gp);
        return scene;
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        // Creating scenes
        inputScene = buildInputGUI();
        statScene = buildStatGUI();
        
        // Updating stats
        updateStats();
        
        // Allowing stage to be modified outside start method
        this.stage = primaryStage;
        
        // Locking size of stage
        primaryStage.setResizable(false);
        
        // Setting up stage
        primaryStage.setTitle("Golf Stats");
        primaryStage.setScene(statScene);
        primaryStage.show();
    }
    
    // Method to change screen
    public void changeScreen(String screen)
    {
        if (screen.equals("statScene"))
        {
            stage.setScene(statScene);
        }
        if (screen.equals("inputScene"))
        {
            stage.setScene(inputScene);
        }
    }
    
    // Event handler for button press
    private void buttonPressed(ActionEvent event)
    {
        // Finding what button was pressed
        Button button = (Button) event.getSource();
        String buttonText = button.getText();
        
        // Stat button pressed
        if (buttonText.equals("Stats"))
        {
            // Returning to stat screen
            changeScreen("statScene");
        }
        
        // Input button pressed
        if (buttonText.equals("Input"))
        {
            // Going to input screen
            changeScreen("inputScene");
        }
        
        // Submit button pressed
        if (buttonText.equals("Submit"))
        {
            // Checking if input is valid
            if (checkInput())
            {
                // Getting stats from input fields
                getStats();
                
                // Updating stats                
                updateStats();
                
                // Clearing input screen
                clearInput();
                
                // Switching back to stats screen
                changeScreen("statScene");
            }
        }
    }
    
    // Method to calculate handicap
    private double hc()
    {
        // Initializing variables
        double hc, par, score;
        double totalHC = 0;
        double output = 0;
        double rounds = 0;
        
        try
        {            
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT Par, Score FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                par = result.getInt("Par");
                score = result.getInt("Score");
                hc = (score-par)*0.96;
                totalHC += hc;
                rounds ++;
            }
            
            output = totalHC/rounds;
            
            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
        
        // Returning value
        return output;
    }
    
    // Method to get total rounds
    private int rounds()
    {
        // Initializing variables
        int rounds = 0;
        
        try
        {
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT Par FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                rounds ++;
            }
            
            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
       
        // Returning value
        return rounds;
    }
    
    // Method to find total GIR
    private double gir()
    {
        // Initializing variables
        double output = 0;
        double rounds = 0;
        double greensHit = 0;
        
        try
        {
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT GreensHit FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                greensHit += result.getDouble("GreensHit");
                rounds ++;
            }
            
            // Calculating output
            output = greensHit / (18*rounds) * 100;
            
            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
       
        // Returning value
        return output;
    }
   
    // Method to find total FIR
    private double fir()
    {
        // Initializing variables
        double output = 0;
        double fairwaysHit = 0;
        double fairwaysPotential = 0;
        
        try
        {
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT FairwaysHit, FairwaysPotential FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                fairwaysHit += result.getDouble("FairwaysHit");
                fairwaysPotential += result.getDouble("FairwaysPotential");
            }
            
            // Calculating output
            output = fairwaysHit / fairwaysPotential * 100;
            
            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
       
        // Returning value
        return output;
    }
    
    // Method to find total PPH
    private double pph()
    {
        // Initializing variables
        double output = 0;
        double rounds = 0;
        double puttsTotal = 0;
        
        try
        {
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT PuttsTotal FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                puttsTotal += result.getDouble("PuttsTotal");
                rounds ++;
            }
            
            // Calculating output
            output = puttsTotal / (18*rounds);
            
            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
       
        // Returning value
        return output;
    }
    
    // Method to find average score
    private double avgScore()
    {
        // Initializing variables
        double output = 0;
        double totalScore = 0;
        double rounds = 0;
        
        try
        {      
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT Score FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                totalScore += result.getDouble("Score");
                rounds ++;
            }
            
            // Calculating output
            output = totalScore / rounds;
            
            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
        
        // Returning value
        return output;
    }
    
    // Method to calculate total pars
    private int pars()
    {
        // Declaring variables
        int totalPars = 0;
        
        try
        {      
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT Pars FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                totalPars += result.getInt("Pars");
            }

            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
        
        // Returning value
        return totalPars;
    }
    
    // Method to calculate total birdies
    private int birdies()
    {
        // Declaring variables
        int totalBirdies = 0;
        
        try
        {      
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT Birdies FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                totalBirdies += result.getInt("Birdies");
            }

            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
        
        // Returning value
        return totalBirdies;
    }
    
    // Method to calculate total eagles
    private int eagles()
    {
        // Declaring variables
        int totalEagles = 0;
        
        try
        {      
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = "SELECT Eagles FROM Stats";
            
            // Getting data from database
            ResultSet result = stmt.executeQuery(sql);
            
            while (result.next())
            {
                totalEagles += result.getInt("Eagles");
            }

            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
        
        // Returning value
        return totalEagles;
    }
    
    // Method to update stats
    private void updateStats()
    {
        // Rounding decimals
        DecimalFormat percentage = new DecimalFormat("##.#");
        DecimalFormat putts = new DecimalFormat("#.##");
        
        // Updating handicap
        hcStat.setText(String.valueOf(percentage.format(hc())));
        
        // Updating rounds
        roundStat.setText(String.valueOf(rounds()));
        
        // Updating GIR
        girStat.setText(String.valueOf(percentage.format(gir()) + "%"));
        
        // Updating FIR
        firStat.setText(String.valueOf(percentage.format(fir()) + "%"));
        
        // Updating PPH
        pphStat.setText(String.valueOf(putts.format(pph())));
        
        // Updating avg score
        avgScoreStat.setText(String.valueOf(percentage.format(avgScore())));
        
        // Updating pars
        parStat.setText(String.valueOf(pars()));
        
        // Updating birdies
        birdieStat.setText(String.valueOf(birdies()));
        
        // Updating eagles
        eagleStat.setText(String.valueOf(eagles()));
    }
    
    // Method to get stats from input screen and put them in database
    private void getStats()
    {
        // Defining variables
        double courseRating, slopeRating;
        int totalPar = 0;
        int totalScore = 0;
        int greensHit =0;
        int fairwaysHit = 0;
        int fairwaysPotential = 0;
        int pars = 0;
        int birdies = 0;
        int eagles = 0;
        int totalPutts = 0;
        String crStr, srStr, parStr, scoreStr, greenStr, fStr, fpStr, parsStr, birdieStr, eagleStr, puttStr;
        
        // Getting course and slope rating
        courseRating = Double.valueOf(courseRatingInput.getText());
        slopeRating = Double.valueOf(slopeRatingInput.getText());
        crStr = String.valueOf(courseRating);
        srStr = String.valueOf(slopeRating);
        
        // Getting par of the course
        for (int i=0; i<18; i++)
        {
            totalPar += Integer.valueOf(par[i].getText());
        }
        parStr = String.valueOf(totalPar);
        
        // Getting total score for the round
        for (int i=0; i<18; i++)
        {
            totalScore += Integer.valueOf(score[i].getText());
        }
        scoreStr = String.valueOf(totalScore);
        
        // Getting greens hits
        for (int i=0; i<18; i++)
        {
            if (girBox[i].isSelected()) greensHit++;
        }
        greenStr = String.valueOf(greensHit);
        
        // Getting fairways hit and potential
        for (int i=0; i<18; i++)
        {
            if (Integer.valueOf(par[i].getText()) == 3) continue;
            else
            {
                fairwaysPotential++;
                if (firBox[i].isSelected()) fairwaysHit++;
            }
        }
        fStr = String.valueOf(fairwaysHit);
        fpStr = String.valueOf(fairwaysPotential);
        
        // Getting pars, birdies and eagles
        for (int i=0; i<18; i++)
        {
            if (Integer.valueOf(par[i].getText()) - Integer.valueOf(score[i].getText()) == 0) pars++;
            if (Integer.valueOf(par[i].getText()) - Integer.valueOf(score[i].getText()) == 1) birdies++;
            if (Integer.valueOf(par[i].getText()) - Integer.valueOf(score[i].getText()) == 2) eagles++;
        }
        parsStr = String.valueOf(pars);
        birdieStr = String.valueOf(birdies);
        eagleStr = String.valueOf(eagles);
        
        // Getting total putts
        for (int i=0; i<18; i++)
        {
            totalPutts += Integer.valueOf(putts[i].getText());
        }
        puttStr = String.valueOf(totalPutts);
        
        // Putting in database
        try
        {      
            // Connecting to database
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Creating Statement object
            Statement stmt = conn.createStatement();
            
            // Creating SQL statement
            String sql = ("INSERT INTO Stats VALUES (" +
                          crStr + ", "+
                          srStr + ", "+
                          parStr + ", "+
                          scoreStr + ", "+
                          fStr + ", "+
                          fpStr + ", "+
                          greenStr + ", "+
                          parsStr + ", "+
                          birdieStr + ", "+
                          eagleStr + ", "+
                          puttStr + ")");

                          
            // Executing SQL statement
            stmt.execute(sql);

            // Closing connection
            stmt.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
        }
    }
    
    // Method to clear input fields
    private void clearInput()
    {
        // Clearing input
        for (int i=0; i<18; i++)
        {
            par[i].setText("");
            score[i].setText("");
            putts[i].setText("");
            firBox[i].setSelected(false);
            girBox[i].setSelected(false);
        }
        courseRatingInput.setText("");
        slopeRatingInput.setText("");
    }
    
    // Method to check if input is fully filled and possible
    private boolean checkInput()
    {
        // Cheking input
        for (int i=0; i<18; i++)
        {
            if ((Integer.valueOf(par[i].getText()) != 3) && (Integer.valueOf(par[i].getText()) != 4) && (Integer.valueOf(par[i].getText()) != 5)) return false;
            if (!isNumeric(score[i].getText())) return false;
            if (!isNumeric(putts[i].getText())) return false;
            if (Integer.valueOf(putts[i].getText()) >= Integer.valueOf(score[i].getText())) return false;
        }
        if (!isNumeric(courseRatingInput.getText())) return false;
        if (!isNumeric(slopeRatingInput.getText())) return false;

        return true;
    }
    
    // Checking if string is a number 
    // Taken from: https://www.baeldung.com/java-check-string-number
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
