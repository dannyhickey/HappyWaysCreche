package creche;
//@author Daniel Hickey
//@version 1st March 2013
//This is the parent class which is a subclass of the Person class. 
import java.util.Set;

public class Parent extends Person {

    public static final String PERSON_TYPE = "Parent";

    private int id;
    private Set<Child> children;

    public Parent()
    {
        this(null, null, null);
    }

    public Parent(String firstName, String surname, String gender)
    {
        this(firstName, surname, gender, null);
    }

    public Parent(String firstName, String surname, String gender, Set<Child> children)
    {
        super(firstName, surname, gender);
        this.id = nextId();
        this.children = children;
    }

    @Override
    public String getPersonType() {
        return PERSON_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

}
