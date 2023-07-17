import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Employee {
    private JPanel Main;
    private JTextField textName;
    private JTextField textSalary;
    private JTextField textMobile;
    private JButton saveButton;
    private JTable table1;
    private JTextField txtId;
    private JButton searchButton;
    private JButton updateButton;
    private JButton deleteButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    Connection con;
    PreparedStatement pst;

    public void connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/employee","root","");

        }catch (SQLException e){

            System.out.println("data connection error!");

        } catch (ClassNotFoundException e) {

            System.out.println("Database needs to Online!");
        }
    }



    void table_load(){
        try {
            pst = con.prepareStatement("select * from `employee`");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));

        }catch (SQLException e){
            System.out.println("Table are not load!");
        }
    }


    public Employee() {
        connect();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String eName, eSalary, eMoblie;
                eName = textName.getText();
                eSalary = textSalary.getText();
                eMoblie = textMobile.getText();

                try {
                    pst = con.prepareStatement("INSERT INTO `employee`(`eName`, `eSalary`, `eMoblie`) VALUES (?,?,?)");
                    pst.setString(1, eName);
                    pst.setString(2, eSalary);
                    pst.setString(3, eMoblie);
                    pst.executeUpdate();
                    JOptionPane.showConfirmDialog(null, "Record Added!!");
                    table_load();
                    textName.setText("");
                    textSalary.setText("");
                    textMobile.setText("");

                } catch (SQLException ex) {
                    JOptionPane.showConfirmDialog(null,"No data insert. Please Try again!","Data Error",JOptionPane.PLAIN_MESSAGE);
                }

            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    String id = txtId.getText();
                    pst = con.prepareStatement("SELECT `eName`, `eSalary`, `eMoblie` FROM `employee` WHERE eID=?");
                    pst.setString(1,id);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()==true){
                        String name = rs.getString(1);
                        String salary = rs.getString(2);
                        String mobile =rs.getString(3);

                        textName.setText(name);
                        textSalary.setText(salary);
                        textMobile.setText(mobile);
                    }else {
                        textName.setText("");
                        textSalary.setText("");
                        textMobile.setText("");
                        JOptionPane.showConfirmDialog(null,"Invalid Employee Number","Employee Not Found",JOptionPane.PLAIN_MESSAGE);
                    }
                }catch (SQLException ex){

                }


            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eName,eSalary,eMobile,eId;
                eName = textName.getText();
                eSalary = textSalary.getText();
                eMobile = textMobile.getText();
                eId = txtId.getText();

                try {
                    pst = con.prepareStatement("update `employee` set `eName` = ?,`eSalary` =?,`eMoblie` = ? where eID = ?");
                    pst.setString(1,eName);
                    pst.setString(2,eSalary);
                    pst.setString(3,eMobile);
                    pst.setString(4,eId);
                    pst.executeUpdate();
                    JOptionPane.showConfirmDialog(null, "Record Updated!","Updated",JOptionPane.PLAIN_MESSAGE);
                    table_load();
                    textName.setText("");
                    textSalary.setText("");
                    textMobile.setText("");


                }catch (SQLException ex){
                    JOptionPane.showConfirmDialog(null,"Update error. Try again!","Update Error",JOptionPane.PLAIN_MESSAGE);
                }


            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String del;
                del = txtId.getText();
                try {
                    pst = con.prepareStatement("DELETE from `employee` where eID = ?");
                    pst.setString(1,del);
                    pst.executeUpdate();
                    JOptionPane.showConfirmDialog(null,"Deleted!");
                    table_load();
                    textName.setText("");
                    textSalary.setText("");
                    textMobile.setText("");
                    textName.requestFocus();

                }catch (SQLException ex){
                    JOptionPane.showConfirmDialog(null,"Something wrong!");
                }
            }
        });
    }
}