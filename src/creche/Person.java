package creche;
//@author Daniel Hickey
//@version 1st March 2013
//This is the Person Class which is the superclass of Child and Parent.

public abstract class Person {

    private static int autoIncrementedId = 0;
    private String firstName;
    private String surname;
    private String gender;

    protected Person(String firstName, String surname, String gender) {
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
    }

    public abstract String getPersonType();

    public abstract int getId();

    protected static synchronized int nextId ()
    {
        return ++autoIncrementedId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return this.getPersonType() + " {" +
                "id=" + this.getId() +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    // Determines whether two objects are equal
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().getName().equals(this.getClass().getName()) && this.getId() == ((Child) obj).getId();
    }
}
