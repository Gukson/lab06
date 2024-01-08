package org.github.gukson.lab06.model.machine.harvester;

import org.github.gukson.lab06.model.Field;
import org.github.gukson.lab06.model.machine.harvester.Harvester;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class HarvesterGUI extends JFrame {
    private JPanel contentPane;
    private JTextField portInput;
    private JLabel errorLabel, FieldInfoLabel, xLabel, yLabel;
    private JButton unregisterButton, registerButton;
    private Harvester harvester;
    public HarvesterGUI(Harvester harvester) {
        this.harvester = harvester;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 455, 441);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        registerButton = new JButton("Register");
        registerButton.setBounds(20, 313, 85, 53);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Objects.equals(portInput.getText(), "")){
                    errorLabel.setText("Trzeba podać numer portu");
                }
                else {
                    harvester.setPort(Integer.parseInt(portInput.getText()));
                    try {
                        harvester.setKeepHarvesting(true);
                        registerButton.setEnabled(false);
                        unregisterButton.setEnabled(true);
                        portInput.setEnabled(false);

                        harvester.registration();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        contentPane.add(registerButton);

        unregisterButton = new JButton("Unregister");
        unregisterButton.setBounds(116, 315, 85, 53);
        unregisterButton.setEnabled(false);
        unregisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                harvester.setKeepHarvesting(false);
                registerButton.setEnabled(true);
                portInput.setEnabled(true);
                unregisterButton.setEnabled(false);
            }
        });
        contentPane.add(unregisterButton);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("./src/main/resources/data/combine-harvester.png"));
        lblNewLabel.setBounds(20, 20, 256, 256);
        contentPane.add(lblNewLabel);

        FieldInfoLabel = new JLabel("Actual Field");
        FieldInfoLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        FieldInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        FieldInfoLabel.setBounds(307, 31, 100, 16);
        contentPane.add(FieldInfoLabel);

        portInput = new JTextField();
        portInput.setBounds(44, 259, 130, 26);
        contentPane.add(portInput);
        portInput.setColumns(10);

        JLabel portLabel = new JLabel("Port");
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portLabel.setBounds(44, 243, 130, 16);
        contentPane.add(portLabel);

        xLabel = new JLabel("x:");
        xLabel.setBounds(288, 59, 61, 16);
        contentPane.add(xLabel);

        yLabel = new JLabel("y:");
        yLabel.setBounds(288, 82, 61, 16);
        contentPane.add(yLabel);

        errorLabel = new JLabel("");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(20, 373, 419, 16);
        contentPane.add(errorLabel);
    }

    public void updateInfo(Field field){
        xLabel.setText("x: " + field.getX());
        yLabel.setText("y: " + field.getY());
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void portUsed(){
        harvester.setKeepHarvesting(false);
        registerButton.setEnabled(true);
        portInput.setEnabled(true);
        unregisterButton.setEnabled(false);
        errorLabel.setText("Ten port jest już w użyciu, spróbuj inny");
    }
}
