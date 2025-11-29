import java.util.*;

class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }
}

class Student {
    int rollNumber;
    String studentName;
    int[] marks;

    Student(int rollNumber, String studentName, int[] marks) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.marks = marks;
    }

    void validateMarks() throws InvalidMarksException {
        for (int m : marks) {
            if (m < 0 || m > 100) {
                throw new InvalidMarksException("Marks must be between 0 and 100");
            }
        }
    }

    double calculateAverage() {
        int sum = 0;
        for (int m : marks) sum += m;
        return sum / 3.0;
    }

    void displayResult() {
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Name: " + studentName);
        System.out.println("Marks: " + Arrays.toString(marks));
        double avg = calculateAverage();
        System.out.println("Average: " + avg);

        boolean pass = true;
        for (int m : marks) {
            if (m < 35) pass = false;
        }

        System.out.println("Result: " + (pass ? "Pass" : "Fail"));
    }
}

class Main {   // 👈 FIXED

    Student[] students = new Student[100];
    int count = 0;
    Scanner sc = new Scanner(System.in);

    void addStudent() {
        try {
            System.out.print("Enter Roll Number: ");
            int roll = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            int[] marks = new int[3];
            System.out.println("Enter 3 subject marks:");
            for (int i = 0; i < 3; i++) {
                marks[i] = sc.nextInt();
            }

            Student s = new Student(roll, name, marks);
            s.validateMarks();

            students[count++] = s;
            System.out.println("Student Added Successfully");

        } catch (InvalidMarksException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type");
            sc.nextLine();
        }
    }

    void showStudentDetails() {
        System.out.print("Enter Roll Number: ");
        int roll = sc.nextInt();

        for (int i = 0; i < count; i++) {
            if (students[i].rollNumber == roll) {
                students[i].displayResult();
                return;
            }
        }
        System.out.println("Student not found");
    }

    void mainMenu() {
        while (true) {
            System.out.println("\n1. Add Student");
            System.out.println("2. Show Student Details");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int ch;
            try {
                ch = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice!");
                sc.nextLine();
                continue;
            }

            if (ch == 1) addStudent();
            else if (ch == 2) showStudentDetails();
            else if (ch == 3) break;
            else System.out.println("Invalid choice");
        }

        System.out.println("Closing the program...");
        sc.close();
    }

    public static void main(String[] args) {
        Main rm = new Main();   // 👈 FIXED
        rm.mainMenu();
    }
}