import com.yourapp.Profile
import com.yourapp.Role
import com.yourapp.Tenant
import com.yourapp.User
import com.yourapp.UserRole

class BootStrap {

    def init = { servletContext ->
        def tenant = new Tenant(code: "abc").save()

        def adminRole = new Role(authority: "ROLE_ADMIN").save()
        def userRole = new Role(authority: "ROLE_USER").save()

        def adminProfile = new Profile(tenant: tenant).save()
        def userProfile = new Profile(tenant: tenant).save()

        def userWithAdminRole = new User(username: "admin@huubap.com", altUsername: "admin", password: "pass", tenant: tenant, profile: adminProfile).save()
        def userWithUserRole = new User(username: "user@huubap.com", altUsername: "user", password: "pass", tenant: tenant, profile: userProfile).save()

        UserRole.create(userWithAdminRole, adminRole, true)
        UserRole.create(userWithUserRole, userRole, true)
    }
    def destroy = {
    }
}
