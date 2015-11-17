package creche;
//@author Daniel Hickey
//@version 1st March 2013
//This is the HappyWaysCrecheApp which is holds the main method and the applications 
//main features. 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class HappyWaysCrecheApp extends JFrame implements ActionListener, KeyListener
{

    private JTextField tfOption = new JTextField();
    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private List<Person> creche = new ArrayList<Person>(); 
    //private static final NumberFormat nf = NumberFormat.getCurrencyInstance();

    public HappyWaysCrecheApp()
    {
        this.setTitle("Happyways Creche Data Entry");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(400, 200));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.add(new JLabel(new ImageIcon(this.getClass().getResource("logo.png"))), BorderLayout.NORTH);

        JPanel pnlOptions = new JPanel(new GridLayout(10, 1, 0, 0));
        pnlOptions.add(new JLabel("1. Register A Person"));
        pnlOptions.add(new JLabel("2. Register A Child with a Parent"));
        pnlOptions.add(new JLabel("3. List All Parents & Children"));
        pnlOptions.add(new JLabel("4. List All Children of Parent ?? (based on ID)"));
        pnlOptions.add(new JLabel("5. Calculate Weekly Income for All Children"));
        pnlOptions.add(new JLabel("6. Search for a Person (based on First Name & Last Name"));
        pnlOptions.add(new JLabel("7. Remove a Person (based on ID)"));
        pnlOptions.add(new JLabel("8. Exit System"));
        pnlOptions.add(new JLabel("Enter Option :"));
        tfOption.addKeyListener(this);
        pnlOptions.add(tfOption);
        mainPanel.add(pnlOptions, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        pnlButtons.add(btnOk);
        pnlButtons.add(new JLabel("       "));
        pnlButtons.add(btnCancel);
        mainPanel.add(pnlButtons, BorderLayout.SOUTH);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(new JLabel(""), BorderLayout.NORTH);
        this.add(new JLabel(""), BorderLayout.WEST);
        this.add(new JLabel(""), BorderLayout.SOUTH);
        this.add(new JLabel(""), BorderLayout.EAST);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if(src == btnOk)
        {
            try
            {
                int choice = Integer.parseInt(tfOption.getText());
                switch (choice)
                {
                    case 1: registerAPerson(); break;
                    case 2: registerChildWithParent(); break;
                    case 3: listPersons(creche); break;
                    case 4: listChildrenOfParent(); break;
                    case 5: calcTotalWeeklyFee(); break;
                    case 6: findPerson(); break;
                    case 7: removeAPerson(); break;
                    case 8: System.exit(0); break;
                    default: throw new Exception();
                }
            }
            catch(Exception e1)
            {
                e1.printStackTrace();
                showError("Invalid Option value!");
            }
        }
        else if(src == btnCancel)
        {
            System.exit(0);
        }
    }

//    @Override
//    public Insets getInsets() {
//        return new Insets(40, 20, 20, 20);
//    }
//
    public void registerAPerson ()
    {
        String personType = getOptionValueFromUser("Person Type", Parent.PERSON_TYPE, Child.PERSON_TYPE);
        if(personType == null) return;
        Person person;
        String firstName;
        String surname;
        do
        {
            firstName = getStringValueFromUser("First Name");
            if(firstName == null) return;
            surname = getStringValueFromUser("Surname");
            if(surname == null) return;
            person = getPersonByFirstAndSurnames(firstName, surname);
            if(person != null)
                showError("A Person with same First Name and Surname already exists!");
        }
        while(person != null);
        String gender = getOptionValueFromUser("Gender", "Male", "Female");
        if(gender == null) return;
        if(personType.equalsIgnoreCase(Child.PERSON_TYPE))
        {
            Double fee = getDoubleValueFromUser("Fee");
            if(fee == null) return;
            person = new Child(firstName, surname, gender, fee);
        }
        else
            person = new Parent(firstName, surname, gender);
        creche.add(person);
        JOptionPane.showMessageDialog(this, "Person registered successfully!");
    }

    public void listPersons (Collection<? extends Person> persons)
    {
        String str = "";
        if(persons != null && persons.size() > 0)
            for(Person person : persons)
                str += person.toString() + "\n";
        else
            str = "None found.";
        JOptionPane.showMessageDialog(this, new JTextArea(str));
    }

    public void findPerson ()
    {
        Person person;
        String firstName;
        String surname;
        firstName = getStringValueFromUser("First Name");
        if(firstName == null) return;
        surname = getStringValueFromUser("Surname");
        if(surname == null) return;
        person = getPersonByFirstAndSurnames(firstName, surname);
        if(person != null && person.getPersonType().equals(Parent.PERSON_TYPE))
        {
            String str = person.toString() + "\n";
            Set<Child> children = ((Parent) person).getChildren();
            str += "Child Count = " + (children != null ? children.size() : 0) + "\n";
            if(children != null && children.size() > 0)
                for(Child child : children)
                    str += child.toString() + "\n";
            JOptionPane.showMessageDialog(this, new JTextArea(str));
        }
    }

    public void removeAPerson ()
    {
        Integer id;
        do
        {
            id = getIntValueFromUser("Person Id");
            if(id != null)
            {
                for(Person person : creche)
                    if(person.getId() == id)
                    {
                        if(person.getPersonType().equals(Child.PERSON_TYPE))
                            for(Person pperson : creche)
                                if(pperson.getPersonType().equals(Parent.PERSON_TYPE))
                                {
                                    Parent parent = (Parent) pperson;
                                    Set<Child> children = parent.getChildren();
                                    if(children != null && children.size() > 0)
                                        for(Person cperson : children)
                                            if(cperson.getId() == person.getId())
                                                parent.getChildren().remove(person);
                                }
                        creche.remove(person);
                        JOptionPane.showMessageDialog(this, new JTextArea("Following Person removed:\n" + person.toString()));
                        break;
                    }
            }
        }
        while(id != null);
    }

    public void registerChildWithParent ()
    {
        Integer id;
        do
        {
            id = getIntValueFromUser("Parent Id");
            if(id != null)
            {
                Person person = getPersonById(id, Parent.PERSON_TYPE);
                if(person == null)
                {
                    showError("No Parent exists with this Id = " + id);
                    continue;
                }
                do
                {
                    Parent parent = (Parent) person;
                    id = getIntValueFromUser("Child Id");
                    if(id != null)
                    {
                        person = getPersonById(id, Child.PERSON_TYPE);
                        if(person == null)
                        {
                            showError("No Child exists with this Id = " + id);
                            continue;
                        }
                        if(parent.getChildren() == null)
                            parent.setChildren(new HashSet<Child>());
                        parent.getChildren().add((Child) person);
                        JOptionPane.showMessageDialog(this, "Child registered with Parent successfully!");
                        return;
                    }
                }
                while(id != null);
            }
        }
        while(id != null);
    }

    public void listChildrenOfParent()
    {
        Integer id;
        do
        {
            id = getIntValueFromUser("Parent Id");
            if(id != null)
            {
                Person person = getPersonById(id, Parent.PERSON_TYPE);
                if(person == null)
                {
                    showError("No Parent exists with this Id = " + id);
                    continue;
                }
                Parent parent = (Parent) person;
                listPersons(parent.getChildren());
                return;
            }
        }
        while(id != null);
    }

    public void calcTotalWeeklyFee ()
    {
        double totalFee = 0;
        if(creche != null && creche.size() > 0)
            for(Person person : creche)
                if(person.getPersonType().equals(Child.PERSON_TYPE))
                    totalFee += ((Child) person).getFee();
        JOptionPane.showMessageDialog(this, "Weekly Income for All Children is \u20ac" + (totalFee));
    }

    public static void main(String[] args) {
        new HappyWaysCrecheApp();
    }

    private String getOptionValueFromUser (String label, String...values)
    {
        String value;
        do
        {
            value = getStringValueFromUser("Enter '" + label + "' " + Arrays.toString(values) + " : ", true);
            if(value != null)
            {
                for(int i = 0; i < values.length; i++)
                {
                    if(value.equalsIgnoreCase(values[i]))
                        return value;
                    else if(i >= values.length - 1)
                        showError(getInvalidLabel(label));
                }
            }
        }
        while(value != null);
        return value;
    }

    private String getStringValueFromUser (String label)
    {
        return getStringValueFromUser(label, false);
    }

    private String getStringValueFromUser (String label, boolean displayLabelAlone)
    {
        return JOptionPane.showInputDialog(this, displayLabelAlone ? label : getEnterLabel(label));
    }

    public String getEnterLabel (String label)
    {
        return "Enter '" + label + "' : ";
    }

    public String getInvalidLabel (String label)
    {
        return "Invalid '" + label + "' value!";
    }

    private Integer getIntValueFromUser (String label)
    {
        return getIntValueFromUser(label, false);
    }

    private Integer getIntValueFromUser (String label, boolean displayLabelAlone)
    {
        do
        {
            String value = getStringValueFromUser(label, displayLabelAlone);
            if(value != null)
            {
                try
                {
                    return Integer.parseInt(value);
                }
                catch(Exception ignored)
                {
                    showError(getInvalidLabel(label));
                }
            }
            else
                return null;
        }
        while(true);
    }

    private Double getDoubleValueFromUser (String label)
    {
        return getDoubleValueFromUser(label, false);
    }

    private Double getDoubleValueFromUser (String label, boolean displayLabelAlone)
    {
        Double dvalue = null;
        String value = getStringValueFromUser(label, displayLabelAlone);
        if(value != null)
        {
            try
            {
                dvalue = Double.parseDouble(value);
            }
            catch(Exception ignored)
            {}
        }
        return dvalue;
    }

    private void showError (String error)
    {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == tfOption && e.getKeyCode() == KeyEvent.VK_ENTER)
            btnOk.doClick();
    }

    private Person getPersonById(int pid, String personType)
    {
        if(creche != null && creche.size() > 0)
        {
            for (Person person : creche) 
            {
                if(person != null && person.getPersonType().equals(personType) && person.getId() == pid)
                    return person;
            }
        }
        return null;
    }

    private Person getPersonByFirstAndSurnames (String firstName, String surname)
    {
        if(creche != null && creche.size() > 0)
        {
            for(Person person : creche)
            {
                if(person != null && person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getSurname().equalsIgnoreCase(surname))
                    return person;
            }
        }
        return null;
    }

}
