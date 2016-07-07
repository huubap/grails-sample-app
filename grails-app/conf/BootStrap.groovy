import com.yourapp.Role
import com.yourapp.User
import com.yourapp.UserRole

class BootStrap {

    def init = { servletContext ->
        def adminUser = new User(username: "admin", password: "pass").save()
        def employeeUser = new User(username: "employee", password: "pass").save()

        def adminRole = new Role(authority: "ROLE_ADMIN").save()
        def employeeRole = new Role(authority: "ROLE_USER").save()
        UserRole.create(adminUser, adminRole, true)
        UserRole.create(employeeUser, employeeRole, true)
    }
    def destroy = {
    }
}
