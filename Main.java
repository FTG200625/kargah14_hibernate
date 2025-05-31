package ap.aut;

import jakarta.persistence.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static SessionFactory sessionFactory;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        setUpSessionFactory();

        try {
            runApplication();
        } finally {
            closeSessionFactory();
        }
    }

    private static void setUpSessionFactory() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static void closeSessionFactory() {
        sessionFactory.close();
    }

    private static void runApplication() {
        while (true) {
            System.out.println("\n[L]ogin, [S]ign up:");
            String choice = scanner.nextLine();

            if (choice.equals("l") || choice.equals("login")) {
                loginUser();
                break;
            } else if (choice.equals("s") || choice.equals("sign up")) {
                registerUser();
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.println("\n--- Registration ---");
        User user = new User();

        System.out.print("First Name: ");
        user.setFirstName(scanner.nextLine());

        System.out.print("Last Name: ");
        user.setLastName(scanner.nextLine());

        System.out.print("Age: ");
        user.setAge(Integer.parseInt(scanner.nextLine()));

        System.out.print("Email: ");
        String email = scanner.nextLine();
        user.setEmail(email);

        System.out.print("Password: ");
        String password = scanner.nextLine();
        if (password.length() < 8) {
            System.out.println("Error: Password must be at least 8 characters.");
            return;
        }
        user.setPassword(password);

        sessionFactory.inTransaction(session -> {

            User existingUser = session.get(User.class, user.getEmail());
            if (existingUser != null) {
                System.out.println("Error: Email already registered.");
                return;
            }

            session.persist(user);
            System.out.println("Registration successful!");
        });
    }

    private static void loginUser() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        sessionFactory.inTransaction(session -> {
            User user = session.get(User.class, email);
            if (user != null && user.getPassword().equals(password)) {
                System.out.printf("\nWelcome, %s %s!\n",
                        user.getFirstName(), user.getLastName());
            } else {
                System.out.println("Error: Invalid email or password.");
            }
        });
    }

}

@Entity
@Table(name = "users")
class User {

    @Basic(optional = false)
    private String email;

    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;

    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;

    @Basic(optional = false)
    private Integer age;

    @Basic(optional = false)
    private String password;

    @Basic(optional = false)
    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();


    public User() {}

    public User(String firstName, String lastName, int age, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
    }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getCreatedAt() { return createdAt; }
}