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
        System.out.println("****************Fill your details to register yourself****************");
        System.out.print("Enter your name: ");
        this.name = sc.nextLine();

        System.out.print("Enter your phone number: ");
        this.phn = sc.nextLong();

        System.out.print("Enter your Gender(F/M/O): ");
        this.gender = sc.next();

    }
    void getDetail() {//function to show all details of person

        System.out.println("Name = " + name);
        System.out.println("Gender = " + gender);
        System.out.println("Phone number = " + phn);
    }
}
class Patient extends Person{//patient class to inherit person and adding some more attributes
    static Patient[] patients = new Patient[20];

    Scanner sc= new Scanner(System.in);
    int Pid=1000;
    Boolean ins;
    static int count = 0;
    void createPatient(){//Create patient object
        super.createPerson();
        System.out.print("Do you have an Insurance (Y=true/N=false): ");
        this.ins = sc.nextBoolean();
        patients[count] = this;
        count++;
        this.Pid+=count;
    }
    void getDetail(){
        System.out.println("Patient's ID: "+Pid);
        super.getDetail();
        System.out.println("Have Insurance = "+ins);
    }
}

class Doctor extends Person{//Inheriting person in doctor class
    static Doctor[] doctors = new Doctor[20];
    Scanner sc= new Scanner(System.in);
    int Did=2000;
    String splz;
    int fee;
    static int count = 0;

    void createDoctor(){// creating doctor object
        super.createPerson();
        System.out.print("Enter your Speciallization: ");
        this.splz = sc.nextLine();

        System.out.print("Enter your Consultation fees: ");
        this.fee = sc.nextInt();

        doctors[count] = this;
        count++;
        this.Did+=count;
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
    private Doctor doctor;
    private Patient patient;
    private String date;
    private String time;
    static Appointment[] appointments = new Appointment[30];

    static int countAppointments = 0;
    public Appointment(Doctor doctor, Patient patient, String date, String time){   //constructor booking appointments
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.time = time;
        appointments[countAppointments] = this;
        appId+=countAppointments;
        countAppointments++;

    }

    public static void showAppointments(){//function to show appointments booked till now
        //this will show all the appointments of all the doctors
        int size = appointments.length;
        System.out.printf("%20s %20s %20s %20s %20s \n","Appointment ID","Doctor's name","patient's name", "Date" ,"Time");
        for(int i=0;i<countAppointments;i++){
            System.out.printf("%20s %20s %20s %20s %20s\n \n",appointments[i].appId,appointments[i].doctor.name,appointments[i].patient.name,appointments[i].date,appointments[i].time);
        }
    }
    public static void showDoctorApp(String docName) {//show appointments of specified doctor
        System.out.printf("%20s %20s %20s %20s","Doctor's name","patient's name", "Date" ,"Time");
        for(int i=0;i<countAppointments;i++){
            if(appointments[i].doctor.name.equalsIgnoreCase(docName))
                System.out.printf("%20s %20s %20s %20s\n \n",appointments[i].doctor.name,appointments[i].patient.name,appointments[i].date,appointments[i].time);
        }
    }

    public static void showPatientApp(String patName){// show appointments of specified patient
        boolean found = false;
        int ind=-1;
        for(int i=0;i<countAppointments;i++){
            if(appointments[i].patient.name.equalsIgnoreCase(patName)){
                ind = i;
                found=true;
            }
        }
        if(found){
            System.out.printf("%20s %20s %20s %20s \n","Doctor's name","patient's name", "Date" ,"Time");
            System.out.printf("%20s %20s %20s %20s\n \n",appointments[ind].doctor.name,appointments[ind].patient.name,appointments[ind].date,appointments[ind].time);
        }
        else{
            System.out.println("No such Patient in records");
        }
    }

    public static void DenyApp(int AppId){//To cancel an appointment

        int ind=0;
        boolean found = false;
        for(int i=0;i<countAppointments;i++){
            if(appointments[i].appId == AppId){
                ind =i;
                found = true;
            }
        }
        countAppointments--;
        if(found) {
            for (int i = ind; i < countAppointments - 1; i++) {
                appointments[i] = appointments[i + 1];
            }
            appointments[countAppointments] = null;
            System.out.println("Appointement Cancelled!");
        }
    }
    public static void rescheApp(int id,String t, String d){//To Reschedule any appointment
        int ind=0;
        boolean found = false;
        for(int i=0;i<countAppointments;i++){
            if(appointments[i].appId==id){
                ind = i;
                found = true;
                appointments[i].time = t;
                appointments[i].date = d;
                break;
            }
        }
        if(found) {
            System.out.println("Re-Schedule done!");
            System.out.printf("%20s %20s %20s %20s \n", "Doctor's name", "patient's name", "Date", "Time");
            System.out.printf("%20s %20s %20s %20s \n\n", appointments[ind].doctor.name, appointments[ind].patient.name, appointments[ind].date, appointments[ind].time);
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
        System.out.println("6: Exit");
        System.out.println("Press any key to switch to Patient's Portal");
    }

    static void showPatMenu(){//menu driven options for Patient's portal
        System.out.println("1. New Patient's admitting Registeration");
        System.out.println("2: Book Your Appointment");
        System.out.println("3: Your Scheduled Appointments");
        System.out.println("4: Check Doctor's Details");
        System.out.println("5: Doctors Available for you");
        System.out.println("6: Exit");
        System.out.println("Press any key to switch to Doctor's portal");
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
                    Doctor[] arr = Doctor.doctors;
                    System.out.printf("%20s %20s %20s %20s %20s \n", "Doctor's name", "Phone number", "Gender", "Speciallization", "Consultation fees");
                    for (int i = 0; i < Doctor.count; i++)
                        System.out.printf("%20s %20s %20s %20s %20s\n", arr[i].name, arr[i].phn, arr[i].gender, arr[i].splz, arr[i].fee);
                    break;
                case (2)://Scheduled Appointments for the day
                    Appointment.showAppointments();
                    break;
                case (3)://Get Doctor's List
                    Doctor[] arr1 = Doctor.doctors;
                    if(Doctor.count==0){
                        System.out.println("no records");
                        break;
                    }
                    System.out.printf("%20s %20s %20s %20s %20s \n", "Doctor's name", "Phone number", "Gender", "Speciallization", "Consultation fees");
                    for (int i = 0; i < Doctor.count; i++)
                        System.out.printf("%20s %20s %20s %20s %20s \n", arr1[i].name, arr1[i].phn, arr1[i].gender, arr1[i].splz, arr1[i].fee);
                    break;
                case (4)://Deny an Appointment
                    System.out.println("These are the scheduled appointments");
                    Appointment.showAppointments();
                    System.out.print("Enter the Appointment ID: ");
                    int id = sc.nextInt();
                    Appointment.DenyApp(id);
                    break;

                case (5)://Reschedule Appointment
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
                case(6)://Exit
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
                    Patient[] ar = Patient.patients;
                    System.out.printf("%20s %20s %20s %20s \n","Patient's Id","patient's name", "Phone number" ,"Gender", "Insurance");
                    for (int i = 0; i < Patient.count; i++)
                        System.out.printf("%20s %20s %20s %20s \n", ar[i].Pid, ar[i].name, ar[i].phn, ar[i].gender, ar[i].ins);
                    break;
                case (2)://Book Your Appointment
                    Doctor doc = new Doctor();
                    doc.createDoctor();
                    Patient pat1 = new Patient();
                    pat1.createPatient();
                    String date, time;
                    System.out.print("Enter date: ");
                    date = sc.next();
                    System.out.print("Enter time: ");
                    time = sc.next();
                    Appointment ap = new Appointment(doc,pat1,date,time);
                    Appointment.showAppointments();
                    break;
                case (3)://Your Scheduled Appointments
                    String name;
                    System.out.print("enter patient's name: ");
                    name = sc.next();
                    Appointment.showPatientApp(name);
                    break;
                case (4)://Check Doctor's Details
                    System.out.println("Showing doctors list");
                    Doctor[] arr = Doctor.doctors;
                    System.out.printf("%20s %20s \n", "Doctor's id", "Doctor's name");
                    for (int i = 0; i < Doctor.count; i++)
                        System.out.printf("%20s %20s \n", arr[i].Did, arr[i].name);
                    System.out.println("which doctor's details you want enter id: ");
                    int id = sc.nextInt();
                    boolean found = false;
                    for (int i = 0; i < Doctor.count; i++){
                        if(arr[i].Did == id){
                            found = true;
                            arr[i].getDetail();
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
                    Doctor[] arr1 = Doctor.doctors;
                    System.out.printf("%20s %20s %20s %20s \n", "Doctor's name", "Phone number", "Gender", "Speciallization", "Consultation fees");
                    for (int i = 0; i < Doctor.count; i++)
                        System.out.printf("%20s %20s %20s %20s \n", arr1[i].name, arr1[i].phn, arr1[i].gender, arr1[i].splz, arr1[i].fee);
                    break;
                case(6)://Exit
                    System.exit(0);
                default://switch to Doctor's portal
                    docOperations();
            }
        }
    }
    public static void main(String[] args) {
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
