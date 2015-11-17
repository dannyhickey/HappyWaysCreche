package creche;

//@author Daniel Hickey
//@version 1st March 2013
//This is the child class which is a subclass of the Person class

public class Child extends Person {

    public static final String PERSON_TYPE = "Child";

    private int id;
    private double fee;

    public Child()
    {
        this(null, null, null, 0);
    }

    public Child(String firstName, String surname, String gender, double fee)
    {
        super(firstName, surname, gender);
        this.id = nextId();
        this.fee = fee;
    }

    @Override
    public String getPersonType() {
        return PERSON_TYPE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

}
