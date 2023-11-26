package at.htl.control;

import at.htl.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public boolean checkMail(String eMail) {
        String expectedDomain = "@htl-leonding.ac.at";

        if (eMail.endsWith(expectedDomain)) {
            // Trenne den Benutzernamen und die Domain
            String[] parts = eMail.split("@");
            String username = parts[0];

            // Trenne den Vornamen und den Nachnamen
            String[] nameParts = username.split("\\.");

            // Überprüfe, ob die Struktur der E-Mail korrekt ist
            if (nameParts.length == 2 && nameParts[0].length() == 1 && !nameParts[1].isEmpty()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
