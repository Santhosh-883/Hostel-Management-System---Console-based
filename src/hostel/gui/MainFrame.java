package hostel.gui;

import hostel.data.DataManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DataManager dataManager;
    private WardenLoginPanel wardenLoginPanel;
    private StudentLoginPanel studentLoginPanel;

    public MainFrame() {
        setTitle("Hostel Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dataManager = new DataManager();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);
        
        BackgroundImagePanel initialPanel = new BackgroundImagePanel("/resources/bg.jpg");
        initialPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("--- Welcome to the Hostel Management System! ---", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setOpaque(false);
        initialPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false); // Make the button panel transparent

        JButton wardenButton = new JButton("Warden Panel");
        JButton studentButton = new JButton("Student Panel");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(wardenButton);
        buttonPanel.add(studentButton);
        buttonPanel.add(exitButton);
        initialPanel.add(buttonPanel, BorderLayout.CENTER);

        wardenLoginPanel = new WardenLoginPanel(dataManager, 
            () -> {
                // On successful login, show warden features
                WardenFeaturesPanel wardenFeaturesPanel = new WardenFeaturesPanel(dataManager, "Admin Warden", () -> {
                    cardLayout.show(mainPanel, "initial");
                    wardenLoginPanel.clearFields();
                    mainPanel.revalidate();
                    mainPanel.repaint();
                });
                mainPanel.add(wardenFeaturesPanel, "wardenFeatures");
                cardLayout.show(mainPanel, "wardenFeatures");
                mainPanel.revalidate();
                mainPanel.repaint();
            }, 
            () -> {
                cardLayout.show(mainPanel, "initial");
                mainPanel.revalidate();
                mainPanel.repaint();
            } // On back, show initial panel
        );

        studentLoginPanel = new StudentLoginPanel(dataManager, 
            (student) -> {
                // On successful login, show student features
                StudentFeaturesPanel studentFeaturesPanel = new StudentFeaturesPanel(dataManager, student, () -> {
                    cardLayout.show(mainPanel, "initial");
                    studentLoginPanel.clearFields();
                    mainPanel.revalidate();
                    mainPanel.repaint();
                });
                mainPanel.add(studentFeaturesPanel, "studentFeatures");
                cardLayout.show(mainPanel, "studentFeatures");
                mainPanel.revalidate();
                mainPanel.repaint();
            }, 
            () -> {
                cardLayout.show(mainPanel, "initial");
                mainPanel.revalidate();
                mainPanel.repaint();
            } // On back, show initial panel
        );

        // Add all panels to the main card layout
        mainPanel.add(initialPanel, "initial");
        mainPanel.add(wardenLoginPanel, "wardenLogin");
        mainPanel.add(studentLoginPanel, "studentLogin");

        add(mainPanel);

        // --- Action Listeners --- //
        wardenButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "wardenLogin");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        studentButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "studentLogin");
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        exitButton.addActionListener(e -> System.exit(0));

        cardLayout.show(mainPanel, "initial");
    }
}
