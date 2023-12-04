import java.sql.*;
import java.util.ArrayList;
import java.util.Formatter;
import javax.print.Doc;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Scanner;
class Person{// Base class person that includes attributes and methods common for doctor and patient
    Scanner sc= new Scanner(System.in);
    String name, gender;
    long phn;
    void createPerson(){//Create person object
        System.out.println("****************Fill your details****************");
        System.out.print("Enter name: ");
        this.name = sc.nextLine();

        System.out.print("Enter phone number: ");
        this.phn = sc.nextLong();

        System.out.print("Enter Gender(F/M/O): ");
        this.gender = sc.next();

    }
    void getDetail() {//function to show all details of person

        System.out.println("Name = " + name);
        System.out.println("Gender = " + gender);
        System.out.println("Phone number = " + phn);
    }
}
class Patient extends Person{//patient class to inherit person and adding some more attributes
//    static Patient[] patients = new Patient[20];
static ArrayList<Patient> patients = new ArrayList<>();
    Scanner sc= new Scanner(System.in);
    int Pid=1000;
    Boolean ins;
    static int count = 0;
    void createPatient(){//Create patient object
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "root");
            System.out.println("Patient Details: ");
            super.createPerson();
            System.out.print("Do you have an Insurance (Y=true/N=false): ");
            this.ins = sc.nextBoolean();
            patients.add(this);
            count++;
            this.Pid+=count;
            PreparedStatement stm = con.prepareStatement("INSERT INTO Patients VALUES(?,?,?,?,?)");
            stm.setInt(1,this.Pid);
            stm.setString(2,this.name);
            stm.setLong(3,this.phn);
            stm.setString(4,this.gender);
            stm.setBoolean(5,this.ins);
            stm.executeUpdate();
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    void getDetail(){
        System.out.println("Patient's ID: "+Pid);
        super.getDetail();
        System.out.println("Have Insurance = "+ins);
    }
}

class Doctor extends Person{//Inheriting person in doctor class
//    static Doctor[] doctors = new Doctor[20];
static ArrayList<Doctor> doctors = new ArrayList<>();
    Scanner sc= new Scanner(System.in);
    int Did=2000;
    String splz;
    int fee;
    static int count = 0;

    void createDoctor(){// creating doctor object
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "root");
            System.out.println("Doctor's Details: ");
            super.createPerson();
            System.out.print("Enter Speciallization: ");
            this.splz = sc.nextLine();

            System.out.print("Enter Consultation fees: ");
            this.fee = sc.nextInt();

            doctors.add(this);
            count++;
            this.Did+=count;
            PreparedStatement stm = con.prepareStatement("INSERT INTO Doctors VALUES(?,?,?,?,?,?)");
            stm.setInt(1,this.Did);
            stm.setString(2,this.name);
            stm.setLong(3,this.phn);
            stm.setString(4,this.gender);
            stm.setString(5,this.splz);
            stm.setInt(6,this.fee);
            stm.executeUpdate();
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    void getDetail(){
        System.out.println("Doctor's ID: "+Did);
        super.getDetail();
        System.out.println("Spciallized in"+splz);
        System.out.println("Consultation fee: "+fee);
    }
}
class Appointment {// class for booking appointments
    int appId = 3001;
    private int docID;
    private int patID;
    private String date;
    private String time;
//    static Appointment[] appointments = new Appointment[30];
static ArrayList<Appointment> appointments = new ArrayList<>();
    static int countAppointments = 0;
    public Appointment(int docId, int patId, String date, String time){   //constructor booking appointments
        this.docID = docId;
        this.patID = patId;
        this.date = date;
        this.time = time;
        appointments.add(this);
        appId+=countAppointments;
        countAppointments++;

    }

    public static void showAppointments(){//function to show appointments booked till now
        //this will show all the appointments of all the doctors
        int size = appointments.size();
        if(size==0){
            System.out.println("No Appointments for today");
            return;
        }
        System.out.printf("%20s %20s %20s %20s %20s \n","Appointment ID","Doctor's name","patient's name", "Date" ,"Time");
        for(int i=0;i<countAppointments;i++){
            System.out.printf("%20s %20s %20s %20s %20s\n \n",appointments.get(i).appId,appointments.get(i).docID,appointments.get(i).patID,appointments.get(i).date,appointments.get(i).time);
        }
    }
    public static void showDoctorApp(int docId) {//show appointments of specified doctor
        if(countAppointments==0){
            System.out.println("No Appointments");
        }
        Boolean flag = false;
        System.out.printf("%20s %20s %20s %20s","Doctor's name","patient's name", "Date" ,"Time");
        for(int i=0;i<countAppointments;i++){
            if(appointments.get(i).docID==docId){
                System.out.printf("%20s %20s %20s %20s %20s\n \n",appointments.get(i).appId,appointments.get(i).docID,appointments.get(i).patID,appointments.get(i).date,appointments.get(i).time);
                flag=true;
            }
        }
        if(!flag)
            System.out.println("No Appointments for this Id");
    }

    public static void showPatientApp(int patId){// show appointments of specified patient
        boolean found = false;
        int ind=-1;
        for(int i=0;i<countAppointments;i++){
            if(appointments.get(i).patID==patId){
                ind = i;
                found=true;
            }
        }
        if(found){
            System.out.printf("%20s %20s %20s %20s \n","Doctor's name","patient's name", "Date" ,"Time");
            System.out.printf("%20s %20s %20s %20s %20s\n \n",appointments.get(ind).appId,appointments.get(ind).docID,appointments.get(ind).patID,appointments.get(ind).date,appointments.get(ind).time);

        }
        else{
            System.out.println("No such appointments scheduled for this patient");
        }
    }

    public static void DenyApp(int AppId){//To cancel an appointment

        int ind=0;
        boolean found = false;
        for(int i=0;i<countAppointments;i++){
            if(appointments.get(i).appId == AppId){
                ind =i;
                found = true;
            }
        }
        if(found){
            appointments.remove(ind);
            countAppointments--;
        }
        else{
            System.out.println("Appointment not found");
        }
    }
    public static void rescheApp(int id,String t, String d){//To Reschedule any appointment
        int ind=0;
        boolean found = false;
        for(int i=0;i<countAppointments;i++){
            if(appointments.get(i).appId==id){
                ind = i;
                found = true;
                appointments.get(i).time = t;
                appointments.get(i).date = d;
                break;
            }
        }
        if(found) {
            System.out.println("Re-Schedule done!");
            System.out.printf("%20s %20s %20s %20s \n", "Doctor's Id", "patient's Id", "Date", "Time");
            System.out.printf("%20s %20s %20s %20s \n\n", appointments.get(ind).docID, appointments.get(ind).patID, appointments.get(ind).date, appointments.get(ind).time);
        }
        else{
            System.out.println("No records to ReSchedule for the given input");
        }
    }
}

public class Hospital {
    static void showDocMenu(){//menu driven options for Doctor's portal
        System.out.println("1. New Doctor joining Registeration");
        System.out.println("2: Scheduled Appointments for the day");
        System.out.println("3: Get Doctor's List");
        System.out.println("4: Deny an Appointment");
        System.out.println("5: Reschedule Appointment");
        System.out.println("6: Patients List");
        System.out.println("7: Exit");
        System.out.println("Press 0 to switch to Patient's Portal");
    }

    static void showPatMenu(){//menu driven options for Patient's portal
        System.out.println("1. New Patient's admitting Registeration");
        System.out.println("2: Book Your Appointment");
        System.out.println("3: Your Scheduled Appointments");
        System.out.println("4: Check Doctor's Details");
        System.out.println("5: Doctors Available for you");
        System.out.println("6: Exit");
        System.out.println("Press 0 to switch to Doctor's portal");
    }
    static void showPatList(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "root");
            System.out.printf("%20s %20s %20s %20s %20s \n","Patient's Id","patient's name", "Phone number" ,"Gender", "Insurance");
            String selQuery = "select * from patients";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(selQuery);
            while(rs.next()){
                String col1 = Integer.toString(rs.getInt(1));
                String col2 = rs.getString(2);
                String col3 = Integer.toString(rs.getInt(3));
                String col4 = rs.getString(4);
                String col5 = Boolean.toString(rs.getBoolean(5));
                System.out.printf("%20s %20s %20s %20s %20s \n",col1,col2, col3 ,col4, col5);
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    static void showDocList(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "root");
            System.out.printf("%20s %20s %20s %20s %20s %20s \n", "Doctor's ID","Doctor's name", "Phone number", "Gender", "Speciallization", "Consultation fees");
            String selQuery = "select * from Doctors";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(selQuery);
            while(rs.next()){
                String col1 = Integer.toString(rs.getInt(1));
                String col2 = rs.getString(2);
                String col3 = Integer.toString(rs.getInt(3));
                String col4 = rs.getString(4);
                String col5 = rs.getString(5);
                String col6 = Integer.toString(rs.getInt(6));
                System.out.printf("%20s %20s %20s %20s %20s %20s\n",col1,col2, col3 ,col4, col5,col6);
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    static void docOperations() {//function contains The Menu Driven operations for doctor's portal
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Select: ");
            showDocMenu();
            int choice = sc.nextInt();
            switch (choice) {
                case (1)://New Doctor joining Registeration
                    Doctor doc = new Doctor();
                    doc.createDoctor();
                    showDocList();
                    break;
                case (2)://Scheduled Appointments for the day
                    Appointment.showAppointments();
                    break;
                case (3)://Get Doctor's List
                    ArrayList<Doctor> arr1 = Doctor.doctors;
                    if(Doctor.count==0){
                        System.out.println("no records");
                        break;
                    }
                    showDocList();
                    break;
                case (4)://Deny an Appointment
                    if(Appointment.countAppointments==0){
                        System.out.println("No appointments");
                        break;
                    }
                    System.out.println("These are the scheduled appointments");
                    Appointment.showAppointments();
                    System.out.print("Enter the Appointment ID: ");
                    int id = sc.nextInt();
                    Appointment.DenyApp(id);
                    break;

                case (5)://Reschedule Appointment
                    if(Appointment.countAppointments==0){
                        System.out.println("No Appointments to re-Schedule");
                        break;
                    }
                    System.out.println("available appointments");
                    Appointment.showAppointments();
                    System.out.println("Enter App id: ");
                    id = sc.nextInt();
                    System.out.println("Enter new date: ");
                    String date = sc.next();
                    System.out.println("Enter new time");
                    String time = sc.next();
                    Appointment.rescheApp(id,time,date);
                    break;
                case(6):
                    showPatList();
                    break;
                case(7)://Exit
                    try{
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "root");
                        Statement stm = con.createStatement();
                        String selQuery = "delete from patients";
                        String selQuery1 = "delete from doctors";
                        stm.executeUpdate(selQuery);
                        stm.executeUpdate(selQuery1);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                    System.exit(0);
                default://switch to Patient's Portal
                    patOperations();
            }
        }
    }
    static void patOperations(){//function contains The menu driven operations for patient's portal
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Select: ");
            showPatMenu();
            int choice = sc.nextInt();
            switch (choice) {
                case (1)://New Patient's admitting Registeration
                    Patient pat = new Patient();
                    pat.createPatient();
                    System.out.println("Updated list of Patients");
                    showPatList();
                    break;
                case (2)://Book Your Appointment
//                    Doctor doc = new Doctor();
//                    doc.createDoctor();
//                    Patient pat1 = new Patient();
//                    pat1.createPatient();
                    if(Doctor.count==0){
                        System.out.println("No Doctors available for appointment");
                        break;
                    }
                    System.out.println("Doctors available: ");
                    showDocList();
                    System.out.println("Enter Patient ID: ");
                    int id = sc.nextInt();
                    System.out.println("Enter Doctor's ID for Appointment: ");
                    int id1 = sc.nextInt();
                    String date, time;
                    System.out.print("Enter date: ");
                    date = sc.next();
                    System.out.print("Enter time: ");
                    time = sc.next();
                    Appointment ap = new Appointment(id1,id,date,time);
                    Appointment.showAppointments();
                    break;
                case (3)://Your Scheduled Appointments
                    System.out.print("enter patient's Id: ");
                    int Id = sc.nextInt();
                    Appointment.showPatientApp(Id);
                    break;
                case (4)://Check Doctor's Details
                    if(Doctor.count==0){
                        System.out.println("No Doctor's available right now");
                        break;
                    }
                    System.out.println("Showing doctors list");
                    ArrayList<Doctor> arr = Doctor.doctors;
//                    System.out.printf("%20s %20s \n", "Doctor's id", "Doctor's name");
//                    for (int i = 0; i < Doctor.count; i++)
//                        System.out.printf("%20s %20s \n", arr.get(i).Did, arr.get(i).name);
                    showDocList();
                    System.out.println("which doctor's details you want enter id: ");
                    id = sc.nextInt();
                    boolean found = false;
                    for (int i = 0; i < Doctor.count; i++){
                        if(arr.get(i).Did == id){
                            found = true;
                            arr.get(i).getDetail();
                        }
                    }
                    if(!found){
                        System.out.println("No records");
                    }
                    break;
                case (5)://Doctors Available for you
                    if(Doctor.count==0){
                        System.out.println("No doctors available right now");
                        break;
                    }
                   showDocList();
                    break;

                case(6)://Exit
                    try{
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "root");
                        Statement stm = con.createStatement();
                        String selQuery = "delete from patients";
                        String selQuery1 = "delete from doctors";
                        stm.execute(selQuery);
                        stm.execute(selQuery1);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                    System.exit(0);
                default://switch to Doctor's portal
                    docOperations();
            }
        }
    }
    public static void main(String[] args) {
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "root");
            Statement stm = con.createStatement();
            String selQuery = "delete from patients";
            String selQuery1 = "delete from doctors";
            stm.execute(selQuery);
            stm.execute(selQuery1);
        }catch(Exception e){
            System.out.println(e);
        }//this is to clear old data that causes abnormal result in new run.
        try{
        Scanner sc = new Scanner(System.in);
        System.out.println("********* Welcome to the Portal *********");
        System.out.println("Select the designation");
        System.out.println("1: Patient \n2: Doctor");
        int person = sc.nextInt();
        sc.nextLine();
            switch (person) {
                case (1):
                    System.out.println("Patient's Portal");
                    patOperations();
                    break;
                case (2):
                    System.out.println("Doctor's Portal");
                    docOperations();
                    break;
            }
        }catch(Exception e){
            System.out.println("Some Error Occured exiting...");
            System.out.println(e);
        }
    }
}
